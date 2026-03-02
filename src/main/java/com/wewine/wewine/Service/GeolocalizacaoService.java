package com.wewine.wewine.Service;

import org.springframework.stereotype.Service;

/**
 * Serviço para cálculos geográficos e de distância
 * Utiliza a Fórmula de Haversine para calcular distância entre coordenadas GPS
 */
@Service
public class GeolocalizacaoService {

    private static final double RAIO_TERRA_KM = 6371.0; // Raio médio da Terra em quilômetros

    /**
     * Calcula a distância entre duas coordenadas geográficas usando a Fórmula de Haversine
     *
     * @param lat1 Latitude do ponto 1
     * @param lon1 Longitude do ponto 1
     * @param lat2 Latitude do ponto 2
     * @param lon2 Longitude do ponto 2
     * @return Distância em quilômetros
     */
    public double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        // Converte graus para radianos
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        // Fórmula de Haversine
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                   Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                   Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distância em quilômetros
        return RAIO_TERRA_KM * c;
    }

    /**
     * Calcula a distância entre duas coordenadas geográficas
     * Retorna 0 se alguma coordenada for nula
     */
    public double calcularDistanciaSegura(Double lat1, Double lon1, Double lat2, Double lon2) {
        if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
            return 0.0;
        }
        return calcularDistancia(lat1, lon1, lat2, lon2);
    }

    /**
     * Valida se as coordenadas são válidas
     */
    public boolean coordenadasValidas(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            return false;
        }
        return latitude >= -90 && latitude <= 90 &&
               longitude >= -180 && longitude <= 180;
    }

    /**
     * Arredonda a distância para 2 casas decimais
     */
    public double arredondarDistancia(double distancia) {
        return Math.round(distancia * 100.0) / 100.0;
    }
}

