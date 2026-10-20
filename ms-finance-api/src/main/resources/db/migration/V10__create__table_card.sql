CREATE TABLE card (
   id UUID PRIMARY KEY,
   name VARCHAR(255),
   created_at TIMESTAMP NOT NULL,
   updated_at TIMESTAMP NOT NULL,
   invoice DECIMAL(19, 2) NOT NULL,
   user_id UUID NOT NULL,

   CONSTRAINT fk_card_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE

);

ALTER TABLE expenses
    ADD COLUMN card_id UUID;

ALTER TABLE expenses
    ADD CONSTRAINT fk_card_expense FOREIGN KEY (card_id) REFERENCES card(id) ON DELETE CASCADE;