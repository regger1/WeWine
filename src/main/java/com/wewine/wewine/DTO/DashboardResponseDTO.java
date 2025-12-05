package com.wewine.wewine.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponseDTO {
    private BigDecimal faturamentoMensal;
    private Integer pedidosEmAberto;
    private Integer garrafasVendidas;
    private BigDecimal comissoesAPagar;
    private List<MixProdutosDTO> mixDeProdutos;
    private List<VendasPorCidadeDTO> vendasPorCidade;
    private List<PedidoRecenteDTO> pedidosRecentes;
    private List<EstoqueBaixoDTO> estoqueBaixo;
}
