package com.wewine.wewine.enums;

public enum AcessoAppEnum {
    SEM_ACESSO("Sem Acesso"),
    ACESSO_LIBERADO("Acesso Liberado");

    private final String descricao;

    AcessoAppEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

