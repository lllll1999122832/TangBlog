package Tang.controller;

import Tang.pojo.ResponseResult;
import Tang.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping
public class UploadController {
    @Autowired
    UploadService uploadService;
    @PostMapping("/upload")
    public ResponseResult uploadImg(@RequestParam("img")MultipartFile multipartFile){
        try {
           return uploadService.uploadImg(multipartFile);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("图片上传失败!");
        }
    }
}
