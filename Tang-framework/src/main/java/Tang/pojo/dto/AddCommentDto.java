package Tang.pojo.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "添加评论dto")
public class AddCommentDto {
    @ApiModelProperty(notes = "评论id")
    private Long id;
@ApiModelProperty(notes = "评论类型")
    private String type;

    private Long articleId;

    private Long rootId;

    private String content;

    private Long toCommentUserId;

    private Long toCommentId;

    /**
     * 告诉mybatis plus在插入的时候需要填充
     */

    private Long createBy;
    private Date createTime;


    private Long updateBy;
    /**
     * 更新和创建的时候都需要填充
     */
    private Date updateTime;

    private Integer delFlag;

}
