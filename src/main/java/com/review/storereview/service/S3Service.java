package com.review.storereview.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.CollectionUtils;
import com.review.storereview.common.exception.ParamValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class S3Service {

    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;
    private static final String S3_END_POINT = "https://storereview-bucket.s3.us-east-2.amazonaws.com/";

    @PostConstruct
    public void setS3Client() {
        // AWSCredentials 생성
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        // S3 Client 생성
        s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    /**
     * 새로운 파일을 업로드한다.
     * @param multipartFile
     * @return 업로드한 이미지파일의 url 반환
     */
    @Transactional
    public String uploadFile(MultipartFile multipartFile) {
        // 1.  중복아닌 이름 생성  ex) {UUID}_{파일명} +
        String fileName = createFileName(getFileExtension(multipartFile.getOriginalFilename()));

        // 2. ObjectMetadata로 변환 및 S3 업로드
         String uploadImageUrl = putS3(multipartFile, fileName);
        return uploadImageUrl;
    }

    // 파일 이름 생성
    private String createFileName(String originalFilename) {
        return UUID.randomUUID() + "_" + originalFilename;
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
        // TODO url을 전달할 지 파일명을 전달할지 결정해야함 with 프론트
        return s3Client.getUrl(bucketName, fileName).toString();
    }

    /**
     * S3에 업로드된 이미지파일을 삭제한다.
     * @param deletedImgUrls
     */
    public void deleteFiles(List<String> deletedImgUrls) {
        String fileName = null;
        List<String> imgUrlList = deletedImgUrls;
        if (!CollectionUtils.isNullOrEmpty(imgUrlList))
            for (String deletedImgUrl : imgUrlList) {
                fileName = deletedImgUrl.replace(S3_END_POINT, "");
                s3Client.deleteObject(bucketName, fileName);
            }
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("S3Service.getFileExtension 호출됨==========");
            Map<String, String> parameterErrorMsg = new HashMap<>();
            parameterErrorMsg.put(fileName, "잘못된 형식의 파일입니다.");
            throw new ParamValidationException(parameterErrorMsg);
        }
    }
    /**
     * 업데이트 시, 이미지 작업 호출
     * 1. 제거 : db imgUrl에서 dto.imgUrl을 중복 제거하여 삭제할 imgUrl을 구한다.
     * 2. 추가 : 1) imgFile 업로드 2) 업로드한 imgUrl을 dto.imgUrl에 추가
     * 3. review.setImgUrl()
     */
    public List<String> saveOrDeleteImg (List<String> imgUrlsFromDB, List<String> remainingImgUrls, List<MultipartFile> addedImgFiles) {
        // 반환할 imgUrl에 남은 imgUrl 미리 저장
        List<String> renewImgUrls = remainingImgUrls;

        // 1. 남은 이미지 처리 (삭제 or Nothing)
        System.out.println("남은 imgUrl null인지 체크 : " +Objects.isNull(remainingImgUrls));
        if (CollectionUtils.isNullOrEmpty(remainingImgUrls)) {   // 전달된 남은 imgUrl이 비었고
            if (!CollectionUtils.isNullOrEmpty(imgUrlsFromDB)) // 원래 이미지가 있는 리뷰의 경우, 삭제 진행
                deleteFiles(imgUrlsFromDB);
        }
       else {  // 전달된 남은 imgUrl이 있고
            if (!CollectionUtils.isNullOrEmpty(imgUrlsFromDB)) // 원래 이미지가 있는 리뷰의 경우, 비교 후 삭제 진행
                remainingImgUrls.forEach(remainingImgUrl -> {
                    imgUrlsFromDB.removeIf(dbUrl -> dbUrl.equals(remainingImgUrl));    // db의 url리스트와 다른지 비교하면서 같으면  제거. 남은 url은 제거할 url
                });
            // 제거할 imgUrl이 있으면 삭제 진행
            if (!CollectionUtils.isNullOrEmpty(imgUrlsFromDB))
                deleteFiles(imgUrlsFromDB);
        }

       // 2. 추가된 이미지파일 추가
        if (!CollectionUtils.isNullOrEmpty(addedImgFiles)) {
            // s3 업로드 및 url 추가
            for (MultipartFile imgFile : addedImgFiles) {
                String imgUrl = uploadFile(imgFile);
                renewImgUrls.add(imgUrl);    // 기존 imgUrls에 업로드된 url 추가
            }
        }
       return renewImgUrls;
    }

    /**
     * 프론트로부터 혹은 DB로부터 전달된 객체가 비었는지 체크하기 위해
     * null인 경우와 빈 배열일 경우를 동시에 체크한다.
     */
    private boolean isEmptydoubleCheck(List<?> objects) {
        if (!Objects.isNull(objects))
            if (!objects.isEmpty()) {
                return true;
        }
        return false;
    }
}
