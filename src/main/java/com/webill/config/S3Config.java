package com.webill.config;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {


    @Value("${webill.aws.access_key_id}")
    private String awsId;

    @Value("${webill.aws.secret_access_key}")
    private String awsKey;

    @Value("${webill.s3.region}")
    private String region;

    @Value("${webill.s3.bucket}")
    private String bucketNameWebill;

    @Bean
    public AmazonS3 s3Client(){
        //System.out.println("The credentials informations are: "+awsId+" and "+awsKey);
        
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsId, awsKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withRegion(Regions.fromName(region))
            .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
            .build();

        return s3Client;
    }


    public String getAwsId() {
        return awsId;
    }

    public void setAwsId(String awsId) {
        this.awsId = awsId;
    }

    public String getAwsKey() {
        return awsKey;
    }

    public void setAwsKey(String awsKey) {
        this.awsKey = awsKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBucketNameWebill() {
        return bucketNameWebill;
    }

    public void setBucketNameWebill(String bucketNameWebill) {
        this.bucketNameWebill = bucketNameWebill;
    }
}
