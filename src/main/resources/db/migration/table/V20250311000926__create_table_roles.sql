-- Write your migration SQL here
CREATE TABLE public."roles"
(
    "id"             uuid                     NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    "role"           varchar(255)             NOT NULL UNIQUE,
    "role_reference" varchar(255),
    "description"    varchar(255) NULL DEFAULT NULL,
    "created_at"     timestamp with time zone NOT NULL DEFAULT now(),
    "updated_at"     timestamp with time zone NOT NULL DEFAULT now()
);

CREATE TRIGGER update_modified_time
    BEFORE UPDATE
    ON "roles"
    FOR EACH ROW EXECUTE PROCEDURE update_modified_column();
