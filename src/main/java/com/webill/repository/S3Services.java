package com.webill.repository;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface S3Services {

    public ByteArrayOutputStream downloadFile(String keyName);

    public void uploadFile(String keyName, MultipartFile file);

    public List<String> listFiles();

    public void deleteFile(String keyName);

    public String getFile(String keyName);

    public void uploadFileFromServer(String keyName,File file);

}
