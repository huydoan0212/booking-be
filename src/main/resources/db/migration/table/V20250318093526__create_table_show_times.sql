CREATE TABLE public."show_times" (
    "id" uuid NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    "movie_id" uuid NOT NULL,
    "cinema_hall_id" uuid NOT NULL,
    "show_time" timestamp with time zone NOT NULL,
    "language" character varying(255) NOT NULL,
    "subtitle" character varying(255),
    "screen_format" character varying(255) NOT NULL,
    "created_at" timestamp with time zone NOT NULL DEFAULT now(),
    "created_by" uuid NULL,
    "updated_at" timestamp with time zone NOT NULL DEFAULT now(),
    "updated_by" uuid NULL
);

ALTER TABLE public."show_times" ADD CONSTRAINT "fk_show_times_movie_id" FOREIGN KEY ("movie_id") REFERENCES public."movies"("id");
ALTER TABLE public."show_times" ADD CONSTRAINT "fk_show_times_cinema_hall_id" FOREIGN KEY ("cinema_hall_id") REFERENCES public."cinema_halls"("id");
ALTER TABLE public."show_times" ADD CONSTRAINT "fk_show_times_created_by" FOREIGN KEY ("created_by") REFERENCES public."users"("id");
ALTER TABLE public."show_times" ADD CONSTRAINT "fk_show_times_updated_by" FOREIGN KEY ("updated_by") REFERENCES public."users"("id");

CREATE TRIGGER update_modified_time BEFORE UPDATE ON public."show_times"
FOR EACH ROW EXECUTE PROCEDURE update_modified_column();
