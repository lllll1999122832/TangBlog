package Tang.service;

import Tang.pojo.ResponseResult;
import Tang.pojo.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
