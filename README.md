# Elasticsearch Search API

Projeto exemplo com backend **Spring Boot** e frontend **React + Vite** para consultar documentos indexados no Elasticsearch.

O backend busca documentos no índice `wikipedia`, usando o campo `content` como campo pesquisável. A resposta exibida no frontend usa os campos `title`, `url` e `content`.

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

Verifique:

```bash
java -version
mvn -version
node --version
npm --version
docker --version
```

## 1. Subir o Elasticsearch

Na raiz do projeto:

```bash
docker compose up -d
```

Teste se o Elasticsearch está respondendo:

```bash
curl -k -u elastic:user123 https://localhost:9200
```

Credenciais usadas pelo projeto:

- Host: `https://localhost:9200`
- Usuário: `elastic`
- Senha: `user123`

## 2. Criar o índice `wikipedia`

Execute:

```bash
curl -k -u elastic:user123 -X PUT "https://localhost:9200/wikipedia" \
  -H "Content-Type: application/json" \
  -d '{
    "mappings": {
      "properties": {
        "title": { "type": "text" },
        "url": { "type": "keyword" },
        "content": { "type": "text" }
      }
    }
  }'
```

Se o índice já existir, esse comando pode retornar erro. Nesse caso, você pode continuar usando o índice existente.

## 3. Inserir documento de teste

```bash
curl -k -u elastic:user123 -X POST "https://localhost:9200/wikipedia/_doc" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Computador",
    "url": "https://example.com/computador",
    "content": "Computador é uma máquina eletrônica capaz de processar dados."
  }'
```

Force a atualização do índice para o documento aparecer imediatamente nas buscas:

```bash
curl -k -u elastic:user123 -X POST "https://localhost:9200/wikipedia/_refresh"
```

## 4. Rodar o backend

Na raiz do projeto:

```bash
mvn compile
mvn spring-boot:run
```

Se o Maven Wrapper estiver configurado corretamente, também é possível usar:

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
curl "http://localhost:8080/v1/search?query=computador&page=1"
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