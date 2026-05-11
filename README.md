# Elasticsearch Search API

https://github.com/user-attachments/assets/ef8eb2cc-47aa-48ca-b324-ecc3890630ab

Projeto exemplo com backend **Spring Boot** e frontend **React + Vite** para consultar documentos indexados no Elasticsearch.

O backend busca documentos no índice `wikipedia`, usando os campos `title` e `content` como campos pesquisáveis. A resposta exibida no frontend usa os campos `title`, `url`, `content`, `score` e trechos destacados por highlight.

## Funcionalidades do Elasticsearch implementadas

- Busca em múltiplos campos com `multi_match`.
- Peso maior para o campo `title` com `title^3`.
- Busca tolerante a erro de digitação com `fuzziness: AUTO`.
- Highlight dos termos encontrados em `title` e `content`.
- Paginação com `from` e `size`.
- Exibição da pontuação de relevância (`_score`) retornada pelo Elasticsearch.

## Tecnologias

- Java 17
- Spring Boot 3.1.0
- Maven
- Elasticsearch 8.8.0
- React
- Vite
- Docker Compose

## Pré-requisitos

- Java 17
- Maven ou Maven Wrapper funcionando
- Node.js e npm
- Docker e Docker Compose

## 1. Subir o Elasticsearch

Na raiz do projeto:

```bash
docker compose up -d
```

Credenciais usadas pelo projeto:

- Host: `http://localhost:9200`
- Usuário: `elastic`
- Senha: `user123`

## 2. Criar o índice `wikipedia`

Execute:

```bash
curl -u elastic:user123 -X PUT "http://localhost:9200/wikipedia" \
  -H "Content-Type: application/json" \
  -d '{
    "settings": {
      "analysis": {
        "analyzer": {
          "pt_text": {
            "type": "standard",
            "stopwords": "_portuguese_"
          }
        }
      }
    },
    "mappings": {
      "properties": {
        "title": {
          "type": "text",
          "analyzer": "pt_text"
        },
        "url": { "type": "keyword" },
        "content": {
          "type": "text",
          "analyzer": "pt_text"
        }
      }
    }
  }'
```

Se o índice já existir, esse comando pode retornar erro. Nesse caso, você pode continuar usando o índice existente.

## 3. Inserir documento de teste

```bash
curl -u elastic:user123 -H "Content-Type: application/x-ndjson" \
  -X POST "http://localhost:9200/wikipedia/_bulk" \
  --data-binary "@./static/wiki.json"
```

Force a atualização do índice para o documento aparecer imediatamente nas buscas:

```bash
curl -u elastic:user123 -X POST "http://localhost:9200/wikipedia/_refresh"
```

## 4. Rodar o backend

Na raiz do projeto:

```bash
./mvnw compile
./mvnw spring-boot:run
```

O backend ficará disponível em:

```text
http://localhost:8080/v1
```

Teste a API:

```bash
curl "http://localhost:8080/v1/search?query=Illustration&page=1"
```

Teste a tolerância a erro de digitação buscando uma palavra escrita de forma incompleta ou errada:

```bash
curl "http://localhost:8080/v1/search?query=computdor&page=1"
```

A resposta inclui `score` e `highlights`:

```json
[
  {
    "title": "Computador",
    "url": "http://example.com/computador",
    "abs": "Computador e uma maquina eletronica capaz de processar dados",
    "score": 3.14,
    "highlights": [
      "<mark>Computador</mark> e uma maquina eletronica capaz de processar dados."
    ]
  }
]
```

## 5. Rodar o frontend

Em outro terminal:

```bash
cd frontend
npm install
npm run dev
```

Acesse:

```text
http://localhost:5173
```

O Vite está configurado para encaminhar chamadas de `/v1` para o backend em `http://localhost:8080`.
