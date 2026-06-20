package dev.vieira.ms_finance_api.core.entities;

import java.util.UUID;

public class Category {

    private final UUID id;
    private String nameCategory;

    public Category(String category_name) {
        this.id = UUID.randomUUID();
        this.nameCategory = category_name;
    }

    public Category(UUID id, String category_name) {
        this.id = id;
        this.nameCategory = category_name;
    }

    public UUID getId() {
        return id;
    }

    public String getNameCategory() {
        return nameCategory;
    }
}
