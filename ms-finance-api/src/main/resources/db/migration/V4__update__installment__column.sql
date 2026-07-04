UPDATE expenses SET installment = 1 WHERE installment IS NULL;

ALTER TABLE expenses ALTER COLUMN installment SET DEFAULT 1;

ALTER TABLE expenses ALTER COLUMN installment SET NOT NULL;