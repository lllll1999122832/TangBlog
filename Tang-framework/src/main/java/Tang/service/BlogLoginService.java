package Tang.service;

import Tang.pojo.ResponseResult;
import Tang.pojo.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
