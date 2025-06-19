package com.example.booking.domain.minio;

import com.example.booking.domain.minio.dto.UploadFileResponse;
import com.example.booking.domain.minio.service.IMinioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/upload")
public class UploadController {

    private static final Logger log = LoggerFactory.getLogger(UploadController.class);
    private final IMinioService minioService;

    public UploadController(IMinioService minioService) {
        this.minioService = minioService;
    }


    // Upload file
    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        return minioService.uploadFile(file);
    }

    @PostMapping(
            value = "/download",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<InputStreamResource> downloadFile(
            @RequestParam String filename
    ) throws Exception {
        InputStream fileStream = minioService.getFile(filename);
        InputStreamResource resource = new InputStreamResource(fileStream);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(filename)
                .build());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // Nếu bạn biết kích thước file, có thể set contentLength:
        // headers.setContentLength(contentLength);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }



    // Xóa file
    @DeleteMapping("/")
    public void deleteFile(@RequestParam String filename) throws Exception {
        minioService.deleteFile(filename);
    }
}
