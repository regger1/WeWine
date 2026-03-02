    package com.wewine.wewine.Entity;

    import com.wewine.wewine.enums.FormaPagamentoEnum;
    import com.wewine.wewine.enums.StatusPedido;
    import jakarta.persistence.*;
    import lombok.*;

    import java.math.BigDecimal;
    import java.time.LocalDate;
    import java.util.ArrayList;
    import java.util.List;

    @Data
    @Entity
    @Table(name = "pedido")
    public class PedidoEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long codigoPedido;
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "cliente_id")
        private ClienteEntity cliente;
        private LocalDate data;
        @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<ItemPedidoEntity> itens = new ArrayList<>();
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "representante_id")
        private RepresentanteEntity representante;
        @Enumerated(EnumType.STRING)
        private FormaPagamentoEnum pagamento;
        @Column(precision = 10, scale = 2, nullable = false)
        private BigDecimal total;
        @Enumerated(EnumType.STRING)
        private StatusPedido status;
    }
