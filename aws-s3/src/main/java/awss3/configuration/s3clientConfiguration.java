package awss3.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import lombok.Data;

@Configuration
@Data
public class s3clientConfiguration {

	@Autowired
	private CommonConfiguration commonConfiguration;
	
	public AmazonS3 getAmazonS3Client() {
		BasicAWSCredentials credentials = new BasicAWSCredentials(commonConfiguration.getAccessKey(),commonConfiguration.getSecretKey());
        return AmazonS3ClientBuilder
        		  .standard()
        		  .withCredentials(new AWSStaticCredentialsProvider(credentials))
        		  .withRegion(commonConfiguration.getRegion())
        		  .build();
	}
}
