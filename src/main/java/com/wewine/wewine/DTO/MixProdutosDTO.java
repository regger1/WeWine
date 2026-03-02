package com.wewine.wewine.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MixProdutosDTO {
    private String tipo;
    private Integer quantidade;
    private BigDecimal valorTotal;
    private Double porcentagem;
}

