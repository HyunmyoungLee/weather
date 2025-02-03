package com.weatherandoutfit.infra;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@NoArgsConstructor
@Slf4j
@PropertySource("classpath:amazon.properties")
@Service
public class S3Service {
	
	private  AmazonS3 amazonS3;
	@Value("${s3.bucket}")
	private String bucketName;
	
	
	
	//이미지 파일 업로드
	public String uploadFile(MultipartFile file) throws IOException {
		String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
		
		File convertedFile = convertFile(file);
		log.info("{},{}", fileName, convertedFile);
		log.info("{}", bucketName);
		amazonS3.putObject(new PutObjectRequest(bucketName, fileName, convertedFile)
				.withCannedAcl(CannedAccessControlList.PublicRead));
		
		convertedFile.delete();
		return amazonS3.getUrl(bucketName, fileName).toString();
	}
	
	public void deleteFile(String fileName) {
		amazonS3.deleteObject(bucketName,fileName);
	}

	private File convertFile(MultipartFile file) throws IOException {
		File convertFile = new File(System.getProperty("java.io.tmpdir") + "/" +file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convertFile);
		fos.write(file.getBytes());
		fos.close();
		return convertFile;
	}	
}
