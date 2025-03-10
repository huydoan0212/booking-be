CREATE TABLE public."users"
(
    "id"          uuid                     NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    "name"        varchar(255)             NOT NULL,
    "username"    varchar(255)             NOT NULL UNIQUE,
    "password"    varchar(255)             NOT NULL,
    "dob"         date NULL DEFAULT NULL,
    "id_number"   varchar(12) NULL DEFAULT NULL unique,
    "gender"      smallint NULL DEFAULT 0,
    "role_id"     uuid                     not null,
    "user_status" varchar(50) NULL,
    "created_at"  timestamp with time zone NOT NULL DEFAULT now(),
    "created_by"  uuid NULL,
    "updated_at"  timestamp with time zone NOT NULL DEFAULT now(),
    "updated_by"  uuid NULL
);

CREATE TRIGGER update_modified_time
    BEFORE UPDATE
    ON "users"
    FOR EACH ROW EXECUTE PROCEDURE update_modified_column();

ALTER TABLE "users"
    ADD CONSTRAINT "fk_user_role" FOREIGN KEY ("role_id") REFERENCES "roles" ("id");
ALTER TABLE "users"
    ADD CONSTRAINT "fk_user_created" FOREIGN KEY ("created_by") REFERENCES "users" ("id");
ALTER TABLE "users"
    ADD CONSTRAINT "fk_user_updated" FOREIGN KEY ("updated_by") REFERENCES "users" ("id");
