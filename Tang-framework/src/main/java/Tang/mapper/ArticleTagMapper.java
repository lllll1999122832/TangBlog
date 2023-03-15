package Tang.mapper;

import Tang.pojo.ArticleTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sg_article_tag(文章标签关联表)】的数据库操作Mapper
* @createDate 2023-03-13 22:09:05
* @Entity .pojo.ArticleTag
*/
@Mapper
@Repository
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

}




