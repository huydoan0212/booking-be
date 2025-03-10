-- .\script\createMigration.ps1 -desc "create_table_users" -type "table"
-- .\script\createMigration.ps1 -desc "init_data_users" -type "seed"


-- Write your migration SQL here
CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE
OR REPLACE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
NEW.updated_at
= now();
RETURN NEW;
END;
$$
language 'plpgsql';
