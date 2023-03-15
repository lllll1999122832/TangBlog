import Tang.TangBlogApplication;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Deque;
import java.util.LinkedList;

@SpringBootTest(classes = TangBlogApplication.class)
@Component
//@ConfigurationProperties(prefix = "oss")
public class OSSTest {

    private String accessKey;
   private String secretKey;
   private String bucket;



    @Test
    public void testOSS(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
//...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
//        String accessKey = "FK_ATJ8-Mf_e-yOEUTgCZZ-hTaHv6HzKcCT6Oj8S";
//        String secretKey = "yFgn7Pa8sZKjZ94oISG2jRPCxrsMwGXwujwjzw1C";
//        String bucket = "rog13";

//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "2023/lin.png";

        try {
//            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
//            ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);
            InputStream inputStream=new FileInputStream("E:\\小米10图像\\c3f768f742a73a6f6f293a9e26fed19c.webp");
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
//        } catch (UnsupportedEncodingException ex) {
//            //ignore
//        }
    } catch (Exception ex) {
        //ignore
    }

    }
    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public static void main(String[] args) {
        Deque<Integer>deque=new LinkedList<>();
        deque.addLast(2);
        Integer num=1;
        if(num>deque.peekFirst()){
            System.out.println(1);
        }
    }
}
