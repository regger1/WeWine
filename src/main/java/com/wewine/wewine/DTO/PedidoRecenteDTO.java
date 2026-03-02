package com.wewine.wewine.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoRecenteDTO {
    private Long id;
    private String cliente;
    private BigDecimal valorTotal; // CORRIGIDO
    private String status;
}
