package Tang.service;

import Tang.pojo.Article;
import Tang.pojo.ResponseResult;
import Tang.pojo.dto.AddArticleDto;
import Tang.pojo.dto.UpdateArticleDto;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 文章表(Article)表服务接口
 *
 * @author makejava
 * @since 2023-03-01 18:58:34
 */
public interface ArticleService extends IService<Article> {

    ResponseResult getHotArticle();

//    IPage getArticleListVo(Page page, LambdaQueryWrapper queryWrapper);

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto articleDto);

    ResponseResult listArticle(int pageNum, int pageSize, String title, String summary);

    ResponseResult getArticleById(Long id);

    ResponseResult updateArticle(UpdateArticleDto updateArticleDto);
}

