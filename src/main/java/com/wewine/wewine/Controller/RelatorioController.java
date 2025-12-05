package com.wewine.wewine.Controller;

import com.wewine.wewine.DTO.RelatorioFiltroDTO;
import com.wewine.wewine.DTO.RelatorioPerformanceDTO;
import com.wewine.wewine.DTO.RelatorioVendasDTO;
import com.wewine.wewine.Service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
@Tag(name = "Relatórios", description = "Endpoints para geração de relatórios")
@CrossOrigin(origins = "*")
public class RelatorioController {

    private final RelatorioService relatorioService;

    @Operation(summary = "Obter dados de vendas por período",
               description = "Retorna o total de vendas e número de pedidos filtrados por período e cidade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados obtidos com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/vendas-periodo")
    public ResponseEntity<RelatorioVendasDTO> getVendasPorPeriodo(@RequestBody RelatorioFiltroDTO filtro) {
        if (filtro.getDiasPeriodo() == null || filtro.getDiasPeriodo() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        RelatorioVendasDTO resultado = relatorioService.getVendasPorPeriodo(filtro);
        return ResponseEntity.ok(resultado);
    }

    @Operation(summary = "Gerar PDF de vendas por período",
               description = "Gera um PDF com o relatório de vendas filtrado por período e cidade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF gerado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro ao gerar PDF")
    })
    @PostMapping("/vendas-periodo/pdf")
    public ResponseEntity<byte[]> gerarPDFVendasPorPeriodo(@RequestBody RelatorioFiltroDTO filtro) {
        try {
            if (filtro.getDiasPeriodo() == null || filtro.getDiasPeriodo() <= 0) {
                return ResponseEntity.badRequest().build();
            }

            byte[] pdfBytes = relatorioService.gerarPDFVendasPorPeriodo(filtro);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "relatorio-vendas-periodo.pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obter dados de performance de produtos",
               description = "Retorna o número de pedidos filtrados por período, tipo de vinho e cidade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados obtidos com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/performance-produtos")
    public ResponseEntity<RelatorioPerformanceDTO> getPerformanceProdutos(@RequestBody RelatorioFiltroDTO filtro) {
        if (filtro.getDiasPeriodo() == null || filtro.getDiasPeriodo() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        RelatorioPerformanceDTO resultado = relatorioService.getPerformanceProdutos(filtro);
        return ResponseEntity.ok(resultado);
    }

    @Operation(summary = "Gerar PDF de performance de produtos",
               description = "Gera um PDF com o relatório de performance de produtos filtrado por período, tipo de vinho e cidade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF gerado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro ao gerar PDF")
    })
    @PostMapping("/performance-produtos/pdf")
    public ResponseEntity<byte[]> gerarPDFPerformanceProdutos(@RequestBody RelatorioFiltroDTO filtro) {
        try {
            if (filtro.getDiasPeriodo() == null || filtro.getDiasPeriodo() <= 0) {
                return ResponseEntity.badRequest().build();
            }

            byte[] pdfBytes = relatorioService.gerarPDFPerformanceProdutos(filtro);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "relatorio-performance-produtos.pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

