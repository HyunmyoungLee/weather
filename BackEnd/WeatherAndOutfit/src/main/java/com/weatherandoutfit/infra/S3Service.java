package com.weatherandoutfit.infra;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class S3Service {

	@Value("${accesskey}")
	private String accesskey;
	@Value("${secretkey}")
	private String secretKey;
	
	@Autowired
	private  AmazonS3 amazonS3;
	private  final String bucketName = "weatherandoutfit";
	
	
	
	//이미지 파일 업로드
	public String uploadFile(MultipartFile file) throws IOException {
		String fileName = "userProfile/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
		
		File convertedFile = convertFile(file);
		log.info("{},{}", fileName, convertedFile);
		log.info(" accesskey : {}, secretKey : {}",accesskey, secretKey);
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
