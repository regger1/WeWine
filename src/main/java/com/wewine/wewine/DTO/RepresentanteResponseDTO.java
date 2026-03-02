    package com.wewine.wewine.DTO;

    import com.wewine.wewine.enums.*;
    import lombok.Data;

    import java.time.LocalDate;

    @Data
    public class RepresentanteResponseDTO {
        private Long id;
        private String nome;
        private String cpfCnpj;
        private String rgIe;
        private LocalDate nascimento;
        private String nomeFantasia;
        private String situacaoLegal;
        private StatusRepresentanteEnum status;
        private String email;
        private String celularWhatsapp;
        private String cep;
        private String endereco;
        private String numero;
        private String complemento;
        private String bairro;
        private String cidade;
        private String estado;
        private RegiaoAtuacaoEnum regiaoAtuacao;
        private PorcentagemEnum regraComissao;
        private String observacoes;
        private String banco;
        private String agencia;
        private String conta;
        private TipoContaEnum tipoConta;
        private AcessoAppEnum concederAcessoApp;
        private String loginAplicativo;
        private Double vendas;
        private Double comissao;
        private Integer clientesAtivos;
        private String[] cidades;
    }
