package com.wewine.wewine.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RotaOtimizadaDTO {
    private Long representanteId;
    private String representanteNome;
    private Double distanciaTotalKm;
    private Integer totalClientes;
    private List<ClienteRotaDTO> clientesOrdenados;
    private LocalizacaoDTO pontoInicial; // Localização inicial do representante
}

