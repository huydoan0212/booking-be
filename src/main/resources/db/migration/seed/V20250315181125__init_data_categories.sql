INSERT INTO categories (id, name, slug, description, created_at, created_by, updated_at, updated_by)
VALUES
    (uuid_generate_v4(), 'Hành động', 'hanh-dong', 'Thể loại phim có nhiều pha hành động kịch tính.', now(), null, now(), null),
    (uuid_generate_v4(), 'Kinh dị', 'kinh-di', 'Thể loại phim mang đến cảm giác hồi hộp, sợ hãi.', now(), null, now(), null),
    (uuid_generate_v4(), 'Hài hước', 'hai-huoc', 'Phim hài mang lại tiếng cười và giải trí.', now(), null, now(), null),
    (uuid_generate_v4(), 'Tình cảm', 'tinh-cam', 'Phim xoay quanh câu chuyện tình yêu, cảm xúc.', now(), null, now(), null),
    (uuid_generate_v4(), 'Khoa học viễn tưởng', 'khoa-hoc-vien-tuong', 'Phim về công nghệ, tương lai, vũ trụ.', now(), null, now(), null),
    (uuid_generate_v4(), 'Hoạt hình', 'hoat-hinh', 'Phim dành cho trẻ em và cả người lớn với nhiều bài học.', now(), null, now(), null),
    (uuid_generate_v4(), 'Phiêu lưu', 'phieu-luu', 'Phim về hành trình khám phá, trải nghiệm mới.', now(), null, now(), null),
    (uuid_generate_v4(), 'Tâm lý', 'tam-ly', 'Phim khai thác sâu về cảm xúc và suy nghĩ của nhân vật.', now(), null, now(), null),
    (uuid_generate_v4(), 'Thể thao', 'the-thao', 'Phim về các môn thể thao và tinh thần đồng đội.', now(), null, now(), null),
    (uuid_generate_v4(), 'Âm nhạc', 'am-nhac', 'Phim liên quan đến âm nhạc, nghệ sĩ, ban nhạc.', now(), null, now(), null);