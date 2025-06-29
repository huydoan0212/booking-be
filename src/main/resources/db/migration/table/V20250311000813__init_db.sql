-- Write your migration SQL here
ALTER ROLE booking WITH SUPERUSER;

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
