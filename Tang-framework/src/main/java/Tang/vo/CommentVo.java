package Tang.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true) //设置set 时返回返回对象本身
public class CommentVo {
    //评论Id
    private Long id;
    private String username;

    private String type;

    private Long articleId;

    private Long rootId;

    private String content;

    private Long toCommentUserId;
    private String toCommentUserName;

    private Long toCommentId;

    //评论用户的id
    private Long createBy;

    private Date createTime;

    private Long updateBy;
    //子评论
    private List<CommentVo> children;

//    private Date updateTime;
//
//    private Integer delFlag;
}
