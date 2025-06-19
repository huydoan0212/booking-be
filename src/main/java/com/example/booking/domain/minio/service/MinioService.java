package com.example.booking.domain.minio.service;

import com.example.booking.domain.minio.dto.UploadFileResponse;
import io.minio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

@Service
public class MinioService implements IMinioService {

    private static final Logger log = LoggerFactory.getLogger(MinioService.class);
    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    @Value("${minio.url}")
    private String url;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    // Kiểm tra và tạo bucket nếu chưa tồn tại
    private void ensureBucketExists() throws Exception {
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!isExist) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    public UploadFileResponse uploadFile(MultipartFile file) throws Exception {
        ensureBucketExists();
        try (InputStream is = file.getInputStream()) {
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();

            // Xác định folder dựa trên loại file
            String folder = determineFolder(contentType);
            String objectName = folder + "/" + fileName; // Đặt file vào thư mục

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(is, file.getSize(), -1)
                            .contentType(contentType) // Đặt MIME type cho file
                            .headers(Map.of("Content-Disposition", "inline")) // Thêm header Content-Disposition
                            .build()
            );

            return new UploadFileResponse(
                    fileName,
                    contentType,
                    file.getSize(),
                    url + bucketName + "/" + objectName
            );
        } catch (Exception e) {
            throw new Exception("Error uploading file", e);
        }
    }

    // Phương thức xác định folder dựa trên loại file
    private String determineFolder(String contentType) {
        if (contentType == null) {
            return "file"; // Mặc định lưu vào /file nếu không xác định được MIME type
        }

        // Nhóm MIME type cho hình ảnh, video, audio
        if (contentType.startsWith("image/")) {
            return "image";
        } else if (contentType.startsWith("video/") || contentType.startsWith("audio/")) {
            return "image";
        }
        // Nhóm MIME type cho tài liệu
        else if (contentType.equals("application/pdf") ||
                contentType.equals("application/msword") ||
                contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
                contentType.equals("application/vnd.ms-excel") ||
                contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            return "file";
        }

        return "file"; // Mặc định lưu vào /file
    }


    // Lấy file từ MinIO
    public InputStream getFile(String filename) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder().extraHeaders(Map.of("Content-Disposition", "attachment")).bucket(bucketName).object(filename).build());
    }

    // Xóa file từ MinIO
    public void deleteFile(String filename) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(filename).build());
    }
}
