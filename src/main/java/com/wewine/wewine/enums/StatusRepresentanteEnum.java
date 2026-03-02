package com.wewine.wewine.enums;

public enum StatusRepresentanteEnum {
    ATIVO("Ativo"),
    INATIVO("Inativo");

    private final String descricao;

    StatusRepresentanteEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

