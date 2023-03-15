package Tang.mapper;

import Tang.pojo.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sg_comment(评论表)】的数据库操作Mapper
* @createDate 2023-03-08 16:52:16
* @Entity .pojo.Comment
*/
@Mapper
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

}




