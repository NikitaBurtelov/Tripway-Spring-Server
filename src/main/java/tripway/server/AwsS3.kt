package tripway.server

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@PropertySource("classpath:/application.properties")
@Component
@Slf4j
class AwsS3 {
    @Autowired
    private lateinit var environment: Environment

    @Bean
    fun s3Client(): AmazonS3Client {
        val credentials = BasicAWSCredentials(
                environment.getProperty("aws.s3.access_key_id"),
                environment.getProperty("aws.s3.secret_access_key")
        )

        val s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_WEST_1)
                .build()

        if (s3Client.doesBucketExistV2(bucketName)) {
            LoggerFactory.getLogger(AwsS3::class.java.name).info("Bucket name is not available."
                    + " Try again with a different Bucket name.")
        } else {
            s3Client.createBucket(bucketName);
        }
        return s3Client as AmazonS3Client
    }

    companion object{
        const val bucketName = "tripway-bucket"
    }
}
