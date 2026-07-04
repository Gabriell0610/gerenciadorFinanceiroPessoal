CREATE TABLE report (
    id                  UUID PRIMARY KEY,
    user_id             UUID NOT NULL,
    competency          DATE NOT NULL,
    status              VARCHAR(10) NOT NULL DEFAULT 'OPEN',
    total_expenses      DECIMAL(10,2) NOT NULL,
    total_installments  DECIMAL(10,2) NOT NULL DEFAULT 0,
    grand_total         DECIMAL(10,2) NOT NULL,
    item_count          INT NOT NULL,
    generated_at        TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP NOT NULL,

    CONSTRAINT uq_report_user_competency UNIQUE (user_id, competency)
);