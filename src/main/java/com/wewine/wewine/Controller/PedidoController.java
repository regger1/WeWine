package com.wewine.wewine.Controller;

import com.wewine.wewine.DTO.AtualizarStatusDTO;
import com.wewine.wewine.DTO.PedidoRequestDTO;
import com.wewine.wewine.DTO.PedidoResponseDTO;
import com.wewine.wewine.Service.PedidoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Endpoints para gerenciamento de pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    private Long getRepresentanteIdSimulado() {
        return 1L;
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> createPedido(@Valid @RequestBody PedidoRequestDTO request) {

        PedidoResponseDTO saved = pedidoService.createPedido(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getCodigoPedido())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping("/meusPedidos")
    public ResponseEntity<List<PedidoResponseDTO>> findByRepresentante() {
        Long representanteId = getRepresentanteIdSimulado();

        List<PedidoResponseDTO> lista = pedidoService.findByRepresentanteId(representanteId);

        if (lista.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(lista);
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> findAll() {
        List<PedidoResponseDTO> lista = pedidoService.findAll();

        if (lista.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> findById(@PathVariable Long id) {
        PedidoResponseDTO dto = pedidoService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Long id,
            @RequestBody AtualizarStatusDTO dto
    ) {
        pedidoService.atualizarStatus(id, dto.getStatus());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Long id) {
        pedidoService.deletePedido(id);
        return ResponseEntity.noContent().build();
    }
}
