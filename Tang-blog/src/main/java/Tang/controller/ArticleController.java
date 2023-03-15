package Tang.controller;

import Tang.pojo.Article;
import Tang.pojo.ResponseResult;
import Tang.service.ArticleService;
import com.github.benmanes.caffeine.cache.Cache;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    ArticleService articleService;
    @Autowired
    Cache<Long, ResponseResult> responseResultCache;

    @GetMapping("/hotArticleList")
    public ResponseResult  getHotArticle(){
        //查询热门文章,结果封装成ResponseResult
        ResponseResult result= articleService.getHotArticle();
        return result;
    }
    @GetMapping("/articleList")
    //参数query类型,即是参数在网址上
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){

        return articleService.articleList(pageNum,pageSize,categoryId);
    }
    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
//        String key="getArticleDetail:"+id;
        //优先缓存
      return   responseResultCache.get(id,o->articleService.getArticleDetail(o));
//        return articleService.getArticleDetail(id);
    }
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id")Long id){
        return articleService.updateViewCount(id);
    }

}
