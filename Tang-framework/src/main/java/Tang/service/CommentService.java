package Tang.service;

import Tang.pojo.Comment;
import Tang.pojo.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sg_comment(评论表)】的数据库操作Service
* @createDate 2023-03-08 16:52:16
*/
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType,Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
