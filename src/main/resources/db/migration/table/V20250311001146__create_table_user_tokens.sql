CREATE TABLE public."user_tokens"
(
    "id"         uuid                     NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    "token"      text                     NOT NULL,
    "expired_at" timestamp with time zone NOT NULL,
    "used"       boolean                  NOT NULL DEFAULT false,
    "user_id"    uuid                     NOT NULL,
    "created_at" timestamp with time zone NOT NULL DEFAULT now(),
    "updated_at" timestamp with time zone NOT NULL DEFAULT now()
);
ALTER TABLE "user_tokens"
    ADD CONSTRAINT "fk_user_tokens_user_id" FOREIGN KEY ("user_id") REFERENCES "users" ("id");

CREATE TRIGGER update_modified_time
    BEFORE UPDATE
    ON public."user_tokens"
    FOR EACH ROW EXECUTE PROCEDURE update_modified_column();
