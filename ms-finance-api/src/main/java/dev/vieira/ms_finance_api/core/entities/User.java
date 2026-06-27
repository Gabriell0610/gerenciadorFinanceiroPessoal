package dev.vieira.ms_finance_api.core.entities;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {

    private final UUID id;
    private String name;
    private final String email;
    private String linkCode;
    private LocalDateTime codeExpiresAt;
    private LocalDateTime created_at;
    private Long chatId;

    public User(String nome, String email, Long chatId) {
        this.id = UUID.randomUUID();
        this.name = nome;
        this.email = email;
        this.created_at = LocalDateTime.now();
        this.chatId = chatId;
    }

    public User(UUID id, Long chatId, String name, String email, String telegramId, String linkCode, LocalDateTime codeExpiresAt) {
        this.id = id;
        this.chatId = chatId;
        this.name = name;
        this.email = email;
        this.linkCode = linkCode;
        this.codeExpiresAt = codeExpiresAt;
    }

    // Regra de Negócio: Gerar código de 6 dígitos para o Telegram
    public void generateCode() {
        // Gera um número aleatório entre 100000 e 999999
        int numero = (int) (Math.random() * 900000) + 100000;
        this.linkCode = String.valueOf(numero);
        // O código expira em 15 minutos
        this.codeExpiresAt = LocalDateTime.now().plusMinutes(15);
    }

    public boolean linkTelegramUser(String code, Long telegramId) {
        if (this.linkCode != null
                && this.linkCode.equals(code)
                && LocalDateTime.now().isBefore(this.codeExpiresAt)) {

            this.chatId = telegramId;
            this.linkCode = null; // Invalida o código após o uso
            this.codeExpiresAt = null;
            return true;
        }
        return false;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLinkCode() {
        return linkCode;
    }

    public LocalDateTime getCodeExpiresAt() {
        return codeExpiresAt;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public Long getChatId() {
        return chatId;
    }
}
