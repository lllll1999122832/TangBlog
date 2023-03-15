package Tang.service.impl;

import Tang.enums.AppHttpCodeEnum;
import Tang.pojo.ResponseResult;
import Tang.service.UploadService;
import Tang.utils.PathUtils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;

@Service
@Data
@ConfigurationProperties(prefix = "oss")
public class UploadServiceImpl implements UploadService {
    private String accessKey;
    private String secretKey;
    private String bucket;
    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        //todo 判断文件类型
        //获取原始文件名
       String filename=img.getOriginalFilename();
        //对原始文件名进行判断
        if(!filename.endsWith(".png")&&!filename.endsWith(".jpg")&&!filename.endsWith("webp")){
            return ResponseResult.errorResult(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        //上传文件到oss,得到外链url
        String url=uploadOSS(img); //2023/3/9/uuid.png
        return ResponseResult.okResult(url);
    }


    public String uploadOSS(MultipartFile img) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
//        String accessKey = "FK_ATJ8-Mf_e-yOEUTgCZZ-hTaHv6HzKcCT6Oj8S";
//        String secretKey = "yFgn7Pa8sZKjZ94oISG2jRPCxrsMwGXwujwjzw1C";
//        String bucket = "rog13";

//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = PathUtils.generateFilePath(img.getOriginalFilename());

        try {
            InputStream inputStream = img.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//                System.out.println(putRet.key);
//                System.out.println(putRet.hash);
                return "http://rr8tmswmn.hn-bkt.clouddn.com/"+key;
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
        return "error";
    }
}
