package com.webill.web.rest;
import com.webill.repository.S3Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class UploadFileRessource {

    @Autowired
    S3Services s3Services;

    @PostMapping("/file/upload")
    public String uploadMultipartFile(@RequestParam("file") MultipartFile file) {
        String keyName = file.getOriginalFilename();
        s3Services.uploadFile(keyName, file);
        return "Upload Successfully -> KeyName = " + keyName;
    }
}
