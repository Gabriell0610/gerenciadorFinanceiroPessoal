ALTER TABLE expenses
ALTER COLUMN date_expense TYPE DATE
USING date_expense::date;