package Tang.service.impl;

import Tang.enums.AppHttpCodeEnum;
import Tang.exception.SystemException;
import Tang.mapper.UserMapper;
import Tang.pojo.ResponseResult;
import Tang.pojo.Role;
import Tang.pojo.User;
import Tang.pojo.UserRole;
import Tang.pojo.dto.AddUserDto;
import Tang.pojo.dto.UpdateUserDto;
import Tang.pojo.dto.UserDto;
import Tang.service.RoleService;
import Tang.service.UserRoleService;
import Tang.service.UserService;
import Tang.utils.BeanCopyUtils;
import Tang.utils.SecurityUtils;
import Tang.vo.IdUserRoleVo;
import Tang.vo.PageVo;
import Tang.vo.UserInfoVo;
import Tang.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2023-03-07 17:57:59
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;

    @Override
    public ResponseResult userInfo() {
        //获取当前UserId
        Long userId = SecurityUtils.getUserId();
        //查询用户信息
        User user = getById(userId);
        //封装成userInfo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUsername())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        //对数据进行是否存在判断
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICK_EXIST);
        }
        if(userNameExist(user.getUsername())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //对密码进行加密处理
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        //存入数据库
        this.save(user);
        return ResponseResult.okResult();
    }

    /**
     * 查询数据库用户列表
     * @param pageNum
     * @param pageSize
     * @param userDto
     * @return
     */
    @Override
    public ResponseResult getUserList(Integer pageNum, Integer pageSize, UserDto userDto) {
        //先按要求,加入查询条件
        LambdaQueryWrapper<User>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(!Objects.isNull(userDto.getPhonenumber()),User::getPhonenumber,userDto.getPhonenumber());
        lambdaQueryWrapper.eq(!Objects.isNull(userDto.getUsername()),User::getUsername,userDto.getUsername());
        lambdaQueryWrapper.eq(!Objects.isNull(userDto.getStatus()),User::getStatus,userDto.getStatus());
        //分页
        Page<User>page=new Page<>(pageNum,pageSize);
        this.page(page,lambdaQueryWrapper);
        return ResponseResult.okResult(new PageVo(BeanCopyUtils.copyBeanList(page.getRecords(), UserVo.class),page.getTotal()));
    }

    @Override
    public ResponseResult addUser(AddUserDto addUserDto) {
        //先更新用户信息
        //密码要加密
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.save(user);
        //再更新用户表与角色对应
        addUserDto.getRoleIds().stream().forEach(o->userRoleService.save(new UserRole(user.getId(),o)));
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteUser(Long id) {
        //判断是否为当前用户
       if(SecurityUtils.getUserId().equals(id)){
           return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"无法删除当前登录的用户");
       }
       //删除用户
       this.removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserRole(Long id) {
        //查找用户拥有的角色id
        LambdaQueryWrapper<UserRole>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserRole::getUserId,id);
        List<UserRole> userRoles = userRoleService.list(lambdaQueryWrapper);
        List<Long> roleIds = userRoles.stream().map(o -> o.getRoleId()).collect(Collectors.toList());
        //查找用户的角色信息
        List<Role> roles = roleService.listByIds(roleIds);
        //查找用户信息
        User user = this.getById(id);
        //转换类型
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(new IdUserRoleVo(roleIds,roles,userInfoVo));
    }

    @Override
    public ResponseResult updateUser(UpdateUserDto updateUserDto) {
        //先删除用户的角色信息
       LambdaUpdateWrapper<UserRole> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
       lambdaUpdateWrapper.eq(UserRole::getUserId,updateUserDto.getId());
       userRoleService.remove(lambdaUpdateWrapper);
       //在添加用户信息
        updateUserDto.getRoleIds().stream().forEach(o->userRoleService.save(new UserRole(updateUserDto.getId(),o)));
        //更新用户信息
        User user = BeanCopyUtils.copyBean(updateUserDto, User.class);
        this.updateById(user);
        return ResponseResult.okResult();
    }

    /**
     * 用户名是否存在
     * @param userName
     * @return
     */
    public boolean userNameExist(String userName){
        LambdaQueryWrapper<User>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername,userName);
         return this.count(lambdaQueryWrapper)>0;
    }

    /**
     * 邮箱是否存在
     * @param email
     * @return
     */
    public boolean emailExist(String email){
        LambdaQueryWrapper<User>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getEmail,email);
        return this.count(lambdaQueryWrapper)>0;
    }
    public boolean nickNameExist(String nickName){
        LambdaQueryWrapper<User>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getNickName,nickName);
        return this.count(lambdaQueryWrapper)>0;
    }
}




