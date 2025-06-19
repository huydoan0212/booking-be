package com.example.booking.domain.minio.service;

import com.example.booking.domain.minio.dto.UploadFileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface IMinioService {

    UploadFileResponse uploadFile(MultipartFile file) throws Exception;

    InputStream getFile(String filename) throws Exception;

    void deleteFile(String filename) throws Exception;
}
