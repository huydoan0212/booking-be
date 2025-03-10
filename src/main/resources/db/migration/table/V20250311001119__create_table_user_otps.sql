CREATE TABLE public."user_otps"
(
    "id"               uuid                     NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    "otp_code"         varchar(255)             NOT NULL,
    "otp_expired_time" timestamp with time zone NOT NULL,
    "used"             boolean                  NOT NULL DEFAULT false,
    "otp_type"         varchar(50) NULL,
    "user_id"          uuid                     NOT NULL,
    "created_at"       timestamp with time zone NOT NULL DEFAULT now(),
    "updated_at"       timestamp with time zone NOT NULL DEFAULT now()
);
ALTER TABLE "user_otps"
    ADD CONSTRAINT "fk_user_otps_user_id" FOREIGN KEY ("user_id") REFERENCES "users" ("id");

CREATE TRIGGER update_modified_time
    BEFORE UPDATE
    ON public."user_otps"
    FOR EACH ROW EXECUTE PROCEDURE update_modified_column();
