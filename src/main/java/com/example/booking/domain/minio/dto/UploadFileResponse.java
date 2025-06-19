package com.example.booking.domain.minio.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadFileResponse {
    private String url;
    private String nameFile;
    private String mime;
    private long size;

    public UploadFileResponse(String fileName, String contentType, long size, String url) {
        this.nameFile = fileName;
        this.mime = contentType;
        this.size = size;
        this.url = url;
    }
}
