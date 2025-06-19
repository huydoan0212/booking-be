CREATE TABLE public."movies"
(
    "id"              uuid                     NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    "name"            character varying(255)   NOT NULL,
    "age"             integer                  NOT NULL,
    "duration"        integer                  NOT NULL,
    "image_landscape" text                     NOT NULL,
    "image_portrait"  text                     NOT NULL,
    "slug"            character varying(255)   NOT NULL,
    "rate"            NUMERIC(3, 1),
    "total_votes"     integer NULL,
    "views"           integer NULL,
    "description"     text NULL,
    "sort_order"      integer NULL,
    "actors"          text NULL,
    "director"        text NULL,
    "producers"       text NULL,
    "country"         character varying(255) NULL,
    "trailer"         text NULL,
    "status"          smallint                 NOT NULL DEFAULT 1,
    "start_date"      timestamp with time zone NOT NULL,
    "end_date"        timestamp with time zone NOT NULL,
    "created_at"      timestamp with time zone NOT NULL DEFAULT now(),
    "created_by"      uuid NULL,
    "updated_at"      timestamp with time zone NOT NULL DEFAULT now(),
    "updated_by"      uuid NULL
);

ALTER TABLE "movies"
    ADD CONSTRAINT "fk_user_created" FOREIGN KEY ("created_by") REFERENCES "users" ("id");
ALTER TABLE "movies"
    ADD CONSTRAINT "fk_user_updated" FOREIGN KEY ("updated_by") REFERENCES "users" ("id");

CREATE TRIGGER update_modified_time
    BEFORE UPDATE
    ON public."movies"
    FOR EACH ROW EXECUTE PROCEDURE update_modified_column();
