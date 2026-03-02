package com.wewine.wewine.Entity;

import com.wewine.wewine.enums.CidadeEnum;
import com.wewine.wewine.enums.FormaPagamentoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "cliente")
public class ClienteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Column(unique = true, nullable = false)
    private String cpfCnpj;
    private String endereco;
    @Enumerated(EnumType.STRING)
    private CidadeEnum cidade;
    private String cep;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "representante_id")
    private RepresentanteEntity representante;
    private String email;
    private String telefone;
    @ElementCollection(targetClass = FormaPagamentoEnum.class)
    @CollectionTable(name = "cliente_formas_pagamento", joinColumns = @JoinColumn(name = "cliente_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento")
    private List<FormaPagamentoEnum> formasPagamento;
    private Double latitude;
    private Double longitude;
}
