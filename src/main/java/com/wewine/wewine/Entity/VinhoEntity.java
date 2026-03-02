package com.wewine.wewine.Entity;

import com.wewine.wewine.enums.TipoVinhoEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "vinhos")
public class VinhoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String regiao;

    private String urlImagem;

    private int anoSafra;

    private BigDecimal preco;

    @Enumerated(EnumType.STRING)
    private TipoVinhoEnum tipo;

    private String notasDegustacao;

    private int estoque;
}
