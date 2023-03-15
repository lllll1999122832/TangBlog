package Tang.service;

import Tang.pojo.ResponseResult;
import Tang.pojo.User;
import Tang.pojo.dto.AddUserDto;
import Tang.pojo.dto.UpdateUserDto;
import Tang.pojo.dto.UserDto;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2023-03-07 17:57:59
*/
public interface UserService extends IService<User> {

//    ResponseResult login(User user);

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult getUserList(Integer pageNum, Integer pageSize, UserDto userDto);

    ResponseResult addUser(AddUserDto addUserDto);

    ResponseResult deleteUser(Long id);

    ResponseResult getUserRole(Long id);

    ResponseResult updateUser(UpdateUserDto updateUserDto);
}
