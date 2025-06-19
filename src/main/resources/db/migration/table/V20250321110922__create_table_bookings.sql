CREATE TABLE public."bookings"
(
    "id"             uuid                     NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    "booking_code"   character varying(255)   NOT NULL,
    "total_price"    double precision         NOT NULL,
    "discount_id"    uuid NULL,
    "user_id"        uuid                     NOT NULL,
    "final_price"    double precision         NOT NULL,
    "booking_status" character varying(255)   NOT NULL,
    "payment_status" character varying(255)   NOT NULL,
    "created_at"     timestamp with time zone NOT NULL DEFAULT now(),
    "created_by"     uuid NULL,
    "updated_at"     timestamp with time zone NOT NULL DEFAULT now(),
    "updated_by"     uuid NULL
);

ALTER TABLE public."bookings"
    ADD CONSTRAINT "bookings_discount_id_fkey" FOREIGN KEY ("discount_id") REFERENCES public."discounts" ("id");
ALTER TABLE public."bookings"
    ADD CONSTRAINT "bookings_user_id_fkey" FOREIGN KEY ("user_id") REFERENCES public."users" ("id");
ALTER TABLE public."bookings"
    ADD CONSTRAINT "bookings_created_by_fkey" FOREIGN KEY ("created_by") REFERENCES public."users" ("id");
ALTER TABLE public."bookings"
    ADD CONSTRAINT "bookings_updated_by_fkey" FOREIGN KEY ("updated_by") REFERENCES public."users" ("id");

CREATE TRIGGER update_modified_time
    BEFORE UPDATE
    ON public."bookings"
    FOR EACH ROW EXECUTE PROCEDURE update_modified_column();
