package awss3.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import awss3.configuration.CommonConfiguration;
import awss3.configuration.s3clientConfiguration;
import awss3.service.S3Service;

@Service
public class S3ServiceImpl implements S3Service {

	@Autowired
	s3clientConfiguration s3clientConfiguration;

	@Autowired
	CommonConfiguration commonConfiguration;

	public void uploadFile() {
		s3clientConfiguration.getAmazonS3Client().putObject(commonConfiguration.getBucketName(), "fileName.txt",
				new File("/Users/user/Documents/fileName.txt"));

	}

	@Override
	public void downloadFile(String key) throws IOException {
		S3Object s3Object = s3clientConfiguration.getAmazonS3Client().getObject(commonConfiguration.getBucketName(),
				key);
		InputStream s3ObjectInputStream = s3Object.getObjectContent();
		File targetFile = new File("/Users/user/Documents/target.jpg");
		java.nio.file.Files.copy(s3ObjectInputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

		IOUtils.closeQuietly(s3ObjectInputStream);
	}

	@Override
	public String uploadMultipartFile(MultipartFile file) throws IOException {
		String fileName = generateFileName(file);
		File uploadfile = convertMultiPartToFile(file);
		PutObjectResult result = s3clientConfiguration.getAmazonS3Client()
				.putObject(commonConfiguration.getBucketName(), fileName, uploadfile);
		System.out.println(result);
		uploadfile.delete();
		return commonConfiguration.getS3bucketUrl() + fileName;
	}

	@Override
	public byte[] downloadFileToByteArray(String key) throws IOException {
		S3Object s3Object = s3clientConfiguration.getAmazonS3Client().getObject(commonConfiguration.getBucketName(),
				key);
		InputStream inputStream = s3Object.getObjectContent();
		System.out.println(inputStream.read());
		byte[] bytes = new byte[inputStream.available()];
		System.out.println(bytes);
		return bytes;
	}

	@Override
	public List<Bucket> bucketList() {
		List<Bucket> buckets = s3clientConfiguration.getAmazonS3Client().listBuckets();
		return buckets;
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	@Override
	public void deleteFile(String key) throws IOException {
		s3clientConfiguration.getAmazonS3Client().deleteObject(commonConfiguration.getBucketName(), key);
	}

	@Override
	public List<String> ListObjects() {
		List<String> fileList = new ArrayList<>();
		ObjectListing objectList = s3clientConfiguration.getAmazonS3Client()
				.listObjects(commonConfiguration.getBucketName());
		for (S3ObjectSummary s3ObjectSummary : objectList.getObjectSummaries()) {
			fileList.add(s3ObjectSummary.getKey());
		}
		return fileList;
	}
}
