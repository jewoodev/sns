package sns.demo.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

//@Slf4j
//@RequiredArgsConstructor
//@Component
//@Service
//public class S3Uploader {
//    private final AmazonS3Client client;
//
//    @Value("${cloud.aws.s3.bucket")
//    private String bucket;
//
//    // MultipleFile을 전달받아 File로 변환한 후 S3에 업로드
//    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
//        File uploadFile = convert(multipartFile)
//                .orElseThrow(() -> new IllegalArgumentException("MultipleFile -> File 전환 실패"));
//        return upload(uploadFile, dirName);
//    }
//
//    private String upload(File uploadFile, String dirName) {
//        String fileName = dirName + "/" + uploadFile.getName();
//
//    }
//}
