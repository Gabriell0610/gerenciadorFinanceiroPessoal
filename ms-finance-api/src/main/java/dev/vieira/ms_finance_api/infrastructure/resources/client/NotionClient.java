package dev.vieira.ms_finance_api.infrastructure.resources.client;

import dev.vieira.ms_finance_api.infrastructure.dto.NotionDto.NotionPageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "notionClient", url = "${notion.api.url}")
public interface NotionClient {

    @PostMapping("/pages")
    void createPage(
            @RequestHeader("Authorization") String bearerToken,
            @RequestHeader("Notion-Version") String notionVersion,
            @RequestBody NotionPageRequest request
    );
}
