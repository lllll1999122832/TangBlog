package Tang.service.impl;

import Tang.constants.SystemConstants;
import Tang.enums.AppHttpCodeEnum;
import Tang.exception.SystemException;
import Tang.mapper.CommentMapper;
import Tang.pojo.Comment;
import Tang.pojo.ResponseResult;
import Tang.service.CommentService;
import Tang.service.UserService;
import Tang.utils.BeanCopyUtils;
import Tang.utils.SecurityUtils;
import Tang.vo.CommentVo;
import Tang.vo.PageVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sg_comment(评论表)】的数据库操作Service实现
* @createDate 2023-03-08 16:52:16
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService {
    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(String commentType,Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论
        LambdaQueryWrapper<Comment>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        //对articleId进行判断
        lambdaQueryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,articleId);
        //对articleId进行判断
        lambdaQueryWrapper.eq(Comment::getRootId,-1);
        //对时间进行排序
        lambdaQueryWrapper.orderByDesc(Comment::getCreateTime);
        //查询评论类型
        lambdaQueryWrapper.eq(Comment::getType,commentType);
        //分页查询
        Page<Comment>page=new Page<>(pageNum,pageSize);
        page(page, lambdaQueryWrapper);
        List<CommentVo>commentVoList= toCommentVoList(page.getRecords());
        //查询所有根评论对应的子评论,并对其进行赋值
        commentVoList = commentVoList.stream().map(o -> o.setChildren(getChildren(o.getId(), articleId)))
                .collect(Collectors.toList());
        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        if(!StringUtils.hasText(comment.getContent())){
            //评论内容为空
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
//        comment.setCreateBy(SecurityUtils.getUserId());
        //因为设置了mybatis的自动填充所以 不需要自己填充时间了和创建人
        save(comment);
        return ResponseResult.okResult();
    }

    public List<CommentVo> toCommentVoList(List<Comment>list){
        //对应查询
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //查询评论用户id
        //Nick昵称
        commentVos = commentVos.stream().map(o -> o.setUsername(userService.getById(o.getCreateBy()).getNickName()))
                //查询回复用户的id
                //如果toCommentUserId不为-1才可以查询到
                .map(o -> o.getToCommentUserId() != -1 ? o.setToCommentUserName(userService.getById(o.getToCommentUserId()).getNickName()) : o)
                .collect(Collectors.toList());
        return commentVos;
    }

    /**
     * 根据rootId查询子评论的集合
     * @param rootId
     * @return
     */
    public List<CommentVo> getChildren(Long rootId,Long articleId){
        //查询子类集合
        LambdaQueryWrapper<Comment>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(articleId!=null,Comment::getArticleId,articleId);
        lambdaQueryWrapper.eq(Comment::getRootId,rootId);
        //时间排序
        lambdaQueryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> list = this.list(lambdaQueryWrapper);
        //把子类进行封装成Vo
        List<CommentVo> commentVos = toCommentVoList(list);
        return commentVos;
    }
}




