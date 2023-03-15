package Tang.vo;

import Tang.pojo.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdUserRoleVo {
    private List<Long>roleIds;
    private List<Role>roles;
    private UserInfoVo user;
}
