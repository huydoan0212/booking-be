CREATE TABLE public."seats" (
    "id" uuid NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    "seat_row" varchar(2) NOT NULL,
    "seat_column" integer NOT NULL,
    "type" varchar(25) NOT NULL,
    "status" varchar(25) NOT NULL,
    "cinema_hall_id" uuid NOT NULL,
    "created_at" timestamp with time zone NOT NULL DEFAULT now(),
    "created_by" uuid NULL,
    "updated_at" timestamp with time zone NOT NULL DEFAULT now(),
    "updated_by" uuid NULL
);
ALTER TABLE "seats"
    ADD CONSTRAINT "fk_user_created" FOREIGN KEY ("created_by") REFERENCES "users" ("id");
ALTER TABLE "seats"
    ADD CONSTRAINT "fk_user_updated" FOREIGN KEY ("updated_by") REFERENCES "users" ("id");
ALTER TABLE "seats"
    ADD CONSTRAINT "fk_cinema_hall_id" FOREIGN KEY ("cinema_hall_id") REFERENCES "cinema_halls" ("id");

CREATE TRIGGER update_modified_time BEFORE UPDATE ON public."seats"
FOR EACH ROW EXECUTE PROCEDURE update_modified_column();
