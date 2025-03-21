CREATE TABLE public."tickets"
(
    "id"            uuid                     NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    "ticket_code"   character varying(255)   NOT NULL,
    "show_time_id"  uuid                     NOT NULL,
    "price"         double precision         NOT NULL,
    "ticket_status" character varying(255)   NOT NULL,
    "ticket_type"   character varying(255)   NOT NULL,
    "seat_id"       uuid                     NOT NULL,
    "booking_id"    uuid NULL,
    "created_at"    timestamp with time zone NOT NULL DEFAULT now(),
    "created_by"    uuid NULL,
    "updated_at"    timestamp with time zone NOT NULL DEFAULT now(),
    "updated_by"    uuid NULL
);

ALTER TABLE public."tickets"
    ADD CONSTRAINT "tickets_show_time_id_fkey" FOREIGN KEY ("show_time_id") REFERENCES public."show_times" ("id");
ALTER TABLE public."tickets"
    ADD CONSTRAINT "tickets_seat_id_fkey" FOREIGN KEY ("seat_id") REFERENCES public."seats" ("id");
ALTER TABLE public."tickets"
    ADD CONSTRAINT "tickets_booking_id_fkey" FOREIGN KEY ("booking_id") REFERENCES public."bookings" ("id");
ALTER TABLE public."tickets"
    ADD CONSTRAINT "tickets_created_by_fkey" FOREIGN KEY ("created_by") REFERENCES public."users" ("id");
ALTER TABLE public."tickets"
    ADD CONSTRAINT "tickets_updated_by_fkey" FOREIGN KEY ("updated_by") REFERENCES public."users" ("id");

CREATE TRIGGER update_modified_time
    BEFORE UPDATE
    ON public."tickets"
    FOR EACH ROW EXECUTE PROCEDURE update_modified_column();
