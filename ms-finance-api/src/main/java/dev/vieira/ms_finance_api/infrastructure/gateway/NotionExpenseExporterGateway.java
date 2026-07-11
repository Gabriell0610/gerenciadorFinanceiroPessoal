package dev.vieira.ms_finance_api.infrastructure.gateway;

import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.gateway.ExpenseExporterGateway;
import dev.vieira.ms_finance_api.infrastructure.resources.client.NotionClient;
import dev.vieira.ms_finance_api.infrastructure.dto.NotionDto.NotionPageRequest;
import dev.vieira.ms_finance_api.infrastructure.dto.NotionDto.NotionPageRequest.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotionExpenseExporterGateway implements ExpenseExporterGateway {

    private final NotionClient notionClient;

    @Value("${notion.token}")
    private String token;

    @Value("${notion.database.id}")
    private String databaseId;

    @Override
    public void export(Expense expense) {
        // 1. Monta o corpo do payload respeitando o formato do Notion
        var parent = new Parent(databaseId);

        // Mapeia a coluna "Descrição" (Tipo Title no Notion)
        var titleProp = new TitleProp(List.of(new TextObj(new TextContent(expense.getDescription()))));

        // Mapeia a coluna "Valor" (Tipo Number no Notion)
        var numberProp = new NumberProp(expense.getAmount().doubleValue());

        // Mapeia a coluna "Data" (Tipo Date no Notion em formato ISO)
        String isoDate = expense.getDateExpense().format(DateTimeFormatter.ISO_DATE_TIME);
        var dateProp = new DateProp(new DateDetails(isoDate));

        var multiSelectProp = new MultiSelectProp(
                List.of(new MultiSelectDetails(expense.getCategory()))
        );

        // Junta tudo no mapa de propriedades (as chaves devem ser iguais às colunas do Notion)
        Map<String, Object> properties = Map.of(
                "Descrição", titleProp,
                "Valor", numberProp,
                "Data", dateProp,
                "Categoria", multiSelectProp
        );

        var request = new NotionPageRequest(parent, properties);

        // 2. Dispara via OpenFeign de forma extremamente limpa
        try {
            String authorizationHeader = "Bearer " + token;
            String notionVersion = "2022-06-28";

            notionClient.createPage(authorizationHeader, notionVersion, request);
            System.out.println("[Notion] Gasto espelhado com sucesso!");

        } catch (Exception e) {
            // Tratamento de erro (Se falhar aqui, o Postgres já salvou, seu dado está seguro!)
            System.err.println("[Notion] Falha ao exportar para o Notion: " + e.getMessage());
        }
    }
}
