package com.wewine.wewine.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemPedidoResponseDTO {
    private Long id;
    private Long idVinho;
    private String nomeVinho;
    private String urlImagemVinho;
    private Integer quantidade;
    private BigDecimal precoUnitarioVenda;
    private BigDecimal valorDescontoAplicado;
    private BigDecimal subtotalItem;
}
