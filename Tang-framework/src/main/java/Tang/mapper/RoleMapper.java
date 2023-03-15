package Tang.mapper;

import Tang.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sys_role(角色信息表)】的数据库操作Mapper
* @createDate 2023-03-11 23:10:52
* @Entity .pojo.Role
*/
@Mapper
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleByUserId(Long id);
}




