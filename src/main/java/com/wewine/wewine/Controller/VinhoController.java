package com.wewine.wewine.Controller;

import com.wewine.wewine.DTO.VinhoRequestDTO;
import com.wewine.wewine.DTO.VinhoResponseDTO;
import com.wewine.wewine.Service.VinhoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/vinhos")
@Tag(name = "Vinhos", description = "Endpoints para gerenciamento de vinhos")
public class VinhoController {

    private final VinhoService vinhoService;

    @Autowired
    public VinhoController(VinhoService vinhoService) {
        this.vinhoService = vinhoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os vinhos", description = "Retorna uma lista com todos os vinhos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de vinhos retornada com sucesso")
    public ResponseEntity<List<VinhoResponseDTO>> findAll() {
        List<VinhoResponseDTO> lista = vinhoService.findAll();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar vinho por ID", description = "Retorna um vinho específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vinho encontrado"),
            @ApiResponse(responseCode = "404", description = "Vinho não encontrado")
    })
    public ResponseEntity<VinhoResponseDTO> findById(@PathVariable Long id) {
        return vinhoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar novo vinho", description = "Cadastra um novo vinho no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vinho criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<VinhoResponseDTO> create(@Valid @RequestBody VinhoRequestDTO request) {
        VinhoResponseDTO saved = vinhoService.createVinho(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar vinho", description = "Atualiza os dados de um vinho existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vinho atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Vinho não encontrado")
    })
    public ResponseEntity<VinhoResponseDTO> update(@PathVariable Long id, @Valid @RequestBody VinhoRequestDTO request) {
        return vinhoService.updateVinho(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um vinho", description = "Remove um vinho do sistema pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vinho deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Vinho não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = vinhoService.deleteVinho(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}