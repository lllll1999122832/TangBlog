package Tang.controller;

import Tang.pojo.Menu;
import Tang.pojo.ResponseResult;
import Tang.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    MenuService menuService;

    @GetMapping("/list")
    public ResponseResult menuList(){
        return menuService.menuList();
    }
    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        menuService.save(menu);
        return ResponseResult.okResult();
    }
    @GetMapping("/{id}")
    public ResponseResult getMenuById(@PathVariable("id")Long id){
        return  ResponseResult.okResult(menuService.getById(id));
    }
    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteMenu(@PathVariable("id")Long id){
        return menuService.deleteMenu(id);
    }
    @GetMapping("/tree/select")
    public ResponseResult treeSelect(){
        return menuService.treeSelect();
    }
    @GetMapping("/tree/select/{id}")
    public ResponseResult treeSelectById(@PathVariable("id")Long id){
        return menuService.treeSelectById(id);
    }

}
