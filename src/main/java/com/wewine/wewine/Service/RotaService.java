package com.wewine.wewine.Service;

import com.wewine.wewine.DTO.ClienteRotaDTO;
import com.wewine.wewine.DTO.LocalizacaoDTO;
import com.wewine.wewine.DTO.RotaOtimizadaDTO;
import com.wewine.wewine.Entity.ClienteEntity;
import com.wewine.wewine.Entity.RepresentanteEntity;
import com.wewine.wewine.Repository.ClienteRepository;
import com.wewine.wewine.Repository.RepresentanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Serviço para cálculo de rotas otimizadas usando algoritmo do Vizinho Mais Próximo
 */
@Service
public class RotaService {

    private final ClienteRepository clienteRepository;
    private final RepresentanteRepository representanteRepository;
    private final GeolocalizacaoService geoService;

    @Autowired
    public RotaService(ClienteRepository clienteRepository,
                      RepresentanteRepository representanteRepository,
                      GeolocalizacaoService geoService) {
        this.clienteRepository = clienteRepository;
        this.representanteRepository = representanteRepository;
        this.geoService = geoService;
    }

    /**
     * Calcula a rota otimizada para um representante visitando seus clientes
     * Usa o algoritmo do Vizinho Mais Próximo (Nearest Neighbor)
     *
     * @param representanteId ID do representante
     * @param latInicial Latitude inicial do representante
     * @param lonInicial Longitude inicial do representante
     * @return Rota otimizada com clientes ordenados
     */
    public RotaOtimizadaDTO calcularRotaOtimizada(Long representanteId, Double latInicial, Double lonInicial) {
        RepresentanteEntity representante = representanteRepository.findById(representanteId)
                .orElseThrow(() -> new RuntimeException("Representante não encontrado"));

        // Busca todos os clientes do representante
        List<ClienteEntity> clientes = clienteRepository.findByRepresentanteId(representanteId);

        // Filtra apenas clientes com localização válida
        List<ClienteEntity> clientesComLocalizacao = clientes.stream()
                .filter(c -> geoService.coordenadasValidas(c.getLatitude(), c.getLongitude()))
                .toList();

        if (clientesComLocalizacao.isEmpty()) {
            return criarRotaVazia(representante, latInicial, lonInicial);
        }

        // Algoritmo do Vizinho Mais Próximo
        List<ClienteRotaDTO> rotaOtimizada = calcularVizinhoMaisProximo(
                clientesComLocalizacao, latInicial, lonInicial);

        // Calcula distância total
        double distanciaTotal = rotaOtimizada.stream()
                .mapToDouble(ClienteRotaDTO::getDistanciaEmKm)
                .sum();

        RotaOtimizadaDTO resultado = new RotaOtimizadaDTO();
        resultado.setRepresentanteId(representante.getId());
        resultado.setRepresentanteNome(representante.getNome());
        resultado.setDistanciaTotalKm(geoService.arredondarDistancia(distanciaTotal));
        resultado.setTotalClientes(rotaOtimizada.size());
        resultado.setClientesOrdenados(rotaOtimizada);
        resultado.setPontoInicial(new LocalizacaoDTO(latInicial, lonInicial));

        return resultado;
    }

    /**
     * Implementação do algoritmo do Vizinho Mais Próximo
     * Sempre escolhe o cliente mais próximo do ponto atual
     */
    private List<ClienteRotaDTO> calcularVizinhoMaisProximo(
            List<ClienteEntity> clientes, Double latAtual, Double lonAtual) {

        List<ClienteRotaDTO> rota = new ArrayList<>();
        Set<Long> visitados = new HashSet<>();

        double latCorrente = latAtual;
        double lonCorrente = lonAtual;
        int ordem = 1;

        while (visitados.size() < clientes.size()) {
            ClienteEntity maisProximo = null;
            double menorDistancia = Double.MAX_VALUE;

            // Encontra o cliente mais próximo não visitado
            for (ClienteEntity cliente : clientes) {
                if (!visitados.contains(cliente.getId())) {
                    double distancia = geoService.calcularDistancia(
                            latCorrente, lonCorrente,
                            cliente.getLatitude(), cliente.getLongitude());

                    if (distancia < menorDistancia) {
                        menorDistancia = distancia;
                        maisProximo = cliente;
                    }
                }
            }

            if (maisProximo != null) {
                // Adiciona à rota
                ClienteRotaDTO clienteRota = new ClienteRotaDTO();
                clienteRota.setId(maisProximo.getId());
                clienteRota.setNome(maisProximo.getNome());
                clienteRota.setEndereco(maisProximo.getEndereco());
                clienteRota.setCidade(maisProximo.getCidade() != null ?
                        maisProximo.getCidade().name() : null);
                clienteRota.setTelefone(maisProximo.getTelefone());
                clienteRota.setLatitude(maisProximo.getLatitude());
                clienteRota.setLongitude(maisProximo.getLongitude());
                clienteRota.setDistanciaEmKm(geoService.arredondarDistancia(menorDistancia));
                clienteRota.setOrdemVisita(ordem++);

                rota.add(clienteRota);
                visitados.add(maisProximo.getId());

                // Atualiza posição corrente
                latCorrente = maisProximo.getLatitude();
                lonCorrente = maisProximo.getLongitude();
            }
        }

        return rota;
    }

    /**
     * Cria uma rota vazia quando não há clientes com localização
     */
    private RotaOtimizadaDTO criarRotaVazia(RepresentanteEntity representante,
                                            Double latInicial, Double lonInicial) {
        RotaOtimizadaDTO rota = new RotaOtimizadaDTO();
        rota.setRepresentanteId(representante.getId());
        rota.setRepresentanteNome(representante.getNome());
        rota.setDistanciaTotalKm(0.0);
        rota.setTotalClientes(0);
        rota.setClientesOrdenados(new ArrayList<>());
        rota.setPontoInicial(new LocalizacaoDTO(latInicial, lonInicial));
        return rota;
    }

    /**
     * Calcula distância total entre todos os clientes de um representante
     */
    public double calcularDistanciaTotalClientes(Long representanteId) {
        List<ClienteEntity> clientes = clienteRepository.findByRepresentanteId(representanteId);

        double distanciaTotal = 0.0;
        for (int i = 0; i < clientes.size() - 1; i++) {
            ClienteEntity c1 = clientes.get(i);
            ClienteEntity c2 = clientes.get(i + 1);

            distanciaTotal += geoService.calcularDistanciaSegura(
                    c1.getLatitude(), c1.getLongitude(),
                    c2.getLatitude(), c2.getLongitude());
        }

        return geoService.arredondarDistancia(distanciaTotal);
    }
}

