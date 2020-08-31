package awss3.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.Bucket;

@Service
public interface S3Service {

	public void uploadFile();
	
	public void downloadFile(String key) throws IOException;

	public String uploadMultipartFile(MultipartFile file) throws IOException;

	byte[] downloadFileToByteArray(String key) throws IOException;
	
	public List<Bucket> bucketList();
	
	public void deleteFile(String key) throws IOException;
	
	public List<String> ListObjects();


}
