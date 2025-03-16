CREATE TABLE public."cinemas" (
    "id" uuid NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    "name" character varying(255) NOT NULL,
    "slug" character varying(255) NOT NULL,
    "address" character varying(255) NOT NULL,
    "latitude" double precision NOT NULL,
    "longitude" double precision NOT NULL,
    "phone" character varying(255) NOT NULL,
    "image_landscape" text NOT NULL,
    "image_portrait" text NOT NULL,
    "sort_order" integer NOT NULL,
    "created_at" timestamp with time zone NOT NULL DEFAULT now(),
    "created_by" uuid NULL,
    "updated_at" timestamp with time zone NOT NULL DEFAULT now(),
    "updated_by" uuid NULL
);

ALTER TABLE "cinemas"
    ADD CONSTRAINT "fk_user_created" FOREIGN KEY ("created_by") REFERENCES "users" ("id");
ALTER TABLE "cinemas"
    ADD CONSTRAINT "fk_user_updated" FOREIGN KEY ("updated_by") REFERENCES "users" ("id");

CREATE TRIGGER update_modified_time BEFORE UPDATE ON public."cinemas"
FOR EACH ROW EXECUTE PROCEDURE update_modified_column();
