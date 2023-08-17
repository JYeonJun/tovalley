package kr.ac.kumoh.illdang100.tovalley.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import kr.ac.kumoh.illdang100.tovalley.domain.ImageFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    /**
     * S3 bucket 파일 다운로드
     */
    public ResponseEntity<byte[]> getObject(String storedFileName) throws IOException {
        S3Object o = amazonS3.getObject(new GetObjectRequest(bucket, storedFileName));
        S3ObjectInputStream objectInputStream = ((S3Object) o).getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        String fileName = URLEncoder.encode(storedFileName, "UTF-8").replaceAll("\\+", "%20");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_PNG);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    /**
     * 파일 업로드
     */
    public ImageFile upload(MultipartFile multipartFile, String fileRootPath) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename(); // 파일 이름
        long size = multipartFile.getSize(); // 파일 크기

        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(multipartFile.getContentType());
        objectMetaData.setContentLength(size);

        // S3에 업로드
        String storeFileName = fileRootPath + "/" + UUID.randomUUID() + originalFilename;
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, storeFileName, multipartFile.getInputStream(), objectMetaData)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        String imageUrl = amazonS3Client.getUrl(bucket, storeFileName).toString();

        return new ImageFile(storeFileName, imageUrl);
    }

    /**
     * 파일 삭제
     */
    public void delete(String storeFileName) {
        boolean isExistObject = amazonS3Client.doesObjectExist(bucket, storeFileName);
        if (isExistObject)
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, storeFileName));
    }

    /**
     * 파일 여러개 삭제
     * @param storeFileNameList
     */
    public void deleteFiles(List<String> storeFileNameList) {
        List<KeyVersion> keyList = new ArrayList<>();

        for (String fileName : storeFileNameList) {
            keyList.add(new KeyVersion(fileName));
        }

        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket);
        deleteObjectsRequest.setKeys(keyList);

        amazonS3Client.deleteObjects(deleteObjectsRequest);
    }
}