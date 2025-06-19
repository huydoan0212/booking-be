CREATE TABLE public."cinema_halls" (
    "id" uuid NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    "name" character varying(255) NOT NULL,
    "total_seats" integer NOT NULL,
    "screen_type" character varying(255) NOT NULL,
    "sound_system" character varying(255) NOT NULL,
    "cinema_id" uuid NOT NULL,
    "created_at" timestamp with time zone NOT NULL DEFAULT now(),
    "created_by" uuid NULL,
    "updated_at" timestamp with time zone NOT NULL DEFAULT now(),
    "updated_by" uuid NULL
);
ALTER TABLE "cinema_halls"
    ADD CONSTRAINT "fk_user_created" FOREIGN KEY ("created_by") REFERENCES "users" ("id");
ALTER TABLE "cinema_halls"
    ADD CONSTRAINT "fk_user_updated" FOREIGN KEY ("updated_by") REFERENCES "users" ("id");
ALTER TABLE "cinema_halls"
    ADD CONSTRAINT "fk_cinema_id" FOREIGN KEY ("cinema_id") REFERENCES "cinemas" ("id");

CREATE TRIGGER update_modified_time BEFORE UPDATE ON public."cinema_halls"
FOR EACH ROW EXECUTE PROCEDURE update_modified_column();
