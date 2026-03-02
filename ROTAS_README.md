# Sistema de Localização e Rotas - WeWine API

## 📍 Funcionalidades de Geolocalização

Este módulo adiciona suporte completo para localização GPS e otimização de rotas para representantes visitarem seus clientes.

---

## 🚀 Endpoints Disponíveis

### 1. **Atualizar Localização do Cliente** (Mobile)
Atualiza apenas as coordenadas GPS de um cliente específico.

**Endpoint:** `PATCH /api/clientes/{id}/localizacao`

**Request Body:**
```json
{
  "latitude": -28.6775,
  "longitude": -49.3697
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "nome": "Cliente Exemplo",
  "latitude": -28.6775,
  "longitude": -49.3697,
  ...
}
```

**Uso no Mobile:**
```javascript
// Captura GPS do dispositivo
const position = await getCurrentPosition();

// Envia para o backend
await fetch(`/api/clientes/${clienteId}/localizacao`, {
  method: 'PATCH',
  body: JSON.stringify({
    latitude: position.coords.latitude,
    longitude: position.coords.longitude
  })
});
```

---

### 2. **Calcular Rota Otimizada**
Calcula a melhor rota para o representante visitar todos os seus clientes.

**Endpoint:** `GET /api/rotas/representante/{representanteId}/otimizada`

**Parâmetros:**
- `representanteId` (path): ID do representante
- `latitudeInicial` (query): Latitude inicial do representante (GPS)
- `longitudeInicial` (query): Longitude inicial do representante (GPS)

**Exemplo:**
```
GET /api/rotas/representante/1/otimizada?latitudeInicial=-28.6775&longitudeInicial=-49.3697
```

**Response:** `200 OK`
```json
{
  "representanteId": 1,
  "representanteNome": "João Silva",
  "distanciaTotalKm": 45.32,
  "totalClientes": 5,
  "pontoInicial": {
    "latitude": -28.6775,
    "longitude": -49.3697
  },
  "clientesOrdenados": [
    {
      "id": 3,
      "nome": "Cliente A",
      "endereco": "Rua X, 123",
      "cidade": "CRICIUMA",
      "telefone": "(48) 99999-0001",
      "latitude": -28.6785,
      "longitude": -49.3707,
      "distanciaEmKm": 1.2,
      "ordemVisita": 1
    },
    {
      "id": 7,
      "nome": "Cliente B",
      "endereco": "Rua Y, 456",
      "cidade": "FORQUILHINHA",
      "telefone": "(48) 99999-0002",
      "latitude": -28.7450,
      "longitude": -49.4720,
      "distanciaEmKm": 8.5,
      "ordemVisita": 2
    }
  ]
}
```

---

### 3. **Calcular Distância Total**
Calcula a distância total entre todos os clientes na ordem atual.

**Endpoint:** `GET /api/rotas/representante/{representanteId}/distancia-total`

**Response:** `200 OK`
```json
45.32
```

---

## 🧮 Algoritmo de Otimização

### Vizinho Mais Próximo (Nearest Neighbor)
O sistema usa o algoritmo do **Vizinho Mais Próximo** para calcular a rota:

1. Começa na posição inicial do representante (GPS atual)
2. Sempre escolhe o cliente mais próximo não visitado
3. Move para esse cliente e repete o processo
4. Continua até visitar todos os clientes

### Fórmula de Haversine
Para calcular distâncias entre coordenadas GPS, usamos a **Fórmula de Haversine**, que considera a curvatura da Terra:

```
a = sen²(Δlat/2) + cos(lat1) × cos(lat2) × sen²(Δlon/2)
c = 2 × atan2(√a, √(1−a))
distância = R × c
```

Onde R = 6371 km (raio médio da Terra)

**Precisão:** ±0.5% em distâncias curtas (até 100km)

---

## 📱 Fluxo de Integração Mobile

### 1. Cliente Cadastrado
```javascript
// Mobile envia localização ao cadastrar cliente
POST /api/clientes/representante/{repId}
{
  "nome": "Novo Cliente",
  "cpfCnpj": "12345678901",
  "cidade": "CRICIUMA",
  "latitude": -28.6775,
  "longitude": -49.3697,
  ...
}
```

### 2. Atualização Periódica (Opcional)
```javascript
// Mobile atualiza localização periodicamente
PATCH /api/clientes/{id}/localizacao
{
  "latitude": -28.6780,
  "longitude": -49.3700
}
```

### 3. Representante Solicita Rota
```javascript
// Pega GPS atual do representante
const position = await getCurrentPosition();

// Solicita rota otimizada
GET /api/rotas/representante/{repId}/otimizada
  ?latitudeInicial={position.latitude}
  &longitudeInicial={position.longitude}

// Backend retorna ordem de visita otimizada
```

---

## 🗺️ Exemplos de Uso

### Cenário 1: Representante quer saber a melhor ordem de visita
```bash
# Representante está em Criciúma centro
curl "http://localhost:8080/api/rotas/representante/1/otimizada?latitudeInicial=-28.6775&longitudeInicial=-49.3697"

# Retorna: visitar Cliente C (1.2km) → Cliente A (3.5km) → Cliente B (8.2km)
# Total: 12.9km
```

### Cenário 2: Mobile envia localização do cliente
```bash
# App mobile capturou GPS do cliente
curl -X PATCH http://localhost:8080/api/clientes/5/localizacao \
  -H "Content-Type: application/json" \
  -d '{"latitude": -28.7450, "longitude": -49.4720}'

# Retorna: cliente atualizado com nova localização
```

---

## ✅ Validações

### Coordenadas Válidas
- **Latitude:** -90 a 90
- **Longitude:** -180 a 180

### Tratamento de Erros
- Clientes sem coordenadas são **ignorados** no cálculo da rota
- Se nenhum cliente tiver coordenadas, retorna rota vazia com `totalClientes: 0`
- Coordenadas inválidas retornam `400 Bad Request`

---

## 🔧 Classes Criadas

### DTOs
- `LocalizacaoDTO`: Latitude e longitude
- `ClienteRotaDTO`: Cliente com ordem de visita e distância
- `RotaOtimizadaDTO`: Rota completa com todos os dados

### Services
- `GeolocalizacaoService`: Cálculos geográficos (Haversine)
- `RotaService`: Otimização de rotas (Vizinho Mais Próximo)

### Controllers
- `RotaController`: Endpoints de rotas
- `ClienteController`: Adiciona `PATCH /localizacao`

---

## 📊 Performance

### Complexidade do Algoritmo
- **Vizinho Mais Próximo:** O(n²)
- Para 100 clientes: ~10.000 comparações (instantâneo)
- Para 1.000 clientes: ~1.000.000 comparações (~1-2 segundos)

### Otimizações Futuras (se necessário)
- Algoritmo Genético para rotas grandes (>500 clientes)
- Cache de distâncias calculadas
- Integração com Google Maps API para rotas reais (considera ruas e trânsito)

---

## 🎯 Próximos Passos (Mobile)

1. **Capturar GPS no mobile** ao cadastrar cliente
2. **Enviar coordenadas** via `POST` ou `PATCH`
3. **Solicitar rota otimizada** antes de sair para visitas
4. **Exibir mapa** com a ordem de visita sugerida
5. **(Opcional)** Integrar com Google Maps/Waze para navegação

---

## 📝 Notas Técnicas

- Todas as distâncias são em **quilômetros (km)**
- Coordenadas são armazenadas como `Double` no banco
- Sistema funciona **offline** (não precisa de API externa)
- Ideal para áreas regionais (até ~200km de raio)

---

## 🚨 Limitações

1. **Não considera trânsito/ruas**: Calcula distância "em linha reta" (haversine)
2. **Algoritmo guloso**: Pode não ser a rota ÓTIMA em 100% dos casos
3. **Sem restrições de horário**: Não considera horário de funcionamento dos clientes

Para superar essas limitações em produção, considere integrar com:
- Google Maps Directions API
- GraphHopper API
- OpenRouteService

