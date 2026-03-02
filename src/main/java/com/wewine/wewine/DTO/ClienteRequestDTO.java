package com.wewine.wewine.DTO;

import com.wewine.wewine.enums.CidadeEnum;
import com.wewine.wewine.enums.FormaPagamentoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ClienteRequestDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private String cpfCnpj;

    private String endereco;

    @NotNull
    private CidadeEnum cidade;

    private String cep;

    @NotNull
    private Long representanteId;

    private String email;

    private String telefone;

    private List<FormaPagamentoEnum> formasPagamento;

    private Double latitude;

    private Double longitude;
}
