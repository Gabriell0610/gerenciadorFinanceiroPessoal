package dev.vieira.ms_finance_api.infrastructure.dto.NotionDto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record NotionPageRequest(
        Parent parent,
        Map<String, Object> properties
) {
    public record Parent(String type, @JsonProperty("data_source_id") String dataSourceId) {}

    // Auxiliares para montar o JSON
    public record TextContent(String content) {}
    public record TextObj(TextContent text) {}
    public record TitleProp(List<TextObj> title) {}
    public record NumberProp(Double number) {}
    public record DateDetails(String start) {}
    public record DateProp(DateDetails date) {}

    public record MultiSelectDetails(String name) {}

    public record MultiSelectProp(
            @JsonProperty("multi_select") List<MultiSelectDetails> multiSelect
    ) {}
}
