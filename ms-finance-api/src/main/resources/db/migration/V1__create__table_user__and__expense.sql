CREATE TABLE users (
   id UUID PRIMARY KEY,
   name VARCHAR(255),
   email VARCHAR(255),
   telegram_id VARCHAR(255) NOT NULL,
   chat_id BIGINT NOT NULL,
   link_code VARCHAR(255),
   code_expires_at TIMESTAMP,
   created_at TIMESTAMP NOT NULL
);

CREATE TABLE expenses (
  id UUID PRIMARY KEY,
  user_id UUID NOT NULL,
  message_user VARCHAR(255) NOT NULL,
  amount DECIMAL(19, 2) NOT NULL, -- DECIMAL(19,2) é o padrão ideal para o BigDecimal de finanças
  description VARCHAR(255) NOT NULL,
  date_expense TIMESTAMP NOT NULL, -- Mudado para TIMESTAMP para casar com LocalDateTime
  created_at TIMESTAMP NOT NULL,

  CONSTRAINT fk_expenses_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);