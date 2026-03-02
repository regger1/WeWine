package com.wewine.wewine.Service;

import com.wewine.wewine.DTO.VinhoRequestDTO;
import com.wewine.wewine.DTO.VinhoResponseDTO;
import com.wewine.wewine.Entity.VinhoEntity;
import com.wewine.wewine.Repository.VinhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VinhoService {

    private final VinhoRepository vinhoRepository;

    @Autowired
    public VinhoService(VinhoRepository vinhoRepository) {
        this.vinhoRepository = vinhoRepository;
    }

    // ---------- CONVERSÃO DTO -> ENTITY ----------
    private VinhoEntity toEntity(VinhoRequestDTO dto) {
        VinhoEntity entity = new VinhoEntity();
        if (dto == null) {
            return entity;
        }

        entity.setNome(dto.getNome());
        entity.setAnoSafra(dto.getAnoSafra());
        entity.setRegiao(dto.getRegiao());
        entity.setUrlImagem(dto.getUrlImagem());
        entity.setPreco(dto.getPreco());
        entity.setTipo(dto.getTipo());
        entity.setNotasDegustacao(dto.getNotasDegustacao());

        // 👇 ESSENCIAL: adicionar estoque
        entity.setEstoque(dto.getEstoque() != null ? dto.getEstoque() : 0);

        return entity;
    }

    // ---------- CONVERSÃO ENTITY -> RESPONSE DTO ----------
    private VinhoResponseDTO toResponseDTO(VinhoEntity entity) {
        VinhoResponseDTO dto = new VinhoResponseDTO();
        if (entity == null) {
            return dto;
        }

        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setPreco(entity.getPreco());
        dto.setRegiao(entity.getRegiao());
        dto.setUrlImagem(entity.getUrlImagem());
        dto.setSafra(entity.getAnoSafra() > 0 ? Integer.toString(entity.getAnoSafra()) : null);
        dto.setTipo(entity.getTipo());
        dto.setNotasDegustacao(entity.getNotasDegustacao());

        // 👇 ESSENCIAL: devolver estoque ao front
        dto.setEstoque(entity.getEstoque());

        return dto;
    }

    // ---------- CREATE ----------
    public VinhoResponseDTO createVinho(VinhoRequestDTO requestDTO) {
        VinhoEntity vinhoToSave = toEntity(requestDTO);
        VinhoEntity savedVinho = vinhoRepository.save(vinhoToSave);
        return toResponseDTO(savedVinho);
    }

    // ---------- LISTAR ----------
    public List<VinhoResponseDTO> findAll() {
        List<VinhoEntity> entities = vinhoRepository.findAll();
        return entities.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ---------- BUSCAR POR ID ----------
    public Optional<VinhoResponseDTO> findById(Long id) {
        return vinhoRepository.findById(id)
                .map(this::toResponseDTO);
    }

    // ---------- UPDATE ----------
    public Optional<VinhoResponseDTO> updateVinho(Long id, VinhoRequestDTO requestDTO) {
        return vinhoRepository.findById(id)
                .map(existingVinho -> {

                    existingVinho.setNome(requestDTO.getNome());
                    existingVinho.setAnoSafra(requestDTO.getAnoSafra());
                    existingVinho.setRegiao(requestDTO.getRegiao());
                    existingVinho.setUrlImagem(requestDTO.getUrlImagem());
                    existingVinho.setPreco(requestDTO.getPreco());
                    existingVinho.setTipo(requestDTO.getTipo());
                    existingVinho.setNotasDegustacao(requestDTO.getNotasDegustacao());

                    // 👇 ESSENCIAL: atualizar estoque
                    existingVinho.setEstoque(
                            requestDTO.getEstoque() != null ? requestDTO.getEstoque() : existingVinho.getEstoque()
                    );

                    VinhoEntity updatedVinho = vinhoRepository.save(existingVinho);
                    return toResponseDTO(updatedVinho);
                });
    }

    // ---------- DELETE ----------
    public boolean deleteVinho(Long id) {
        if (vinhoRepository.existsById(id)) {
            vinhoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
