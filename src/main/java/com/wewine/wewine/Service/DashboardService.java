package com.wewine.wewine.Service;

import com.wewine.wewine.DTO.*;
import com.wewine.wewine.Entity.ItemPedidoEntity;
import com.wewine.wewine.Entity.PedidoEntity;
import com.wewine.wewine.Entity.VinhoEntity;
import com.wewine.wewine.Repository.PedidoRepository;
import com.wewine.wewine.Repository.VinhoRepository;
import com.wewine.wewine.enums.StatusPedido;
import com.wewine.wewine.enums.TipoVinhoEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PedidoRepository pedidoRepository;
    private final VinhoRepository vinhoRepository;

    public DashboardResponseDTO getDashboardData() {
        DashboardResponseDTO dashboard = new DashboardResponseDTO();

        dashboard.setFaturamentoMensal(calcularFaturamentoMensal());
        dashboard.setPedidosEmAberto(contarPedidosEmAberto());
        dashboard.setGarrafasVendidas(contarGarrafasVendidas());
        dashboard.setComissoesAPagar(calcularComissoesAPagar());
        dashboard.setMixDeProdutos(calcularMixDeProdutos());
        dashboard.setVendasPorCidade(calcularVendasPorCidade());
        dashboard.setPedidosRecentes(buscarPedidosRecentes());
        dashboard.setEstoqueBaixo(buscarEstoqueBaixo());

        return dashboard;
    }

    private BigDecimal calcularFaturamentoMensal() {
        List<StatusPedido> statusFaturados = Arrays.asList(
                StatusPedido.FATURADO,
                StatusPedido.EMITIDO,
                StatusPedido.ENTREGUE
        );

        LocalDate inicioMes = YearMonth.now().atDay(1);
        LocalDate fimMes = YearMonth.now().atEndOfMonth();

        List<PedidoEntity> pedidos = pedidoRepository.findByStatusInAndDateBetween(
                statusFaturados, inicioMes, fimMes
        );

        return pedidos.stream()
                .map(PedidoEntity::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Integer contarPedidosEmAberto() {
        List<StatusPedido> statusExcluidos = Arrays.asList(
                StatusPedido.CANCELADO,
                StatusPedido.ENTREGUE,
                StatusPedido.EM_TRANSITO
        );

        List<PedidoEntity> pedidos = pedidoRepository.findByStatusNotIn(statusExcluidos);
        return pedidos.size();
    }

    private Integer contarGarrafasVendidas() {
        LocalDate inicioMes = YearMonth.now().atDay(1);
        LocalDate fimMes = YearMonth.now().atEndOfMonth();

        List<StatusPedido> statusConcluidos = Arrays.asList(
                StatusPedido.ENTREGUE,
                StatusPedido.FATURADO
        );

        List<PedidoEntity> pedidos = pedidoRepository.findByStatusInAndDateBetween(
                statusConcluidos, inicioMes, fimMes
        );

        return pedidos.stream()
                .flatMap(pedido -> pedido.getItens().stream())
                .mapToInt(ItemPedidoEntity::getQuantidade)
                .sum();
    }

    private BigDecimal calcularComissoesAPagar() {
        LocalDate inicioMes = YearMonth.now().atDay(1);
        LocalDate fimMes = YearMonth.now().atEndOfMonth();

        List<StatusPedido> statusFaturados = Arrays.asList(
                StatusPedido.FATURADO,
                StatusPedido.EMITIDO,
                StatusPedido.ENTREGUE
        );

        List<PedidoEntity> pedidos = pedidoRepository.findByStatusInAndDateBetween(
                statusFaturados, inicioMes, fimMes
        );

        return pedidos.stream()
                .filter(p -> p.getRepresentante() != null && p.getRepresentante().getRegraComissao() != null)
                .map(p -> {
                    BigDecimal total = p.getTotal();
                    Double percentual = p.getRepresentante().getRegraComissao().getValor();
                    return total.multiply(BigDecimal.valueOf(percentual / 100));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<MixProdutosDTO> calcularMixDeProdutos() {
        LocalDate inicioMes = YearMonth.now().atDay(1);
        LocalDate fimMes = YearMonth.now().atEndOfMonth();

        List<StatusPedido> statusFaturados = Arrays.asList(
                StatusPedido.FATURADO,
                StatusPedido.EMITIDO
        );

        List<PedidoEntity> pedidos = pedidoRepository.findByStatusInAndDateBetween(
                statusFaturados, inicioMes, fimMes
        );

        Map<TipoVinhoEnum, Integer> quantidadePorTipo = new HashMap<>();
        Map<TipoVinhoEnum, BigDecimal> valorPorTipo = new HashMap<>();

        pedidos.stream()
                .flatMap(pedido -> pedido.getItens().stream())
                .forEach(item -> {
                    TipoVinhoEnum tipo = item.getVinho().getTipo();
                    quantidadePorTipo.merge(tipo, item.getQuantidade(), Integer::sum);
                    valorPorTipo.merge(tipo, item.getSubtotalItem(), BigDecimal::add);
                });

        BigDecimal valorTotal = valorPorTipo.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return quantidadePorTipo.entrySet().stream()
                .map(entry -> {
                    TipoVinhoEnum tipo = entry.getKey();
                    Integer quantidade = entry.getValue();
                    BigDecimal valor = valorPorTipo.get(tipo);
                    Double porcentagem = valorTotal.compareTo(BigDecimal.ZERO) > 0
                            ? valor.divide(valorTotal, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .doubleValue()
                            : 0.0;

                    return new MixProdutosDTO(tipo.name(), quantidade, valor, porcentagem);
                })
                .sorted(Comparator.comparing(MixProdutosDTO::getValorTotal).reversed())
                .collect(Collectors.toList());
    }

    private List<VendasPorCidadeDTO> calcularVendasPorCidade() {
        LocalDate inicioMes = YearMonth.now().atDay(1);
        LocalDate fimMes = YearMonth.now().atEndOfMonth();

        List<StatusPedido> statusFaturados = Arrays.asList(
                StatusPedido.FATURADO,
                StatusPedido.EMITIDO
        );

        List<PedidoEntity> pedidos = pedidoRepository.findByStatusInAndDateBetween(
                statusFaturados, inicioMes, fimMes
        );

        Map<String, List<PedidoEntity>> pedidosPorCidade = pedidos.stream()
                .filter(p -> p.getCliente() != null && p.getCliente().getCidade() != null)
                .collect(Collectors.groupingBy(p -> p.getCliente().getCidade().name()));

        return pedidosPorCidade.entrySet().stream()
                .map(entry -> {
                    String cidade = entry.getKey();
                    List<PedidoEntity> pedidosCidade = entry.getValue();
                    Integer quantidade = pedidosCidade.size();
                    BigDecimal valorTotal = pedidosCidade.stream()
                            .map(PedidoEntity::getTotal)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return new VendasPorCidadeDTO(cidade, quantidade, valorTotal);
                })
                .sorted(Comparator.comparing(VendasPorCidadeDTO::getValorTotal).reversed())
                .collect(Collectors.toList());
    }

    private List<PedidoRecenteDTO> buscarPedidosRecentes() {
        List<PedidoEntity> pedidos = pedidoRepository.findTopNRecentOrders();

        return pedidos.stream()
                .limit(5)
                .map(p -> new PedidoRecenteDTO(
                        p.getCodigoPedido(),
                        p.getCliente() != null ? p.getCliente().getNome() : "Cliente não informado",
                        p.getTotal(),  // CORRIGIDO → agora preenche valorTotal
                        p.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    private List<EstoqueBaixoDTO> buscarEstoqueBaixo() {
        List<VinhoEntity> vinhos = vinhoRepository.findByEstoqueLessThan(50);

        return vinhos.stream()
                .map(v -> new EstoqueBaixoDTO(
                        v.getNome(),
                        v.getAnoSafra(),
                        v.getEstoque()
                ))
                .sorted(Comparator.comparing(EstoqueBaixoDTO::getQuantidade))
                .collect(Collectors.toList());
    }
}
