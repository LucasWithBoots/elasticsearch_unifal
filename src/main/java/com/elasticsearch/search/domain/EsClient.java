package com.elasticsearch.search.domain;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EsClient {
    private ElasticsearchClient elasticsearchClient;

    public EsClient() {
        createConnection();
    }

    private void createConnection() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        String USER = "elastic";
        String PWD = "user123";
        credentialsProvider.setCredentials(AuthScope.ANY,
            new UsernamePasswordCredentials(USER, PWD));

        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http"))
            .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                .setDefaultCredentialsProvider(credentialsProvider))
            .build();

        ElasticsearchTransport transport = new RestClientTransport(
            restClient,
            new JacksonJsonpMapper()
        );

        elasticsearchClient = new co.elastic.clients.elasticsearch.ElasticsearchClient(transport);
    }

    public SearchResponse<ObjectNode> search(String query, Integer page) {
        int pageSize = 10;
        int safePage = page != null && page > 0 ? page : 1;
        int from = (safePage - 1) * pageSize;
        String safeQuery = query == null ? "" : query.trim();

        Query searchQuery = Query.of(q -> q.multiMatch(m -> m
            .query(safeQuery)
            .fields("title^3", "content")
            .type(TextQueryType.BestFields)
            .fuzziness("AUTO")
        ));

        SearchResponse<ObjectNode> response;
        try {
            response = elasticsearchClient.search(s -> s
                .index("wikipedia").from(from).size(pageSize)
                .query(searchQuery)
                .highlight(h -> h
                    .preTags("<mark>")
                    .postTags("</mark>")
                    .fields("title", f -> f.numberOfFragments(0))
                    .fields("content", f -> f.fragmentSize(180).numberOfFragments(2))
                ), ObjectNode.class
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response;
    }
}
