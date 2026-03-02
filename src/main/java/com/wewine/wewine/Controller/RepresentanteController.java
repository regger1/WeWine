package com.wewine.wewine.Controller;

import com.wewine.wewine.DTO.RepresentanteRequestDTO;
import com.wewine.wewine.DTO.RepresentanteResponseDTO;
import com.wewine.wewine.Service.RepresentanteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/representantes")
@Tag(name = "Representantes", description = "Endpoints para gerenciamento de representantes")
public class RepresentanteController {
    private final RepresentanteService representanteService;

    public RepresentanteController(RepresentanteService representanteService) {
        this.representanteService = representanteService;
    }

    @PostMapping
    public ResponseEntity<RepresentanteResponseDTO> create(@Valid @RequestBody RepresentanteRequestDTO request) {

        RepresentanteResponseDTO saved = representanteService.createRepresentante(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<RepresentanteResponseDTO>> findAll() {
        List<RepresentanteResponseDTO> lista = representanteService.findAll();

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepresentanteResponseDTO> findById(@PathVariable Long id) {
        return representanteService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepresentanteResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody RepresentanteRequestDTO request) {
        try {
            RepresentanteResponseDTO updated = representanteService.updateRepresentante(id, request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            representanteService.deleteRepresentante(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/atualizar-clientes-ativos")
    public ResponseEntity<RepresentanteResponseDTO> atualizarClientesAtivos(@PathVariable Long id) {
        try {
            representanteService.atualizarClientesAtivos(id);
            return representanteService.findById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
