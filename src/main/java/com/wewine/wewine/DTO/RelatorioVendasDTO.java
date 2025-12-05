package com.wewine.wewine.DTO;

import com.wewine.wewine.enums.CidadeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioVendasDTO {
    private CidadeEnum cidade;
    private BigDecimal totalVendas;
    private Integer numeroPedidos;
    private Integer diasPeriodo;
}

