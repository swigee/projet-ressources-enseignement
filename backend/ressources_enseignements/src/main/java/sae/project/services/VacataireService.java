package sae.project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sae.project.dtos.vacataire.VacataireDTO;
import sae.project.model.User;
import sae.project.model.Vacataire;
import sae.project.repositories.UserRepository;
import sae.project.repositories.VacataireRepository;

import java.text.Normalizer;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class VacataireService {

    public static final String STATUS_TO_CONTACT = "A_CONTACTER";
    public static final String STATUS_IN_PROGRESS = "EN_COURS";
    public static final String STATUS_VALIDATED   = "VALIDE";

    @Autowired
    private VacataireRepository vacataireRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ── CRUD ────────────────────────────────────────

    public List<VacataireDTO> getAll() {
        return vacataireRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public VacataireDTO getById(Integer id) {
        Vacataire contractor = vacataireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contractor not found"));
        return toDTO(contractor);
    }

    public VacataireDTO create(VacataireDTO dto) {
        Vacataire contractor = fromDTO(dto);
        if (contractor.getStatus() == null) contractor.setStatus(STATUS_TO_CONTACT);
        return toDTO(vacataireRepository.save(contractor));
    }

    /**
     * Update a contractor profile.
     * If the status changes to VALIDATED and no user account exists yet,
     * a User account is automatically created using the contractor's first and last name.
     * A temporary password "ChangeMe123" is assigned.
     */
    public VacataireDTO update(Integer id, VacataireDTO dto) {
        Vacataire contractor = vacataireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contractor not found"));

        String previousStatus = contractor.getStatus();
        applyDTO(contractor, dto);

        boolean becomesValidated = STATUS_VALIDATED.equals(dto.getStatus())
                && !STATUS_VALIDATED.equals(previousStatus);

        if (becomesValidated && contractor.getUser() == null) {
            autoCreateUserAccount(contractor);
            log.info("User account auto-created for contractor id={} ({} {})",
                    id, contractor.getFirstName(), contractor.getLastName());
        }

        return toDTO(vacataireRepository.save(contractor));
    }

    public void delete(Integer id) {
        if (!vacataireRepository.existsById(id)) {
            throw new RuntimeException("Contractor not found");
        }
        vacataireRepository.deleteById(id);
    }

    public List<VacataireDTO> search(String keyword) {
        return vacataireRepository.searchByName(keyword).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── Filtering ───────────────────────────────────

    public List<VacataireDTO> getActive() {
        return vacataireRepository.findByUserIsNotNull().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<VacataireDTO> getPending() {
        return vacataireRepository.findByUserIsNull().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── Manual account conversion ────────────────────

    /**
     * Manually converts a contractor profile into an active user account
     * using caller-supplied credentials. Use this to override auto-generated ones.
     */
    @Transactional
    public VacataireDTO convertToUser(Integer contractorId, String username, String rawPassword) {
        log.info("Manual conversion of contractor {} to user account '{}'", contractorId, username);

        Vacataire contractor = vacataireRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found"));

        if (contractor.getUser() != null) {
            throw new IllegalStateException("This contractor already has an active user account (userId="
                    + contractor.getUser().getId() + ")");
        }
        if (userRepository.findByUsernameIgnoreCase(username).isPresent()) {
            throw new IllegalArgumentException("Username '" + username + "' is already taken");
        }

        User user = User.builder()
                .firstName(contractor.getFirstName())
                .lastName(contractor.getLastName())
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .type("VACATAIRE")
                .build();
        userRepository.save(user);

        contractor.setUser(user);
        contractor.setStatus(STATUS_VALIDATED);
        vacataireRepository.save(contractor);

        return toDTO(contractor);
    }

    // ── Private helpers ─────────────────────────────

    /**
     * Auto-creates a User account for the contractor.
     * Username pattern: "firstname.lastname" (lowercased, accent-free, unique).
     */
    private void autoCreateUserAccount(Vacataire contractor) {
        String base     = buildUsername(contractor.getFirstName(), contractor.getLastName());
        String username = resolveUniqueUsername(base);

        User user = User.builder()
                .firstName(contractor.getFirstName())
                .lastName(contractor.getLastName())
                .username(username)
                .password(passwordEncoder.encode("ChangeMe123"))
                .type("VACATAIRE")
                .build();

        userRepository.save(user);
        contractor.setUser(user);

        log.info("Auto-generated username '{}' for contractor '{} {}'",
                username, contractor.getFirstName(), contractor.getLastName());
    }

    /** Builds a candidate username from first and last name. */
    private String buildUsername(String firstName, String lastName) {
        String f = normalize(firstName != null ? firstName : "user");
        String l = normalize(lastName  != null ? lastName  : "unknown");
        return f + "." + l;
    }

    /** Strips accents, lowercases and removes non-alphanumeric characters (except dash). */
    private String normalize(String input) {
        String nfd = Normalizer.normalize(input.trim().toLowerCase(), Normalizer.Form.NFD);
        return nfd.replaceAll("[^\\p{ASCII}]", "")
                  .replaceAll("[^a-z0-9\\-]", "");
    }

    /** Appends an incrementing suffix until the username is unique. */
    private String resolveUniqueUsername(String base) {
        if (userRepository.findByUsernameIgnoreCase(base).isEmpty()) return base;
        int suffix = 2;
        while (true) {
            String candidate = base + suffix;
            if (userRepository.findByUsernameIgnoreCase(candidate).isEmpty()) return candidate;
            suffix++;
        }
    }

    // ── Mapping ─────────────────────────────────────

    private VacataireDTO toDTO(Vacataire v) {
        return VacataireDTO.builder()
                .id(v.getId())
                .recruitmentManager(v.getRecruitmentManager())
                .firstName(v.getFirstName())
                .lastName(v.getLastName())
                .birthDate(v.getBirthDate())
                .department(v.getDepartment())
                .position(v.getPosition())
                .experience(v.getExperience())
                .profile(v.getProfile())
                .skills(v.getSkills())
                .advanceNotice(v.getAdvanceNotice())
                .institution(v.getInstitution())
                .site(v.getSite())
                .sentToManager(v.getSentToManager())
                .managerSignature(v.getManagerSignature())
                .knowledgeSource(v.getKnowledgeSource())
                .otherKnowledgeSource(v.getOtherKnowledgeSource())
                .status(v.getStatus())
                .userId(v.getUser() != null ? v.getUser().getId() : null)
                .accountActive(v.getUser() != null)
                .build();
    }

    private Vacataire fromDTO(VacataireDTO dto) {
        Vacataire v = new Vacataire();
        applyDTO(v, dto);
        return v;
    }

    private void applyDTO(Vacataire v, VacataireDTO dto) {
        v.setRecruitmentManager(dto.getRecruitmentManager());
        v.setFirstName(dto.getFirstName());
        v.setLastName(dto.getLastName());
        v.setBirthDate(dto.getBirthDate());
        v.setDepartment(dto.getDepartment());
        v.setPosition(dto.getPosition());
        v.setExperience(dto.getExperience());
        v.setProfile(dto.getProfile());
        v.setSkills(dto.getSkills());
        v.setAdvanceNotice(dto.getAdvanceNotice());
        v.setInstitution(dto.getInstitution());
        v.setSite(dto.getSite());
        v.setSentToManager(dto.getSentToManager());
        v.setManagerSignature(dto.getManagerSignature());
        v.setKnowledgeSource(dto.getKnowledgeSource());
        v.setOtherKnowledgeSource(dto.getOtherKnowledgeSource());
        if (dto.getStatus() != null) v.setStatus(dto.getStatus());
        // userId and accountActive are read-only output fields
    }
}
