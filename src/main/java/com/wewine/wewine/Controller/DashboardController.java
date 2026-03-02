package com.wewine.wewine.Controller;

import com.wewine.wewine.DTO.DashboardResponseDTO;
import com.wewine.wewine.Service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Endpoints para dashboard com métricas do sistema")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "Obter dados do dashboard",
               description = "Retorna todas as métricas para o dashboard: faturamento mensal, pedidos em aberto, garrafas vendidas, comissões a pagar, mix de produtos, vendas por cidade, pedidos recentes e estoque baixo")
    public ResponseEntity<DashboardResponseDTO> getDashboard() {
        DashboardResponseDTO dashboard = dashboardService.getDashboardData();
        return ResponseEntity.ok(dashboard);
    }
}

