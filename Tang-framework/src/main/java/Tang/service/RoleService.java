package Tang.service;

import Tang.pojo.ResponseResult;
import Tang.pojo.Role;
import Tang.pojo.dto.AddRoleDto;
import Tang.pojo.dto.ChangeStatusRoleDto;
import Tang.pojo.dto.RoleDto;
import Tang.pojo.dto.RoleUpdateDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sys_role(角色信息表)】的数据库操作Service
* @createDate 2023-03-11 23:10:52
*/
public interface RoleService extends IService<Role> {

    List<String> selectRoleByUserId(Long id);

    ResponseResult getList(Integer pageNum, Integer pageSize, RoleDto roleDto);

    ResponseResult changeStatus(ChangeStatusRoleDto changeStatus);

    ResponseResult addRole(AddRoleDto addRoleDto);

    ResponseResult getRole(Long id);

    ResponseResult updateRole(RoleUpdateDto roleUpdateDto);

    ResponseResult deleteRole(Long id);
}
