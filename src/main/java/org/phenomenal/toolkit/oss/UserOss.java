package org.phenomenal.toolkit.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import org.phenomenal.toolkit.entities.OssProperty;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Component
public class UserOss {
    private static OSS client;
    private final OssProperty ossProperty;

    public UserOss(OssProperty ossProperty) {
        this.ossProperty = ossProperty;
    }

    public void init() {
        if (client == null){
            client = new OSSClientBuilder().build(ossProperty.getEndpoint(),ossProperty.getAccessKey(),ossProperty.getAccessSecret());
        }
    }
    public boolean uploadAvatar(String objectName ,ByteArrayInputStream inputStream){
        init();
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossProperty.getBucketName(), objectName, inputStream);
            client.putObject(putObjectRequest);
        }catch (OSSException oe){
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            return false;
        }catch (com.aliyun.oss.ClientException ce){
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
            return false;
        }
        return true;
    }
}
