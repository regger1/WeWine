package com.wewine.wewine.DTO;

import com.wewine.wewine.enums.CidadeEnum;
import com.wewine.wewine.enums.TipoVinhoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioPerformanceDTO {
    private TipoVinhoEnum tipoVinho;
    private CidadeEnum cidade;
    private Integer numeroPedidos;
    private Integer diasPeriodo;
}

