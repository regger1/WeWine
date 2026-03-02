package com.wewine.wewine.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
public class ItemPedidoRequestDTO {
    @NotNull(message = "O ID do vinho é obrigatório.")
    private Long idVinho;
    @Min(value = 1, message = "A quantidade deve ser de pelo menos 1 unidade.")
    private Integer quantidade;
    private BigDecimal descontoItem;
}
