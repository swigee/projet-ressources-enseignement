package sae.project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sae.project.dtos.vacataire.VacataireDTO;
import sae.project.model.Vacataire;
import sae.project.repositories.VacataireRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class VacataireService {

    @Autowired
    private VacataireRepository vacataireRepository;

    public List<VacataireDTO> getAll() {
        return vacataireRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public VacataireDTO getById(Integer id) {
        Vacataire v = vacataireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacataire introuvable"));
        return toDTO(v);
    }

    public VacataireDTO create(VacataireDTO dto) {
        Vacataire v = fromDTO(dto);
        if (v.getStatut() == null) v.setStatut("A_CONTACTER");
        return toDTO(vacataireRepository.save(v));
    }

    public VacataireDTO update(Integer id, VacataireDTO dto) {
        Vacataire v = vacataireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacataire introuvable"));
        applyDTO(v, dto);
        return toDTO(vacataireRepository.save(v));
    }

    public void delete(Integer id) {
        if (!vacataireRepository.existsById(id)) {
            throw new RuntimeException("Vacataire introuvable");
        }
        vacataireRepository.deleteById(id);
    }

    public List<VacataireDTO> search(String keyword) {
        return vacataireRepository.searchByName(keyword).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

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
    }
}
