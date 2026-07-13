package com.warpedcitadel.appusermanagement.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudfront.CloudFrontUtilities;
import software.amazon.awssdk.services.cloudfront.model.CannedSignerRequest;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

@Service
public class CloudFrontService {

        @Value("classpath:keys/private_key.pem")
        private Resource privateKeyResource;

        @Value("${cloud.aws.keypair}")
        private String keyPair;

        private String cloudFrontDomain = "https://www.warpedcitadel.com/";

        public String generateSignedUrl(String objectKey) {

            try {

                Path privateKeyPath = privateKeyResource.getFile().toPath();

                CannedSignerRequest request =
                        CannedSignerRequest.builder()
                                .resourceUrl(cloudFrontDomain + objectKey)
                                .privateKey(privateKeyPath)
                                .keyPairId(keyPair)
                                .expirationDate(
                                        Instant.now().plus(Duration.ofHours(2)))
                                .build();

                return CloudFrontUtilities.create()
                        .getSignedUrlWithCannedPolicy(request)
                        .url();
            } catch (Exception exception) {

                System.out.println("Failed to generate Presigned URL");
            }

            return null;
        }
}
