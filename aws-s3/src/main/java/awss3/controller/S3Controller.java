package awss3.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.Bucket;

import awss3.service.S3Service;

@RestController
@RequestMapping(value="/file")
public class S3Controller {

	@Autowired
	S3Service s3Service;
	
	@PostMapping(value="/upload")
	public ResponseEntity<String> upload() {
		s3Service.uploadFile();
		return new ResponseEntity<>("Success",HttpStatus.OK);
	}

	@PostMapping(value="/upload/multipart")
	public ResponseEntity<String> uploadMultipartFile(@RequestParam("file") MultipartFile file) throws IOException {
		String uploadedFileURL = s3Service.uploadMultipartFile(file);
		return new ResponseEntity<>(uploadedFileURL,HttpStatus.OK);
	}

	@PostMapping(value="/download")
	public ResponseEntity<String> download(@RequestParam("key") String key) throws IOException {
		s3Service.downloadFile(key);
		return new ResponseEntity<>("Success",HttpStatus.OK);
	}
	
	@PostMapping(value="/download/byte-array")
	public ResponseEntity<byte[]> downloadFileToByteArray(@RequestParam("key") String key) throws IOException {
		byte[] bytes = s3Service.downloadFileToByteArray(key);
		return new ResponseEntity<>(bytes,HttpStatus.OK);
	}
	
	@GetMapping(value="/listBuckets")
	public ResponseEntity<List<Bucket>> bucketList(){
		return new ResponseEntity<>(s3Service.bucketList(),HttpStatus.OK);
	}
	
	@PostMapping(value="/deleteFile")
	public ResponseEntity<String> deleteFile(@RequestParam("key") String key) throws IOException {
		s3Service.deleteFile(key);
		return new ResponseEntity<>("Success",HttpStatus.OK);
	}
	
	@GetMapping(value="/listObjects")
	public ResponseEntity<List<String>> listObjects(){
		return new ResponseEntity<>(s3Service.ListObjects(),HttpStatus.OK);
	}
}
