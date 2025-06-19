CREATE TABLE public."discounts"
(
    "id"             uuid                     NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    "code"           character varying(255)   NOT NULL,
    "description"    character varying(255)   NOT NULL,
    "discount_type"  character varying(255)   NOT NULL,
    "discount_value" double precision         NOT NULL,
    "start_date"     timestamp with time zone NOT NULL,
    "end_date"       timestamp with time zone NOT NULL,
    "usage_limit"    integer                  NOT NULL,
    "created_at"     timestamp with time zone NOT NULL DEFAULT now(),
    "created_by"     uuid NULL,
    "updated_at"     timestamp with time zone NOT NULL DEFAULT now(),
    "updated_by"     uuid NULL
);

ALTER TABLE public."discounts"
    ADD CONSTRAINT "discounts_created_by_fkey" FOREIGN KEY ("created_by") REFERENCES public."users" ("id");
ALTER TABLE public."discounts"
    ADD CONSTRAINT "discounts_updated_by_fkey" FOREIGN KEY ("updated_by") REFERENCES public."users" ("id");


CREATE TRIGGER update_modified_time
    BEFORE UPDATE
    ON public."discounts"
    FOR EACH ROW EXECUTE PROCEDURE update_modified_column();
