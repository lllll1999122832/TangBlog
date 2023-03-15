package Tang.mapper;

import Tang.pojo.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sg_tag(标签)】的数据库操作Mapper
* @createDate 2023-03-11 16:14:52
* @Entity .pojo.Tag
*/
@Mapper
@Repository
public interface TagMapper extends BaseMapper<Tag> {

}




