package com.warpedcitadel.appusermanagement.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudfront.CloudFrontUtilities;
import software.amazon.awssdk.services.cloudfront.model.CannedSignerRequest;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

@Service
public class CloudFrontService {

        @Value("${cloudfront.private-key}")
        private String privateKeyPath;

        @Value("${cloud.aws.keypair}")
        private String keyPair;

        private String cloudFrontDomain = "https://www.warpedcitadel.com/";

        public String generateSignedUrl(String objectKey) {

            try {

                Path key = Paths.get(privateKeyPath);

                CannedSignerRequest request =
                        CannedSignerRequest.builder()
                                .resourceUrl(cloudFrontDomain + objectKey)
                                .privateKey(key)
                                .keyPairId(keyPair)
                                .expirationDate(
                                        Instant.now().plus(Duration.ofHours(2)))
                                .build();

                return CloudFrontUtilities.create()
                        .getSignedUrlWithCannedPolicy(request)
                        .url();
            } catch (Exception exception) {

                System.out.println("Failed to generate presigned url :" + exception);
            }

            return null;
        }
}
