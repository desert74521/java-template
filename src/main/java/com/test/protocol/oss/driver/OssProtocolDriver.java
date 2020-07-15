package com.test.protocol.oss.driver;

import java.net.URL;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.ServiceSignature;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.test.protocol.oss.OssProtocol;
import com.test.protocol.oss.vo.OssTokenVo;

@Service
public class OssProtocolDriver implements OssProtocol{
	
	@Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;
	
	@Value("${aliyun.oss.access-key-secret}")
    private String accessKeyScret;
	
	@Value("${aliyun.oss.bucket}")
    private String bucket;
	
	@Value("${aliyun.oss.endpoint}")
    private String endpoint;
	
	@Value("${aliyun.oss.dir}")
    private String dir;
	
	@Override
	public OssTokenVo genetorOssToken(String uploaderType) {
		OssTokenVo ossToken = new OssTokenVo();
		ossToken.setAccessid(this.accessKeyId);
		ossToken.setDir(this.dir + "/");
		String policy = "{\"expiration\": \"2120-01-01T12:00:00.000Z\",\"conditions\": [[\"content-length-range\", 0, 1048576000]]}";
        String encodePolicy = Base64.getEncoder().encodeToString(policy.getBytes());
        ossToken.setPolicy(encodePolicy);
		ossToken.setSignature(ServiceSignature.create().computeSignature(this.accessKeyScret, encodePolicy));
		ossToken.setHost("http://"+ this.bucket + "." + this.endpoint);
		return ossToken;
	}


	@Override
	public String genetorOssTokenForDownload(String link, int day) {
		OSSClient ossClient = new OSSClient("http://"+ this.bucket + "." + this.endpoint, this.accessKeyId,
				this.accessKeyScret);
        Date expiration = new Date(new Date().getTime() + 3600 * 1000 * 24 * day);
        GeneratePresignedUrlRequest generatePresignedUrlRequest ;
        if(link.startsWith("https")) {
        	generatePresignedUrlRequest =new GeneratePresignedUrlRequest(this.bucket, link.replace("https://"+ this.bucket + "." + this.endpoint +"/", ""));
        } else {
        	generatePresignedUrlRequest =new GeneratePresignedUrlRequest(this.bucket, link.replace("http://"+ this.bucket + "." + this.endpoint +"/", ""));
        }
        generatePresignedUrlRequest.setExpiration(expiration);
        URL url = ossClient.generatePresignedUrl(generatePresignedUrlRequest);
        String result = url.toString();
        String[] results = result.split("\\?");
        return results[1];
	}


	@Override
	public String replaceEndPoint(String link) {
		String result = "";
		if(link.startsWith("https")) {
			link = link.substring(8);
			result = "https://"+ this.bucket + "." + this.endpoint + link.substring(link.indexOf("/"));
		} else {
			link = link.substring(7);
			result = "http://"+ this.bucket + "." + this.endpoint + link.substring(link.indexOf("/"));
		}
		return result;
	}
	
}
