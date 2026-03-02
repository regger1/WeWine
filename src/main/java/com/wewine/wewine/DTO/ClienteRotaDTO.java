package com.wewine.wewine.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRotaDTO {
    private Long id;
    private String nome;
    private String endereco;
    private String cidade;
    private String telefone;
    private Double latitude;
    private Double longitude;
    private Double distanciaEmKm; // Distância do ponto anterior
    private Integer ordemVisita; // Ordem sugerida de visita
}

