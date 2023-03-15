package Tang.controller;

import Tang.constants.SystemConstants;
import Tang.pojo.Comment;
import Tang.pojo.ResponseResult;
import Tang.pojo.dto.AddCommentDto;
import Tang.service.CommentService;
import Tang.utils.BeanCopyUtils;
import com.github.benmanes.caffeine.cache.Cache;
import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import org.apiguardian.api.API;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论",description = "这个是关于如何建设富强文明和谐的社会主义强国评论")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    @ApiOperation(value = "just soso",notes = "获取所有的评论")
    public ResponseResult commentList(Long articleId, Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }
    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto comment){
        Comment bean = BeanCopyUtils.copyBean(comment, Comment.class);
        return commentService.addComment(bean);
    }
    @GetMapping("/linkCommentList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页码"),
            @ApiImplicitParam(name = "pageSize", value = "页面大小")
    })
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }
}
