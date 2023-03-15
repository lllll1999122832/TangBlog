package Tang.mapper;



import Tang.pojo.Article;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sg_article(文章表)】的数据库操作Mapper
* @createDate 2023-03-01 19:00:47
* @Entity .pojo.Article
*/
@Mapper
@Repository
public interface ArticleMapper extends BaseMapper<Article> {
//    public IPage getArticleListVo(Page page,
//                                  @Param(Constants.WRAPPER) LambdaQueryWrapper queryWrapper);
}




