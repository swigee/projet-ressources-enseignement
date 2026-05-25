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

    /** Allowed statut values — must match the frontend dropdown. */
    public static final String STATUT_A_CONTACTER = "A_CONTACTER";
    public static final String STATUT_EN_COURS     = "EN_COURS";
    public static final String STATUT_VALIDE       = "VALIDE";

    @Autowired
    private VacataireRepository vacataireRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ───────────────────────────────────────────────
    // CRUD
    // ───────────────────────────────────────────────

    public List<VacataireDTO> getAll() {
        return vacataireRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public VacataireDTO getById(Integer id) {
        Vacataire v = vacataireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contractor not found"));
        return toDTO(v);
    }

    public VacataireDTO create(VacataireDTO dto) {
        Vacataire v = fromDTO(dto);
        if (v.getStatut() == null) v.setStatut(STATUT_A_CONTACTER);
        return toDTO(vacataireRepository.save(v));
    }

    /**
     * Update a contractor profile.
     * <p>
     * <b>If the statut changes to {@code VALIDE} and no user account exists yet,
     * a User account is automatically created</b> using the contractor's first
     * and last name. The generated username follows the pattern
     * {@code firstname.lastname} (lowercased, accent-free, unique).
     * A temporary password {@code ChangeMe123} is assigned; the user must
     * change it on first login.
     */
    public VacataireDTO update(Integer id, VacataireDTO dto) {
        Vacataire v = vacataireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contractor not found"));

        String previousStatut = v.getStatut();
        applyDTO(v, dto);

        // Auto-create user account when status transitions to VALIDE
        boolean becomesValidated = STATUT_VALIDE.equals(dto.getStatut())
                && !STATUT_VALIDE.equals(previousStatut);

        if (becomesValidated && v.getUser() == null) {
            autoCreateUserAccount(v);
            log.info("User account auto-created for contractor id={} ({} {})",
                    id, v.getPrenom(), v.getNom());
        }

        return toDTO(vacataireRepository.save(v));
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

    // ───────────────────────────────────────────────
    // Filtering
    // ───────────────────────────────────────────────

    /** Returns contractor profiles that already have a linked user account. */
    public List<VacataireDTO> getActive() {
        return vacataireRepository.findByUserIsNotNull().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /** Returns contractor profiles still in the recruitment pipeline (no account). */
    public List<VacataireDTO> getPending() {
        return vacataireRepository.findByUserIsNull().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ───────────────────────────────────────────────
    // Manual account conversion (still available as explicit action)
    // ───────────────────────────────────────────────

    /**
     * Manually converts a contractor profile into an active user account,
     * using a caller-supplied username and password.
     * Use this when you want to override the auto-generated credentials.
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
                .firstName(contractor.getPrenom())
                .lastName(contractor.getNom())
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .type("VACATAIRE")
                .build();
        userRepository.save(user);

        contractor.setUser(user);
        contractor.setStatut(STATUT_VALIDE);
        vacataireRepository.save(contractor);

        return toDTO(contractor);
    }

    // ───────────────────────────────────────────────
    // Private helpers
    // ───────────────────────────────────────────────

    /**
     * Automatically creates and links a User account for the given contractor.
     * Username follows the pattern {@code prenom.nom} (lowercased, accent-free,
     * guaranteed unique). A temporary password {@code ChangeMe123} is assigned.
     */
    private void autoCreateUserAccount(Vacataire contractor) {
        String base     = buildUsername(contractor.getPrenom(), contractor.getNom());
        String username = resolveUniqueUsername(base);

        User user = User.builder()
                .firstName(contractor.getPrenom())
                .lastName(contractor.getNom())
                .username(username)
                .password(passwordEncoder.encode("ChangeMe123"))
                .type("VACATAIRE")
                .build();

        userRepository.save(user);
        contractor.setUser(user);

        log.info("Auto-generated username '{}' for contractor '{} {}'",
                username, contractor.getPrenom(), contractor.getNom());
    }

    /**
     * Builds a candidate username from first and last name.
     * Examples: "Jean-Paul Dupont" → "jean-paul.dupont"
     *           "Élodie Lefèvre"   → "elodie.lefevre"
     */
    private String buildUsername(String firstName, String lastName) {
        String f = normalize(firstName != null ? firstName : "user");
        String l = normalize(lastName  != null ? lastName  : "unknown");
        return f + "." + l;
    }

    /**
     * Strips accents, lowercases and removes non-alphanumeric characters (except dash).
     */
    private String normalize(String input) {
        String nfd = Normalizer.normalize(input.trim().toLowerCase(), Normalizer.Form.NFD);
        return nfd.replaceAll("[^\\p{ASCII}]", "")
                  .replaceAll("[^a-z0-9\\-]", "");
    }

    /**
     * Ensures the username is unique by appending an incrementing suffix if needed.
     * e.g. "jean.dupont" → "jean.dupont2" → "jean.dupont3" …
     */
    private String resolveUniqueUsername(String base) {
        if (userRepository.findByUsernameIgnoreCase(base).isEmpty()) return base;
        int suffix = 2;
        while (true) {
            String candidate = base + suffix;
            if (userRepository.findByUsernameIgnoreCase(candidate).isEmpty()) return candidate;
            suffix++;
        }
    }

    // ───────────────────────────────────────────────
    // Mapping
    // ───────────────────────────────────────────────

    private VacataireDTO toDTO(Vacataire v) {
        return VacataireDTO.builder()
                .id(v.getId())
                .responsableRecrutement(v.getResponsableRecrutement())
                .prenom(v.getPrenom())
                .nom(v.getNom())
                .dateNaissance(v.getDateNaissance())
                .departement(v.getDepartement())
                .fonction(v.getFonction())
                .experience(v.getExperience())
                .profil(v.getProfil())
                .competences(v.getCompetences())
                .vueEnAmont(v.getVueEnAmont())
                .etablissement(v.getEtablissement())
                .site(v.getSite())
                .transmisResponsable(v.getTransmisResponsable())
                .signatureResponsable(v.getSignatureResponsable())
                .sourceConnaissance(v.getSourceConnaissance())
                .sourceConnaissanceAutre(v.getSourceConnaissanceAutre())
                .statut(v.getStatut())
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
        v.setResponsableRecrutement(dto.getResponsableRecrutement());
        v.setPrenom(dto.getPrenom());
        v.setNom(dto.getNom());
        v.setDateNaissance(dto.getDateNaissance());
        v.setDepartement(dto.getDepartement());
        v.setFonction(dto.getFonction());
        v.setExperience(dto.getExperience());
        v.setProfil(dto.getProfil());
        v.setCompetences(dto.getCompetences());
        v.setVueEnAmont(dto.getVueEnAmont());
        v.setEtablissement(dto.getEtablissement());
        v.setSite(dto.getSite());
        v.setTransmisResponsable(dto.getTransmisResponsable());
        v.setSignatureResponsable(dto.getSignatureResponsable());
        v.setSourceConnaissance(dto.getSourceConnaissance());
        v.setSourceConnaissanceAutre(dto.getSourceConnaissanceAutre());
        if (dto.getStatut() != null) v.setStatut(dto.getStatut());
        // userId and accountActive are read-only output fields — never applied back
    }
}
