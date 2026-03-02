package com.wewine.wewine.DTO;

import com.wewine.wewine.enums.FormaPagamentoEnum;
import com.wewine.wewine.enums.StatusPedido;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class PedidoResponseDTO {
    private Long codigoPedido;
    private ClienteResponseDTO cliente;
    private LocalDate data;
    private List<ItemPedidoResponseDTO> itens;
    private RepresentanteResponseDTO representante;
    private FormaPagamentoEnum pagamento;
    private BigDecimal total;
    private StatusPedido status;
}
