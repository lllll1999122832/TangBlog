package Tang.mapper;

import Tang.pojo.Link;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sg_link(友链)】的数据库操作Mapper
* @createDate 2023-03-04 11:17:38
* @Entity .pojo.Link
*/
@Mapper
@Repository
public interface LinkMapper extends BaseMapper<Link> {

}




