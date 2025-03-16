CREATE TABLE public."cinema_images"
(
    "cinema_id" uuid NOT NULL,
    "img_url"   text NOT NULL,
    PRIMARY KEY ("cinema_id", "img_url")
);

ALTER TABLE public."cinema_images"
    ADD CONSTRAINT "fk_cinema_images_cinema" FOREIGN KEY ("cinema_id")
        REFERENCES public."cinemas" ("id")
        ON DELETE CASCADE;
