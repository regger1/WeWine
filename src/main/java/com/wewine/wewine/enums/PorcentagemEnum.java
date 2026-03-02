package com.wewine.wewine.enums;

public enum PorcentagemEnum {
    // Adicionar as porcentagens necessárias manualmente
    PORCENTAGEM_5("5%", 5.0),
    PORCENTAGEM_10("10%", 10.0),
    PORCENTAGEM_15("15%", 15.0);

    private final String descricao;
    private final Double valor;

    PorcentagemEnum(String descricao, Double valor) {
        this.descricao = descricao;
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public Double getValor() {
        return valor;
    }
}

