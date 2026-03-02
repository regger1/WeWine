package com.wewine.wewine.DTO;

import com.wewine.wewine.enums.TipoVinhoEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VinhoResponseDTO {
    private Long id;
    private String nome;
    private String safra;
    private TipoVinhoEnum tipo;
    private String regiao;
    private String urlImagem;
    private BigDecimal preco;
    private String notasDegustacao;
    private Integer estoque;
}
