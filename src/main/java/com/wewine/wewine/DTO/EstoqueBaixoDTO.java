package com.wewine.wewine.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstoqueBaixoDTO {
    private String vinho;
    private Integer safra;
    private Integer quantidade;
}

