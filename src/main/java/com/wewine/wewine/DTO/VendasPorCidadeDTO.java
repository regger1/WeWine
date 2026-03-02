package com.wewine.wewine.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendasPorCidadeDTO {
    private String cidade;
    private Integer quantidadePedidos;
    private BigDecimal valorTotal;
}

