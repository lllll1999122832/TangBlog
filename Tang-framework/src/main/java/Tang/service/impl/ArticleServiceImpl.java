package Tang.service.impl;

import Tang.cache.RedisCache;
import Tang.constants.SystemConstants;
import Tang.mapper.ArticleMapper;
import Tang.pojo.Article;
import Tang.pojo.ArticleTag;
import Tang.pojo.Category;
import Tang.pojo.ResponseResult;
import Tang.pojo.dto.AddArticleDto;
import Tang.pojo.dto.UpdateArticleDto;
import Tang.service.ArticleTagService;
import Tang.service.CategoryService;
import Tang.utils.BeanCopyUtils;
import Tang.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import Tang.service.ArticleService;
import io.swagger.models.auth.In;
import org.apache.commons.codec.language.bm.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author makejava
 * @since 2023-03-01 18:58:35
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
   private CategoryService categoryService;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;
    @Override
    public ResponseResult getHotArticle() {
        LambdaQueryWrapper<Article>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        //草稿文章不能查出来 status=0;
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量排序
        lambdaQueryWrapper.orderByDesc(Article::getViewCount);
        Page<Article> page = new Page<>(1,10);
         //最多查询10篇文章 采用分页实现
        List<Article> records = this.page(page, lambdaQueryWrapper).getRecords();
        //采用bean拷贝,只暴露一部分数据
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(records, HotArticleVo.class);
        return ResponseResult.okResult(hotArticleVos);
    }

//   @Override
//    public IPage getArticleListVo(Page page, LambdaQueryWrapper queryWrapper) {
//        return articleMapper.getArticleListVo(page,queryWrapper);
//    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0
                ,Article::getCategoryId,categoryId);
        //如果有categoryId就要查询
        //状态是正式发布的文章
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        //对isTop进行排序,这样可以实现置顶
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);
        Page<Article>page=new Page<>(pageNum,pageSize);
        //返回page的数据中data里面有rows(原data)数据和total,所以需要再次封装
        //注意这里配置要配置分页器
        this.page(page,lambdaQueryWrapper);
        //获取categoryName
        List<Article> records = page.getRecords();
//        for (Article record : records) {
//            Category byId = categoryService.getById(record.getCategoryId());
//            record.setCategoryName(byId.getName());
//        } //采用for循环会报错
        List<Article> collect = records.stream().map((o) -> {
//            Category byId = categoryService.getById(o.getCategoryId());
//            o.setCategoryName(byId.getName());
//            return o;
            //使用@Accessors(chain = true) //设置set 时返回返回对象本身
            return o.setCategoryName(categoryService.getById(o.getCategoryId()).getName());
        }).collect(Collectors.toList());
//        IPage<Article>iPage= this.getArticleListVo(page,lambdaQueryWrapper);
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(collect, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return  ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = this.getById(id);
        //从redis中查找viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        //封装为vo对象
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Category category = categoryService.getById(articleDetailVo.getCategoryId());
        if(category!=null) {
            articleDetailVo.setCategoryName(category.getName());
        }
        //封装返回对象
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中的对应的viewCount
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return null;
    }

    @Override
    public ResponseResult add(AddArticleDto articleDto) {
        //保存博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        this.save(article);
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tag -> new ArticleTag(article.getId(), tag))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listArticle(int pageNum, int pageSize, String title, String summary) {
        //把参数输入进去
        LambdaQueryWrapper<Article>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.hasText(title),Article::getTitle,title);
        lambdaQueryWrapper.like(StringUtils.hasText(summary),Article::getSummary,summary);
        Page<Article>page=new Page<>(pageNum,pageSize);
        this.page(page,lambdaQueryWrapper);
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        return ResponseResult.okResult(new PageVo(articleListVos,page.getTotal()));
    }

    @Override
    public ResponseResult getArticleById(Long id) {
        //查询文章信息
        Article article = this.getById(id);
        //封装并补全tags
        ArticleIdVo articleIdVo = BeanCopyUtils.copyBean(article, ArticleIdVo.class);
        LambdaQueryWrapper<ArticleTag>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        //查询到tags的id
        lambdaQueryWrapper.eq(ArticleTag::getArticleId,articleIdVo.getId());
        List<ArticleTag> articleTags = articleTagService.list(lambdaQueryWrapper);
        List<Long> longs = articleTags.stream().map(articleTag -> articleTag.getTagId()).collect(Collectors.toList());
       articleIdVo.setTags(longs);
        return ResponseResult.okResult(articleIdVo);
    }

    @Override
    public ResponseResult updateArticle(UpdateArticleDto updateArticleDto) {
        //先要把Tags更新一下;
        List<Long> tags = updateArticleDto.getTags();
        List<ArticleTag> articleTags = tags.stream().map(tag -> new ArticleTag(updateArticleDto.getId(), tag))
                .collect(Collectors.toList());
        //对ArticleTag 先删除旧的在生成新的
        LambdaUpdateWrapper<ArticleTag>lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ArticleTag::getArticleId,updateArticleDto.getId());
        articleTagService.remove(lambdaUpdateWrapper);
        //然后再生成新的
        articleTagService.saveBatch(articleTags);
        //把 updateArticleDto封装为Article
        Article article = BeanCopyUtils.copyBean(updateArticleDto, Article.class);
        this.updateById(article);
        return ResponseResult.okResult();
    }
}

