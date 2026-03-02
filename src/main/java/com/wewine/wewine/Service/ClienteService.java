// java
package com.wewine.wewine.Service;

import com.wewine.wewine.DTO.ClienteRequestDTO;
import com.wewine.wewine.DTO.ClienteResponseDTO;
import com.wewine.wewine.DTO.LocalizacaoDTO;
import com.wewine.wewine.DTO.RepresentanteResponseDTO;
import com.wewine.wewine.Entity.ClienteEntity;
import com.wewine.wewine.Entity.RepresentanteEntity;
import com.wewine.wewine.Exception.ResourceNotFoundException;
import com.wewine.wewine.Repository.ClienteRepository;
import com.wewine.wewine.Repository.RepresentanteRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final RepresentanteRepository representanteRepository;
    private final GeolocalizacaoService geolocalizacaoService;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository,
                         RepresentanteRepository representanteRepository,
                         GeolocalizacaoService geolocalizacaoService) {
        this.clienteRepository = clienteRepository;
        this.representanteRepository = representanteRepository;
        this.geolocalizacaoService = geolocalizacaoService;
    }

    /* Mapeamento DTO -> Entity */
    private ClienteEntity toEntity(ClienteRequestDTO dto, RepresentanteEntity representante) {
        ClienteEntity entity = new ClienteEntity();
        entity.setNome(dto.getNome());
        entity.setCpfCnpj(dto.getCpfCnpj());
        entity.setEndereco(dto.getEndereco());
        entity.setCidade(dto.getCidade());
        entity.setCep(dto.getCep());
        entity.setRepresentante(representante);
        entity.setEmail(dto.getEmail());
        entity.setTelefone(dto.getTelefone());
        entity.setFormasPagamento(dto.getFormasPagamento());
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
        return entity;
    }

    /* Mapeamento Entity -> DTO */
    private ClienteResponseDTO toResponseDTO(ClienteEntity entity) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setCpfCnpj(entity.getCpfCnpj());
        dto.setEndereco(entity.getEndereco());
        dto.setCidade(entity.getCidade());
        dto.setCep(entity.getCep());
        dto.setEmail(entity.getEmail());
        dto.setTelefone(entity.getTelefone());
        dto.setFormasPagamento(entity.getFormasPagamento());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());

        if (entity.getRepresentante() != null) {
            RepresentanteResponseDTO representanteDTO = new RepresentanteResponseDTO();
            representanteDTO.setId(entity.getRepresentante().getId());
            representanteDTO.setNome(entity.getRepresentante().getNome());
            representanteDTO.setCpfCnpj(entity.getRepresentante().getCpfCnpj());
            representanteDTO.setEmail(entity.getRepresentante().getEmail());
            representanteDTO.setCelularWhatsapp(entity.getRepresentante().getCelularWhatsapp());
            dto.setRepresentante(representanteDTO);
        }
        return dto;
    }

    /* CRUD */
    public ClienteResponseDTO createCliente(ClienteRequestDTO requestDTO, Long idRepresentante) {
        RepresentanteEntity representante = representanteRepository.findById(idRepresentante)
                .orElseThrow(() -> new ResourceNotFoundException("Representante com ID " + idRepresentante + " não encontrado."));
        ClienteEntity savedCliente = clienteRepository.save(toEntity(requestDTO, representante));
        return toResponseDTO(savedCliente);
    }

    public List<ClienteResponseDTO> findAllByRepresentanteId(Long idRepresentante) {
        List<ClienteEntity> entities = clienteRepository.findByRepresentanteId(idRepresentante);
        return entities.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public ClienteResponseDTO findById(Long id) {
        ClienteEntity entity = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com ID " + id + " não encontrado."));
        return toResponseDTO(entity);
    }

    public List<ClienteResponseDTO> findAll() {
        List<ClienteEntity> entities = clienteRepository.findAll();
        return entities.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public ClienteResponseDTO updateCliente(Long id, ClienteRequestDTO requestDTO, Long idRepresentante) {
        ClienteEntity entity = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com ID " + id + " não encontrado."));

        RepresentanteEntity representante = representanteRepository.findById(idRepresentante)
                .orElseThrow(() -> new ResourceNotFoundException("Representante com ID " + idRepresentante + " não encontrado."));

        entity.setNome(requestDTO.getNome());
        entity.setCpfCnpj(requestDTO.getCpfCnpj());
        entity.setEndereco(requestDTO.getEndereco());
        entity.setCidade(requestDTO.getCidade());
        entity.setCep(requestDTO.getCep());
        entity.setEmail(requestDTO.getEmail());
        entity.setTelefone(requestDTO.getTelefone());
        entity.setFormasPagamento(requestDTO.getFormasPagamento());
        entity.setLatitude(requestDTO.getLatitude());
        entity.setLongitude(requestDTO.getLongitude());
        entity.setRepresentante(representante);

        ClienteEntity updated = clienteRepository.save(entity);
        return toResponseDTO(updated);
    }

    public boolean deleteCliente(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Atualiza apenas a localização (latitude e longitude) de um cliente
     * Método útil para apps mobile que enviam coordenadas GPS
     */
    public ClienteResponseDTO atualizarLocalizacao(Long id, LocalizacaoDTO localizacao) {
        ClienteEntity entity = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com ID " + id + " não encontrado."));

        // Valida coordenadas
        if (!geolocalizacaoService.coordenadasValidas(localizacao.getLatitude(), localizacao.getLongitude())) {
            throw new IllegalArgumentException("Coordenadas inválidas");
        }

        entity.setLatitude(localizacao.getLatitude());
        entity.setLongitude(localizacao.getLongitude());

        ClienteEntity updated = clienteRepository.save(entity);
        return toResponseDTO(updated);
    }
}