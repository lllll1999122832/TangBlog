package Tang.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {

    private Long id;


    private String username;
//    private String userName;

    private String nickName;

//    private String password;

//    private String type;

    private String status;

    private String email;

    private String phonenumber;

    private String sex;

    private String avatar;

//    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;

}
