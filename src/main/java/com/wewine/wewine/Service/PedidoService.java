package com.wewine.wewine.Service;

import com.wewine.wewine.DTO.ItemPedidoRequestDTO;
import com.wewine.wewine.DTO.ItemPedidoResponseDTO;
import com.wewine.wewine.DTO.PedidoRequestDTO;
import com.wewine.wewine.DTO.PedidoResponseDTO;
import com.wewine.wewine.Entity.*;
import com.wewine.wewine.Exception.ResourceNotFoundException;
import com.wewine.wewine.Repository.ClienteRepository;
import com.wewine.wewine.Repository.PedidoRepository;
import com.wewine.wewine.Repository.RepresentanteRepository;
import com.wewine.wewine.Repository.VinhoRepository;
import com.wewine.wewine.enums.StatusPedido;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final RepresentanteRepository representanteRepository;
    private final VinhoRepository vinhoRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository,
                         RepresentanteRepository representanteRepository, VinhoRepository vinhoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.representanteRepository = representanteRepository;
        this.vinhoRepository = vinhoRepository;
    }

    // -----------------------------------------------------------
    // LÓGICA DE NEGÓCIO: CRIAÇÃO DO PEDIDO (@Transactional)
    // -----------------------------------------------------------

    public List<PedidoResponseDTO> findByRepresentanteId(Long idRepresentante) {
        List<PedidoEntity> entities = pedidoRepository.findByRepresentanteId(idRepresentante);

        return entities.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public PedidoResponseDTO findById(Long id) {
        PedidoEntity entity = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido com ID " + id + " não encontrado"));

        return toResponseDTO(entity);
    }

    public List<PedidoResponseDTO> findAll() {
        return pedidoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void atualizarStatus(Long id, StatusPedido novoStatus) {

        PedidoEntity pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado: " + id));

        pedido.setStatus(novoStatus);

        pedidoRepository.save(pedido);
    }

    @Transactional
    public void deletePedido(Long id) {
        PedidoEntity pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido com ID " + id + " não encontrado"));

        pedidoRepository.delete(pedido);
    }

    @Transactional // Garante que se um item falhar, o pedido inteiro será desfeito (rollback).
    public PedidoResponseDTO createPedido(PedidoRequestDTO requestDTO) {

        // 1. Validação dos Vínculos Essenciais
        ClienteEntity cliente = clienteRepository.findById(requestDTO.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));

        RepresentanteEntity representante = representanteRepository.findById(requestDTO.getRepresentanteId())
                .orElseThrow(() -> new ResourceNotFoundException("Representante não encontrado."));

        // 2. Mapeamento e Processamento dos Itens
        List<ItemPedidoEntity> itensEntity = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoRequestDTO itemDto : requestDTO.getItens()) {
            VinhoEntity vinho = vinhoRepository.findById(itemDto.getIdVinho())
                    .orElseThrow(() -> new ResourceNotFoundException("Vinho com ID " + itemDto.getIdVinho() + " não encontrado."));

            // Cria o ItemPedidoEntity
            ItemPedidoEntity itemEntity = processItem(itemDto, vinho);

            itensEntity.add(itemEntity);
            total = total.add(itemEntity.getSubtotalItem());
        }

        // 3. Criação do Pedido Principal (Cabeçalho)
        PedidoEntity pedido = new PedidoEntity();

        // Configurações do pedido
        pedido.setCliente(cliente);
        pedido.setRepresentante(representante);
        pedido.setData(requestDTO.getData());
        pedido.setPagamento(requestDTO.getPagamento());
        pedido.setStatus(StatusPedido.EMITIDO); // Status inicial
        pedido.setTotal(total);

        // Vínculo dos itens com o pedido principal
        pedido.setItens(itensEntity);
        itensEntity.forEach(item -> item.setPedido(pedido));

        // 4. Persistência
        PedidoEntity savedPedido = pedidoRepository.save(pedido);

        // 5. Retorno
        return toResponseDTO(savedPedido);
    }

    // -----------------------------------------------------------
    // MÉTODOS AUXILIARES E PRIVADOS
    // -----------------------------------------------------------

    private ItemPedidoEntity processItem(ItemPedidoRequestDTO itemDto, VinhoEntity vinho) {
        ItemPedidoEntity itemEntity = new ItemPedidoEntity();

        // **Preço de Venda** (É o preço do vinho no momento da venda)
        BigDecimal precoUnitario = vinho.getPreco(); // Assume que o preço está na VinhoEntity

        // **Cálculos**
        BigDecimal quantidade = new BigDecimal(itemDto.getQuantidade());
        BigDecimal subtotalBruto = precoUnitario.multiply(quantidade);

        // Desconto (se não for nulo no DTO)
        BigDecimal descontoItem = itemDto.getDescontoItem() != null ? itemDto.getDescontoItem() : BigDecimal.ZERO;

        // Subtotal final do item
        BigDecimal subtotalFinal = subtotalBruto.subtract(descontoItem)
                .setScale(2, RoundingMode.HALF_UP); // Garantir precisão de 2 casas decimais

        // Mapeamento para a Entity
        itemEntity.setVinho(vinho);
        itemEntity.setQuantidade(itemDto.getQuantidade());
        itemEntity.setPrecoUnitarioVenda(precoUnitario);
        itemEntity.setValorDescontoAplicado(descontoItem);
        itemEntity.setSubtotalItem(subtotalFinal);

        return itemEntity;
    }

    // Mapeamento Entity -> DTO (Complexo por causa dos itens)
    private PedidoResponseDTO toResponseDTO(PedidoEntity entity) {
        PedidoResponseDTO dto = new PedidoResponseDTO();

        dto.setCodigoPedido(entity.getCodigoPedido());
        dto.setData(entity.getData());
        dto.setPagamento(entity.getPagamento());
        dto.setStatus(entity.getStatus());
        dto.setTotal(entity.getTotal());

        // Mapeamento do Cliente
        if (entity.getCliente() != null) {
            dto.setCliente(toClienteResponseDTO(entity.getCliente()));
        }

        // Mapeamento do Representante
        if (entity.getRepresentante() != null) {
            dto.setRepresentante(toRepresentanteResponseDTO(entity.getRepresentante()));
        }

        // Mapeamento dos Itens
        List<ItemPedidoResponseDTO> itensDTO = entity.getItens().stream()
                .map(this::toItemResponseDTO)
                .collect(Collectors.toList());
        dto.setItens(itensDTO);

        return dto;
    }

    // Mapeamento de ClienteEntity -> ClienteResponseDTO
    private com.wewine.wewine.DTO.ClienteResponseDTO toClienteResponseDTO(ClienteEntity entity) {
        com.wewine.wewine.DTO.ClienteResponseDTO dto = new com.wewine.wewine.DTO.ClienteResponseDTO();
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
            dto.setRepresentante(toRepresentanteResponseDTO(entity.getRepresentante()));
        }

        return dto;
    }

    // Mapeamento de RepresentanteEntity -> RepresentanteResponseDTO
    private com.wewine.wewine.DTO.RepresentanteResponseDTO toRepresentanteResponseDTO(RepresentanteEntity entity) {
        com.wewine.wewine.DTO.RepresentanteResponseDTO dto = new com.wewine.wewine.DTO.RepresentanteResponseDTO();
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
        return dto;
    }

    // Mapeamento de ItemPedidoEntity -> ItemPedidoResponseDTO
    private ItemPedidoResponseDTO toItemResponseDTO(ItemPedidoEntity itemEntity) {
        ItemPedidoResponseDTO dto = new ItemPedidoResponseDTO();
        dto.setId(itemEntity.getId());

        // Dados do Vinho
        dto.setIdVinho(itemEntity.getVinho().getId());
        dto.setNomeVinho(itemEntity.getVinho().getNome());
        dto.setUrlImagemVinho(itemEntity.getVinho().getUrlImagem());

        // Dados da Venda
        dto.setQuantidade(itemEntity.getQuantidade());
        dto.setPrecoUnitarioVenda(itemEntity.getPrecoUnitarioVenda());
        dto.setValorDescontoAplicado(itemEntity.getValorDescontoAplicado());
        dto.setSubtotalItem(itemEntity.getSubtotalItem());

        return dto;
    }
}
