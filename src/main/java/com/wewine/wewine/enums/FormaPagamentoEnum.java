package com.wewine.wewine.enums;

public enum FormaPagamentoEnum {
    PIX("PIX"),
    BOLETO_15_DIAS("Boleto 15 dias"),
    BOLETO_30_DIAS("Boleto 30 dias"),
    CARTAO_CREDITO("Cartão de Crédito");

    private final String descricao;

    FormaPagamentoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
