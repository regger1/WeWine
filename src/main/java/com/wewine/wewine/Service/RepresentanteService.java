package com.wewine.wewine.Service;

import com.wewine.wewine.DTO.RepresentanteRequestDTO;
import com.wewine.wewine.DTO.RepresentanteResponseDTO;
import com.wewine.wewine.Entity.RepresentanteEntity;
import com.wewine.wewine.Repository.RepresentanteRepository;
import com.wewine.wewine.Repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RepresentanteService {

    private final RepresentanteRepository representanteRepository;
    private final ClienteRepository clienteRepository;

    @Autowired
    public RepresentanteService(RepresentanteRepository representanteRepository,
                               ClienteRepository clienteRepository) {
        this.representanteRepository = representanteRepository;
        this.clienteRepository = clienteRepository;
    }

    // Mapeamento DTO -> Entity
    private RepresentanteEntity toEntity(RepresentanteRequestDTO dto) {
        RepresentanteEntity entity = new RepresentanteEntity();
        entity.setNome(dto.getNome());
        entity.setCpfCnpj(dto.getCpfCnpj());
        entity.setRgIe(dto.getRgIe());
        entity.setNascimento(dto.getNascimento());
        entity.setNomeFantasia(dto.getNomeFantasia());
        entity.setSituacaoLegal(dto.getSituacaoLegal());
        entity.setStatus(dto.getStatus());
        entity.setEmail(dto.getEmail());
        entity.setCelularWhatsapp(dto.getCelularWhatsapp());
        entity.setCep(dto.getCep());
        entity.setEndereco(dto.getEndereco());
        entity.setNumero(dto.getNumero());
        entity.setComplemento(dto.getComplemento());
        entity.setBairro(dto.getBairro());
        entity.setCidade(dto.getCidade());
        entity.setEstado(dto.getEstado());
        entity.setRegiaoAtuacao(dto.getRegiaoAtuacao());
        entity.setRegraComissao(dto.getRegraComissao());
        entity.setObservacoes(dto.getObservacoes());
        entity.setBanco(dto.getBanco());
        entity.setAgencia(dto.getAgencia());
        entity.setConta(dto.getConta());
        entity.setTipoConta(dto.getTipoConta());
        entity.setConcederAcessoApp(dto.getConcederAcessoApp());
        entity.setLoginAplicativo(dto.getLoginAplicativo());
        entity.setSenhaAcesso(dto.getSenhaAcesso());
        entity.setVendas(dto.getVendas());
        entity.setComissao(dto.getComissao());
        entity.setClientesAtivos(dto.getClientesAtivos());
        if (dto.getCidades() != null && dto.getCidades().length > 0) {
            entity.setCidades(String.join(",", dto.getCidades()));
        }
        return entity;
    }

    // Mapeamento Entity -> DTO
    private RepresentanteResponseDTO toResponseDTO(RepresentanteEntity entity) {
        RepresentanteResponseDTO dto = new RepresentanteResponseDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setCpfCnpj(entity.getCpfCnpj());
        dto.setRgIe(entity.getRgIe());
        dto.setNascimento(entity.getNascimento());
        dto.setNomeFantasia(entity.getNomeFantasia());
        dto.setSituacaoLegal(entity.getSituacaoLegal());
        dto.setStatus(entity.getStatus());
        dto.setEmail(entity.getEmail());
        dto.setCelularWhatsapp(entity.getCelularWhatsapp());
        dto.setCep(entity.getCep());
        dto.setEndereco(entity.getEndereco());
        dto.setNumero(entity.getNumero());
        dto.setComplemento(entity.getComplemento());
        dto.setBairro(entity.getBairro());
        dto.setCidade(entity.getCidade());
        dto.setEstado(entity.getEstado());
        dto.setRegiaoAtuacao(entity.getRegiaoAtuacao());
        dto.setRegraComissao(entity.getRegraComissao());
        dto.setObservacoes(entity.getObservacoes());
        dto.setBanco(entity.getBanco());
        dto.setAgencia(entity.getAgencia());
        dto.setConta(entity.getConta());
        dto.setTipoConta(entity.getTipoConta());
        dto.setConcederAcessoApp(entity.getConcederAcessoApp());
        dto.setLoginAplicativo(entity.getLoginAplicativo());
        dto.setVendas(entity.getVendas());
        dto.setComissao(entity.getComissao());
        dto.setClientesAtivos(entity.getClientesAtivos());
        if (entity.getCidades() != null && !entity.getCidades().isEmpty()) {
            dto.setCidades(entity.getCidades().split(","));
        }
        return dto;
    }

    public RepresentanteResponseDTO createRepresentante(RepresentanteRequestDTO requestDTO) {
        RepresentanteEntity entity = toEntity(requestDTO);
        RepresentanteEntity savedEntity = representanteRepository.save(entity);
        return toResponseDTO(savedEntity);
    }

    // Retorna todos os representantes
    public List<RepresentanteResponseDTO> findAll() {
        return representanteRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Busca por ID (retorna Optional para controle no controller)
    public Optional<RepresentanteResponseDTO> findById(Long id) {
        return representanteRepository.findById(id)
                .map(this::toResponseDTO);
    }

    // Atualiza um representante existente
    public RepresentanteResponseDTO updateRepresentante(Long id, RepresentanteRequestDTO requestDTO) {
        RepresentanteEntity entity = representanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Representante não encontrado com ID: " + id));

        entity.setNome(requestDTO.getNome());
        entity.setCpfCnpj(requestDTO.getCpfCnpj());
        entity.setRgIe(requestDTO.getRgIe());
        entity.setNascimento(requestDTO.getNascimento());
        entity.setNomeFantasia(requestDTO.getNomeFantasia());
        entity.setSituacaoLegal(requestDTO.getSituacaoLegal());
        entity.setStatus(requestDTO.getStatus());
        entity.setEmail(requestDTO.getEmail());
        entity.setCelularWhatsapp(requestDTO.getCelularWhatsapp());
        entity.setCep(requestDTO.getCep());
        entity.setEndereco(requestDTO.getEndereco());
        entity.setNumero(requestDTO.getNumero());
        entity.setComplemento(requestDTO.getComplemento());
        entity.setBairro(requestDTO.getBairro());
        entity.setCidade(requestDTO.getCidade());
        entity.setEstado(requestDTO.getEstado());
        entity.setRegiaoAtuacao(requestDTO.getRegiaoAtuacao());
        entity.setRegraComissao(requestDTO.getRegraComissao());
        entity.setObservacoes(requestDTO.getObservacoes());
        entity.setBanco(requestDTO.getBanco());
        entity.setAgencia(requestDTO.getAgencia());
        entity.setConta(requestDTO.getConta());
        entity.setTipoConta(requestDTO.getTipoConta());
        entity.setConcederAcessoApp(requestDTO.getConcederAcessoApp());
        entity.setLoginAplicativo(requestDTO.getLoginAplicativo());
        entity.setSenhaAcesso(requestDTO.getSenhaAcesso());
        entity.setVendas(requestDTO.getVendas());
        entity.setComissao(requestDTO.getComissao());
        entity.setClientesAtivos(requestDTO.getClientesAtivos());
        if (requestDTO.getCidades() != null && requestDTO.getCidades().length > 0) {
            entity.setCidades(String.join(",", requestDTO.getCidades()));
        }

        RepresentanteEntity updatedEntity = representanteRepository.save(entity);
        return toResponseDTO(updatedEntity);
    }

    // Soft delete - altera o status para INATIVO
    public void deleteRepresentante(Long id) {
        RepresentanteEntity entity = representanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Representante não encontrado com ID: " + id));

        entity.setStatus(com.wewine.wewine.enums.StatusRepresentanteEnum.INATIVO);
        representanteRepository.save(entity);
    }

    /**
     * Atualiza o contador de clientes ativos de um representante
     * Este método conta quantos clientes estão vinculados ao representante
     */
    public void atualizarClientesAtivos(Long representanteId) {
        RepresentanteEntity representante = representanteRepository.findById(representanteId)
                .orElseThrow(() -> new RuntimeException("Representante não encontrado com ID: " + representanteId));

        Long totalClientes = clienteRepository.countByRepresentanteId(representanteId);
        representante.setClientesAtivos(totalClientes.intValue());
        representanteRepository.save(representante);
    }
}