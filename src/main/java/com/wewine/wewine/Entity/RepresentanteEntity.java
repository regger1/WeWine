package com.wewine.wewine.Entity;

import com.wewine.wewine.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "representante")
public class RepresentanteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Column(unique = true, nullable = false)
    private String cpfCnpj;
    private String rgIe;
    private LocalDate nascimento;
    private String nomeFantasia;
    private String situacaoLegal;
    @Enumerated(EnumType.STRING)
    private StatusRepresentanteEnum status;
    @Column(unique = true, nullable = false)
    private String email;
    private String celularWhatsapp;
    private String cep;
    private String endereco;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    @Enumerated(EnumType.STRING)
    private RegiaoAtuacaoEnum regiaoAtuacao;
    @Enumerated(EnumType.STRING)
    private PorcentagemEnum regraComissao;
    @Column(columnDefinition = "TEXT")
    private String observacoes;
    private String banco;
    private String agencia;
    private String conta;
    @Enumerated(EnumType.STRING)
    private TipoContaEnum tipoConta;
    @Enumerated(EnumType.STRING)
    private AcessoAppEnum concederAcessoApp;
    private String loginAplicativo;
    @Column(nullable = false)
    private String senhaAcesso;
    private Double vendas;
    private Double comissao;
    private Integer clientesAtivos;
    @Column(columnDefinition = "TEXT")
    private String cidades;
}
