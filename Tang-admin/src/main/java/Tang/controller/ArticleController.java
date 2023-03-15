package Tang.controller;

import Tang.pojo.ResponseResult;
import Tang.pojo.dto.AddArticleDto;
import Tang.pojo.dto.UpdateArticleDto;
import Tang.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    ArticleService articleService;
    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto articleDto){
        return articleService.add(articleDto);
    }

    @GetMapping("/list")
    public ResponseResult listArticle(int pageNum,int pageSize,String title,String summary){
        return articleService.listArticle(pageNum,pageSize,title,summary);
    }
    @GetMapping("/{id}")
    public ResponseResult getArticleById(@PathVariable("id")Long id){
        return articleService.getArticleById(id);
    }
    @PutMapping
    public ResponseResult updateArticle(@RequestBody UpdateArticleDto updateArticleDto){
        return articleService.updateArticle(updateArticleDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable("id")Long id){
        articleService.removeById(id);
        return ResponseResult.okResult();
    }
}
