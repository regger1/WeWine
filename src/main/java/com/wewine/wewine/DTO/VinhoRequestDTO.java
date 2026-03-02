package com.wewine.wewine.DTO;

import com.wewine.wewine.enums.TipoVinhoEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VinhoRequestDTO {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Ano da safra é obrigatório")
    @Positive(message = "Ano da safra deve ser positivo")
    private Integer anoSafra;

    private String regiao;
    private String urlImagem;

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    private BigDecimal preco;

    private TipoVinhoEnum tipo;
    private String notasDegustacao;

    @NotNull(message = "Estoque é obrigatório")
    @Positive(message = "Estoque deve ser maior que zero")
    private Integer estoque;
}
