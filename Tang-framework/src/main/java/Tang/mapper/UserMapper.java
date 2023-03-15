package Tang.mapper;

import Tang.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2023-03-07 17:57:59
* @Entity .pojo.User
*/
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

}




