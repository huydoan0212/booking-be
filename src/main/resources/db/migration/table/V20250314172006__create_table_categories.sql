CREATE TABLE public."categories"
(
    "id"          uuid                     NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    "name"        character varying(255)   NOT NULL,
    "description" text,
    "slug"        character varying(255)   NOT NULL,
    "created_at"  timestamp with time zone NOT NULL DEFAULT now(),
    "created_by"  uuid NULL,
    "updated_at"  timestamp with time zone NOT NULL DEFAULT now(),
    "updated_by"  uuid NULL
);
ALTER TABLE "categories"
    ADD CONSTRAINT "fk_user_created" FOREIGN KEY ("created_by") REFERENCES "users" ("id");
ALTER TABLE "categories"
    ADD CONSTRAINT "fk_user_updated" FOREIGN KEY ("updated_by") REFERENCES "users" ("id");
CREATE TRIGGER update_modified_time
    BEFORE UPDATE
    ON public."categories"
    FOR EACH ROW EXECUTE PROCEDURE update_modified_column();
