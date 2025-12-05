package com.wewine.wewine.DTO;

import com.wewine.wewine.enums.CidadeEnum;
import com.wewine.wewine.enums.TipoVinhoEnum;
import lombok.Data;

@Data
public class RelatorioFiltroDTO {
    private Integer diasPeriodo; // 7, 30, 90
    private CidadeEnum cidade;
    private TipoVinhoEnum tipoVinho;
}

