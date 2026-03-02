package com.wewine.wewine.DTO;

import com.wewine.wewine.enums.CidadeEnum;
import com.wewine.wewine.enums.FormaPagamentoEnum;
import lombok.Data;

import java.util.List;

@Data
public class ClienteResponseDTO {
    private Long id;
    private String nome;
    private String cpfCnpj;
    private String endereco;
    private CidadeEnum cidade;
    private String cep;
    private RepresentanteResponseDTO representante;
    private String email;
    private String telefone;
    private List<FormaPagamentoEnum> formasPagamento;
    private Double latitude;
    private Double longitude;
}
