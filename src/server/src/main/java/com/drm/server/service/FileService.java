package com.drm.server.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.drm.server.common.FileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final AmazonS3Client amazonS3Client;

    private String bucketName="drmbucketofficial";

    public String getUuidFileName(String fileName) {
        String ext = fileName.substring(fileName.indexOf(".") + 1);
        return UUID.randomUUID() + "." + ext;
    }
    public FileDto uploadFile(String filePath, MultipartFile multipartFile){
        String originalFileName = multipartFile.getOriginalFilename();
        String uploadFileName = getUuidFileName(originalFileName);
        String uploadFileUrl = "";

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());
        String keyName ="" ;
        try (InputStream inputStream = multipartFile.getInputStream()) {

            keyName = filePath + "/" + uploadFileName;

            // S3에 폴더 및 파일 업로드
            amazonS3Client.putObject(
                    new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

            // S3에 업로드한 폴더 및 파일 URL
            uploadFileUrl = "https://drmbucketofficial.s3.ap-northeast-2.amazonaws.com/" + keyName;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return FileDto.builder().originalFileName(originalFileName)
                .uploadFileName(uploadFileName)
                .uploadFilePath(filePath)
                .uploadFileUrl(uploadFileUrl)
                .build();

    }
    public void deleteFile(String filePath, String fileName){
        String keyName = filePath + "/" + fileName;
        try{
            amazonS3Client.deleteObject(bucketName,keyName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }

    }
//    public List<FileDto> uploadFiles(List<MultipartFile> multipartFiles) {
//
//
//        List<FileDto> s3files = new ArrayList<>();
//
//        for (MultipartFile multipartFile : multipartFiles) {
//            FileDto fileDto = uploadFile(multipartFile);
//            s3files.add(fileDto);
//        }
//
//        return s3files;
//    }

}
