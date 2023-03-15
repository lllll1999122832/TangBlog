package Tang.service.impl;

import Tang.pojo.LoginUser;
import Tang.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service("ps")
public class PermissionService {
    /**
     * 判断当前用户是否具有permission
     * @param permission
     * @return
     */
    public boolean hasPermission(String permission){
        //如果是超级管理员,直接返回true
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if(SecurityUtils.isAdmin()){
            return true;
        }
        List<String> permissions = loginUser.getPermissions();
        //否者返回当前用户是否有该权限
        if(Objects.isNull(permissions)){
            return false;
        }
        return permissions.contains(permission);
    }
}
