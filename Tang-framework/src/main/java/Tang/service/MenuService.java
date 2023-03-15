package Tang.service;

import Tang.pojo.Menu;
import Tang.pojo.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service
* @createDate 2023-03-11 22:58:30
*/
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long id);

    ResponseResult menuList();

    ResponseResult updateMenu(Menu menu);

    ResponseResult deleteMenu(Long id);

    ResponseResult treeSelect();

    ResponseResult treeSelectById(Long id);
}
