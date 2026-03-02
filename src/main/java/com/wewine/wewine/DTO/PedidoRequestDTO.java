package com.wewine.wewine.DTO;

import com.wewine.wewine.enums.FormaPagamentoEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
public class PedidoRequestDTO {

    @NotNull(message = "O ID do cliente é obrigatório.")
    private Long clienteId;

    @NotNull(message = "A data é obrigatória.")
    private LocalDate data;

    @NotEmpty(message = "O pedido deve conter pelo menos um item.")
    private List<ItemPedidoRequestDTO> itens;

    @NotNull(message = "O ID do representante é obrigatório.")
    private Long representanteId;

    @NotNull(message = "A forma de pagamento é obrigatória.")
    private FormaPagamentoEnum pagamento;
}
