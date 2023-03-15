package Tang.mapper;

import Tang.pojo.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sg_category(分类表)】的数据库操作Mapper
* @createDate 2023-03-02 18:15:28
* @Entity .pojo.Category
*/
@Mapper
@Repository
public interface CategoryMapper extends BaseMapper<Category> {

}




