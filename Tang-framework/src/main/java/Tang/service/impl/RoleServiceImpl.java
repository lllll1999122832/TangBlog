package Tang.service.impl;

import Tang.mapper.RoleMapper;
import Tang.pojo.ResponseResult;
import Tang.pojo.Role;
import Tang.pojo.RoleMenu;
import Tang.pojo.dto.*;
import Tang.service.RoleMenuService;
import Tang.service.RoleService;
import Tang.utils.BeanCopyUtils;
import Tang.vo.GetRoleVo;
import Tang.vo.PageVo;
import Tang.vo.RoleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
* @createDate 2023-03-11 23:10:52
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService {
    @Autowired
    RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleByUserId(Long id) {
        //判断是不是管理员,假如是管理员就返回admin
        if(id==1L){
            List<String>role=new ArrayList<>();
            role.add("admin");
            return role;
        }else{
            //否者查询用户的信息
            return getBaseMapper().selectRoleByUserId(id);
        }
    }

    @Override
    public ResponseResult getList(Integer pageNum, Integer pageSize, RoleDto roleDto) {
        //进行查询
        LambdaQueryWrapper<Role>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(roleDto.getRoleName()),Role::getRoleName,roleDto.getRoleName());
        lambdaQueryWrapper.eq(StringUtils.hasText(roleDto.getStatus()),Role::getStatus,roleDto.getStatus());
        lambdaQueryWrapper.orderByAsc(Role::getRoleSort);
        Page<Role>page=new Page<>();
        this.page(page,lambdaQueryWrapper);
        return ResponseResult.okResult(new PageVo(BeanCopyUtils.copyBeanList(page.getRecords(),RoleVo.class),page.getTotal()));
    }

    /**
     * 更新role状态
     * @param changeStatus
     * @return
     */

    @Override
    public ResponseResult changeStatus(ChangeStatusRoleDto changeStatus) {
        //选定更新条件
        LambdaUpdateWrapper<Role>lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Role::getId,changeStatus.getRoleId());
        lambdaUpdateWrapper.set(Role::getStatus,changeStatus.getStatus());
        this.update(lambdaUpdateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addRole(AddRoleDto addRoleDto) {
        //先把存角色信息
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        this.save(role);
        //把角色和和权限关系保存一下
        List<Long> menuIds = addRoleDto.getMenuIds();
        menuIds.stream().forEach(menuId->roleMenuService.save(new RoleMenu(role.getId(),menuId)));
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRole(Long id) {
        //查询到角色
        Role role = this.getById(id);
        //进行封装
        GetRoleVo getRoleVo = BeanCopyUtils.copyBean(role, GetRoleVo.class);
        return ResponseResult.okResult(getRoleVo);
    }

    @Override
    public ResponseResult updateRole(RoleUpdateDto roleUpdateDto) {
        //先更新角色信息
        Role role = BeanCopyUtils.copyBean(roleUpdateDto, Role.class);
        this.updateById(role);
        //再更新所有的权限信息
        //先删除原来的,再保存
        LambdaUpdateWrapper<RoleMenu>lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(RoleMenu::getRoleId,roleUpdateDto.getId());
        roleMenuService.remove(lambdaUpdateWrapper);
        List<Long> menuIds = roleUpdateDto.getMenuIds();
        //填写好,现在的
        menuIds.stream().forEach(o->roleMenuService.save(new RoleMenu(role.getId(),o)));
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRole(Long id) {
        //先删除角色
        this.removeById(id);
        //再删除用户拥有的权限
        LambdaUpdateWrapper<RoleMenu>lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(RoleMenu::getRoleId,id);
        roleMenuService.remove(lambdaUpdateWrapper);
        return ResponseResult.okResult();
    }
}




