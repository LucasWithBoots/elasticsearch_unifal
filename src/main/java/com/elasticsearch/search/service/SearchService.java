package com.elasticsearch.search.service;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.elasticsearch.search.api.model.Result;
import com.elasticsearch.search.domain.EsClient;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final EsClient esClient;

    public SearchService(EsClient esClient) {
        this.esClient = esClient;
    }

    public List<Result> submitQuery(String query, Integer page) {
        var searchResponse = esClient.search(query, page);
        List<Hit<ObjectNode>> hits = searchResponse.hits().hits();

        var resultsList = hits.stream().map(h ->
                new Result()
                        .abs(treatContent(readField(h.source(), "content")))
                        .title(readField(h.source(), "title"))
                        .url(readField(h.source(), "url"))
        ).collect(Collectors.toList());

        return resultsList;
    }

    private String readField(ObjectNode source, String fieldName) {
        if (source == null || source.get(fieldName) == null || source.get(fieldName).isNull()) {
            return "";
        }

        return source.get(fieldName).asText("");
    }

    private String treatContent(String content) {
        content = content.replaceAll("</?(som|math)\\d*>", "");
        content = content.replaceAll("[^A-Za-z\\s]+", "");
        content = content.replaceAll("\\s+", " ");
        content = content.replaceAll("^\\s+", "");
        return content;
    }
}
