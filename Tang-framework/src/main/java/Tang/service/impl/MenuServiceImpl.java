package Tang.service.impl;

import Tang.constants.SystemConstants;
import Tang.enums.AppHttpCodeEnum;
import Tang.mapper.MenuMapper;
import Tang.pojo.Menu;
import Tang.pojo.ResponseResult;
import Tang.pojo.RoleMenu;
import Tang.service.MenuService;
import Tang.service.RoleMenuService;
import Tang.utils.BeanCopyUtils;
import Tang.vo.MenuTreeByIdVo;
import Tang.vo.MenuTreeVo;
import Tang.vo.MenuVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
* @createDate 2023-03-11 22:58:30
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService {
    @Autowired
    MenuMapper menuMapper;
    @Autowired
    RoleMenuService roleMenuService;
    @Override
    public List<String> selectPermsByUserId(Long id) {
        if(id.equals(1L)){
            LambdaQueryWrapper<Menu>lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            lambdaQueryWrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
//            List<Menu> menus = menuMapper.selectList(lambdaQueryWrapper);
            List<Menu> menus = menuMapper.selectList(lambdaQueryWrapper);
           return menus.stream().map(o->o.getPerms()).collect(Collectors.toList());
        }
        return  menuMapper.selectPermsByUserId(id);
    }



    @Override
    public ResponseResult menuList() {
        //先查询出所有的Menu
        LambdaQueryWrapper<Menu>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Menu::getOrderNum);
        List<Menu> menus = menuMapper.selectList(lambdaQueryWrapper);
        //封装数据
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        //先判断父菜单是不是自己的id
        if(menu.getId().equals(menu.getParentId())){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"修改菜单'写博文'失败，上级菜单不能选择自己");
        }
        menuMapper.updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long id) {
        //先判断是否存在子菜单
        LambdaQueryWrapper<Menu>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Menu::getParentId,id);
        List<Menu> menus = menuMapper.selectList(lambdaQueryWrapper);
        //如果存在就返回,就删除失败
        if(menus.size()>=1){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"存在子菜单不允许删除");
        }
        menuMapper.deleteById(id);
        return ResponseResult.okResult();
    }
    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long id) {
        MenuMapper baseMapper = getBaseMapper();
        //判断是否为管理员
        List<Menu>menus;
        if(id.equals(1L)){
            //如果是返回所有符合要求的menu
            menus=baseMapper.selectAllRouterMenu();
        }else{
            //否者返回符合当前用户符合所有要求的menu
            menus=baseMapper.selectRouterMenuTreeByUserId(id);
        }
        //先找第一层的菜单,然后在找他们的子菜单,设置到children中去
        List<Menu>menuTree=buildMenuTree(menus,0L);
        return menuTree;
    }

    @Override
    public ResponseResult treeSelect() {
        //先查询所有的权限
        List<Menu> menus = this.list();
        //封装menuVo,两个的标签名不一样
        List<MenuTreeVo>menuTreeVos=new ArrayList<>();
        for(int i=0;i<menus.size();i++){
            menuTreeVos.add(BeanCopyUtils.copyBean(menus.get(i), MenuTreeVo.class));
            menuTreeVos.set(i,menuTreeVos.get(i).setLabel(menus.get(i).getMenuName()));
        }
        //构造树形menu
         menuTreeVos = buildMenuTreeMore(menuTreeVos, 0L);
        return ResponseResult.okResult(menuTreeVos);
    }
    public List<MenuTreeVo> treeSelectAdmin(){
        List<Menu> menus = this.list();
        //封装menuVo,两个的标签名不一样
        List<MenuTreeVo>menuTreeVos=new ArrayList<>();
        for(int i=0;i<menus.size();i++){
            menuTreeVos.add(BeanCopyUtils.copyBean(menus.get(i), MenuTreeVo.class));
            menuTreeVos.set(i,menuTreeVos.get(i).setLabel(menus.get(i).getMenuName()));
        }
        //构造树形menu
        menuTreeVos = buildMenuTreeMore(menuTreeVos, 0L);
        return menuTreeVos;
    }

    /**
     * 根据id查询用户的权限,在进行树形封装
     * @param id
     * @return
     */
    @Override
    public ResponseResult treeSelectById(Long id) {
        List<MenuTreeVo>menuTreeVos=new ArrayList<>();
        if(id==1L){
            menuTreeVos=treeSelectAdmin();
            return ResponseResult.okResult(new MenuTreeByIdVo(menuTreeVos,id));
        }
        //先查找用户所有的权限
        LambdaQueryWrapper<RoleMenu>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RoleMenu::getRoleId,id);
        List<RoleMenu> roleMenus = roleMenuService.list(lambdaQueryWrapper);
        //封装出所有权限
        List<Menu> menus = roleMenus.stream().map(o -> this.getById(o.getMenuId()))
                .collect(Collectors.toList());
        //把Menu转换为MenuTreeVo
        for(int i=0;i<menus.size();i++){
            if(Objects.isNull(menus.get(i))){
                continue;
            }
            menuTreeVos.add(BeanCopyUtils.copyBean(menus.get(i), MenuTreeVo.class));
            menuTreeVos.set(menuTreeVos.size()-1,menuTreeVos.get(menuTreeVos.size()-1).setLabel(menus.get(i).getMenuName()));
        }
        //封装成树形
        menuTreeVos=buildMenuTreeMore(menuTreeVos,0L);
        //最后再次封装
        return ResponseResult.okResult(new MenuTreeByIdVo(menuTreeVos,id));
    }

    /**
     * 有多层遍历
     * @param menus
     * @param parentId
     * @return
     */
    private List<MenuTreeVo> buildMenuTreeMore(List<MenuTreeVo> menus, long parentId) {
        List<MenuTreeVo> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildrenMore(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    private List<MenuTreeVo> getChildrenMore(MenuTreeVo menuTreeVo, List<MenuTreeVo> menus) {
        List<MenuTreeVo> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menuTreeVo.getId()))
                //可以多层遍历获取children
                .map(m->m.setChildren(getChildrenMore(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }

    private List<Menu> buildMenuTree(List<Menu> menus,Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 获取Menu的子菜单
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                //可以多层遍历获取children
//                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}




