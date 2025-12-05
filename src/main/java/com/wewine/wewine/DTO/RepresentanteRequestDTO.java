    package com.wewine.wewine.DTO;

    import com.wewine.wewine.enums.*;
    import jakarta.validation.constraints.Email;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.NotNull;
    import jakarta.validation.constraints.Size;
    import lombok.Data;

    import java.time.LocalDate;

    @Data
    public class RepresentanteRequestDTO {

        @NotBlank
        private String nome;

        @NotBlank
        private String cpfCnpj;

        private String rgIe;

        private LocalDate nascimento;

        private String nomeFantasia;

        private String situacaoLegal;

        @NotNull
        private StatusRepresentanteEnum status;

        @NotBlank
        @Email
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

        @NotNull
        private AcessoAppEnum concederAcessoApp;

        private String loginAplicativo;

        @NotBlank
        @Size(min = 6)
        private String senhaAcesso;

        private Double vendas;

        private Double comissao;

        private Integer clientesAtivos;

        private String[] cidades;
    }