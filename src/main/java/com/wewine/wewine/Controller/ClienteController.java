package com.wewine.wewine.Controller;

import com.wewine.wewine.DTO.ClienteRequestDTO;
import com.wewine.wewine.DTO.ClienteResponseDTO;
import com.wewine.wewine.DTO.LocalizacaoDTO;
import com.wewine.wewine.Service.ClienteService;
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
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Endpoints para gerenciamento de clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista com todos os clientes cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
    public ResponseEntity<List<ClienteResponseDTO>> findAll() {
        List<ClienteResponseDTO> lista = clienteService.findAll();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Retorna um cliente específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteResponseDTO> findById(@PathVariable Long id) {
        try {
            ClienteResponseDTO cliente = clienteService.findById(id);
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/representante/{idRepresentante}")
    @Operation(summary = "Listar clientes por representante", description = "Retorna todos os clientes de um representante específico")
    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
    public ResponseEntity<List<ClienteResponseDTO>> findByRepresentante(@PathVariable Long idRepresentante) {
        List<ClienteResponseDTO> lista = clienteService.findAllByRepresentanteId(idRepresentante);
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/representante/{idRepresentante}")
    @Operation(summary = "Criar novo cliente", description = "Cadastra um novo cliente vinculado a um representante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Representante não encontrado")
    })
    public ResponseEntity<ClienteResponseDTO> create(
            @PathVariable Long idRepresentante,
            @Valid @RequestBody ClienteRequestDTO request) {
        try {
            ClienteResponseDTO saved = clienteService.createCliente(request, idRepresentante);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente ou representante não encontrado")
    })
    public ResponseEntity<ClienteResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO request) {
        try {
            ClienteResponseDTO updated = clienteService.updateCliente(id, request, request.getRepresentanteId());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um cliente", description = "Remove um cliente do sistema pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = clienteService.deleteCliente(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/localizacao")
    @Operation(summary = "Atualizar localização do cliente",
               description = "Atualiza apenas as coordenadas GPS (latitude e longitude) de um cliente. Útil para apps mobile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Localização atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Coordenadas inválidas"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteResponseDTO> atualizarLocalizacao(
            @PathVariable Long id,
            @Valid @RequestBody LocalizacaoDTO localizacao) {
        try {
            ClienteResponseDTO updated = clienteService.atualizarLocalizacao(id, localizacao);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
