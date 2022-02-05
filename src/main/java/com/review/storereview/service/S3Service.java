package com.review.storereview.service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.common.exception.ParamValidationException;
import com.review.storereview.controller.cms.ReviewApiController;
import com.review.storereview.dto.ResponseJsonObject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final Logger logger = LoggerFactory.getLogger(S3Service.class);

    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        // AWSCredentials 생성
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride("AWSS3V4SignerType");

        // S3 Client 생성
        s3Client = AmazonS3ClientBuilder.standard()
                .withClientConfiguration(clientConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
        final S3ClientOptions clientOptions = S3ClientOptions.builder().setPathStyleAccess(true).disableChunkedEncoding().build();
        s3Client.setS3ClientOptions(clientOptions);
    }

    /**
     * 새로운 파일을 업로드한다.
     * @param multipartFile
     */
    @Transactional
    public String uploadFile(MultipartFile multipartFile) {
        // 1.  중복아닌 이름 생성  ex) {UUID}_{파일명} +
        String fileName = createFileName(getFileExtension(multipartFile.getOriginalFilename()));

        // 2. ObjectMetadata로 변환 및 S3 업로드
        uploadFile = putS3(multipartFile, fileName);



        // 4. 로컬 파일 제거
        removeNewFile(uploadFile.get());
        return uploadImageUrl;
    }

    // 파일 이름 생성
    private String createFileName(String originalFilename) {
        return UUID.randomUUID() + "_" + originalFilename;
    }

    /**
     * MultipartFile을 File로 변환한다.
     * @param file
     * @throws IOException
     */
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {   // 생성되면 true
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    /**
     * convert하는 과정에서 생성된 로컬파일을 S3 업로드 완료 후 삭제한다.
     * @param targetFile
    */
    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            logger.info("파일이 삭제되었습니다.");
        }else {
            logger.error("파일이 삭제되지 못했습니다.");
        }
    }

    /**
     * 실제 s3에 이미지를 업로드한 후 접근 가능한 주소를 반환한다.
     * @param uploadFile
     * @param fileName
     */
    private String putS3(MultipartFile uploadFile, String fileName) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(uploadFile.getSize());
        objectMetadata.setContentType(uploadFile.getContentType());
        try(InputStream inputStream = uploadFile.getInputStream()) {
            s3Client.putObject(
                    new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)  // {S3 주소}/{UUID}_{파일명} 경로로 파일 업로드
                            .withCannedAcl(CannedAccessControlList.PublicRead)  // 누구나 파일 읽기 가능
            );
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
        }

        return s3Client.getUrl(bucketName, fileName).toString();

    }

    /**
     * S3에 업로드된 이미지파일을 삭제한다.
     * @param fileName
     */
    public void deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            Map<String, String> parameterErrorMsg = new HashMap<>();
            parameterErrorMsg.put(fileName, "잘못된 형식의 파일입니다.");
            throw new ParamValidationException(parameterErrorMsg);
        }
    }
}
