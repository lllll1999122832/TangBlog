package Tang.service.impl;

import Tang.constants.SystemConstants;
import Tang.mapper.CategoryMapper;
import Tang.pojo.Article;
import Tang.pojo.Category;
import Tang.pojo.ResponseResult;
import Tang.pojo.dto.CategoryDto;
import Tang.pojo.dto.CategoryUpdateDto;
import Tang.service.ArticleService;
import Tang.service.CategoryService;
import Tang.utils.BeanCopyUtils;
import Tang.vo.CategoriesVo;
import Tang.vo.CategoryPageVo;
import Tang.vo.PageVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sg_category(分类表)】的数据库操作Service实现
* @createDate 2023-03-02 18:15:28
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService {
    @Autowired
    ArticleService articleService;
    @Override
    public ResponseResult getCategoryList() {
        //查询文章表,已发布的
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> list = articleService.list(lambdaQueryWrapper);
        //获取文章的分类id,并且去重
        Set<Long> collect = list.stream().map(o -> o.getCategoryId())
                .collect(Collectors.toSet());
        //获取分类表
        List<Category> categories = this.listByIds(collect);
       categories=categories.stream().filter(o->o.getStatus().equals(SystemConstants.STATUS_NORMAL))
               .collect(Collectors.toList()); //文章标签必须为已经发布了的
        //封装vo
        List<CategoriesVo> categoriesVos = BeanCopyUtils.copyBeanList(categories, CategoriesVo.class);
        return ResponseResult.okResult(categoriesVos);
    }

    @Override
    public List<CategoriesVo> listAllCategory() {
        LambdaQueryWrapper<Category>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Category::getStatus,SystemConstants.STATUS_NORMAL);
        List<Category> categories = this.list(lambdaQueryWrapper);
        List<CategoriesVo> categoriesVos = BeanCopyUtils.copyBeanList(categories, CategoriesVo.class);
        return categoriesVos;
    }

    @Override
    public ResponseResult pageList(Integer pageNum, Integer pageSize, String name, String status) {
        //查询封装 简单
        LambdaQueryWrapper<Category>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(name),Category::getName,name);
        lambdaQueryWrapper.eq(StringUtils.hasText(status),Category::getStatus,status);
        Page<Category>page=new Page<>(pageNum,pageSize);
        this.page(page,lambdaQueryWrapper);
        return ResponseResult.okResult(new PageVo(BeanCopyUtils.copyBeanList(page.getRecords(), CategoryPageVo.class),page.getTotal()));
    }

    @Override
    public ResponseResult addCategory(CategoryDto categoryDto) {
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        this.save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCategoryById(Long id) {
        return ResponseResult.okResult(BeanCopyUtils.copyBean(this.getById(id),CategoryPageVo.class));
    }

    @Override
    public ResponseResult updateCategory(CategoryUpdateDto categoryUpdateDto) {
        //先封装回category
        Category category = BeanCopyUtils.copyBean(categoryUpdateDto, Category.class);
        this.updateById(category);
        return ResponseResult.okResult();
    }
}




