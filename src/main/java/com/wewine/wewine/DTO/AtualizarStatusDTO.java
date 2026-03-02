package com.wewine.wewine.DTO;

import com.wewine.wewine.enums.StatusPedido;
import lombok.Data;

@Data
public class AtualizarStatusDTO {
    private StatusPedido status;
}
