package com.wewine.wewine.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalizacaoDTO {

    @NotNull(message = "Latitude é obrigatória")
    @Min(value = -90, message = "Latitude deve ser entre -90 e 90")
    @Max(value = 90, message = "Latitude deve ser entre -90 e 90")
    private Double latitude;

    @NotNull(message = "Longitude é obrigatória")
    @Min(value = -180, message = "Longitude deve ser entre -180 e 180")
    @Max(value = 180, message = "Longitude deve ser entre -180 e 180")
    private Double longitude;
}

