package com.wewine.wewine.Controller;

import com.wewine.wewine.DTO.RotaOtimizadaDTO;
import com.wewine.wewine.Service.RotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rotas")
@Tag(name = "Rotas", description = "Endpoints para cálculo de rotas otimizadas")
public class RotaController {

    private final RotaService rotaService;

    @Autowired
    public RotaController(RotaService rotaService) {
        this.rotaService = rotaService;
    }

    @GetMapping("/representante/{representanteId}/otimizada")
    @Operation(
        summary = "Calcular rota otimizada para representante",
        description = "Calcula a melhor rota para visitar todos os clientes de um representante usando o algoritmo do Vizinho Mais Próximo. " +
                      "Requer a localização inicial (GPS) do representante. Retorna os clientes ordenados pela menor distância."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rota calculada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Representante não encontrado"),
            @ApiResponse(responseCode = "400", description = "Coordenadas inválidas")
    })
    public ResponseEntity<RotaOtimizadaDTO> calcularRotaOtimizada(
            @Parameter(description = "ID do representante", required = true)
            @PathVariable Long representanteId,

            @Parameter(description = "Latitude inicial do representante (GPS)", required = true, example = "-28.6775")
            @RequestParam Double latitudeInicial,

            @Parameter(description = "Longitude inicial do representante (GPS)", required = true, example = "-49.3697")
            @RequestParam Double longitudeInicial) {

        try {
            RotaOtimizadaDTO rota = rotaService.calcularRotaOtimizada(
                    representanteId, latitudeInicial, longitudeInicial);
            return ResponseEntity.ok(rota);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/representante/{representanteId}/distancia-total")
    @Operation(
        summary = "Calcular distância total entre clientes",
        description = "Calcula a distância total que o representante precisa percorrer para visitar todos os seus clientes na ordem atual"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Distância calculada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Representante não encontrado")
    })
    public ResponseEntity<Double> calcularDistanciaTotal(
            @Parameter(description = "ID do representante", required = true)
            @PathVariable Long representanteId) {

        try {
            double distancia = rotaService.calcularDistanciaTotalClientes(representanteId);
            return ResponseEntity.ok(distancia);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

