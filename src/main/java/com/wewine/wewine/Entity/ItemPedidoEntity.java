package com.wewine.wewine.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "itemPedido")
public class ItemPedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private PedidoEntity pedido;
    @ManyToOne
    @JoinColumn(name = "vinho_id", nullable = false)
    private VinhoEntity vinho;
    @Column(nullable = false)
    private Integer quantidade;
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precoUnitarioVenda;
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valorDescontoAplicado;
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotalItem;
}
