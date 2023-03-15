package Tang.service.impl;

import Tang.constants.SystemConstants;
import Tang.mapper.MenuMapper;
import Tang.mapper.UserMapper;
import Tang.pojo.LoginUser;
import Tang.pojo.User;
import Tang.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
   private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(User::getUserName,username);
        lambdaQueryWrapper.eq(User::getUsername,username);
        User user = userMapper.selectOne(lambdaQueryWrapper);
        //判断是否查到用户,如果没有查到抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在!");
        }
        //返回用户信息
        // TODO: 2023/3/7 查询用户权限信息
        //todo 只有后台用户才要权限封装
        if (user.getType().equals(SystemConstants.ADMIN)){
            List<String> list = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user,list);
        }
        //返回实现UserDetails的实现类
        return new LoginUser(user);
    }
}
