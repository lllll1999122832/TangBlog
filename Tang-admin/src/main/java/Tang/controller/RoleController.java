package Tang.controller;

import Tang.pojo.ResponseResult;
import Tang.pojo.Role;
import Tang.pojo.dto.AddRoleDto;
import Tang.pojo.dto.ChangeStatusRoleDto;
import Tang.pojo.dto.RoleDto;
import Tang.pojo.dto.RoleUpdateDto;
import Tang.service.RoleService;
import io.swagger.models.auth.In;
import org.apache.commons.collections4.Put;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    RoleService roleService;
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return ResponseResult.okResult(roleService.list());
    }
    @GetMapping("/list")
    public ResponseResult getList(Integer pageNum, Integer pageSize, RoleDto roleDto){
        return roleService.getList(pageNum,pageSize,roleDto);
    }
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeStatusRoleDto changeStatus){
        return roleService.changeStatus(changeStatus);
    }
    @PostMapping
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.addRole(addRoleDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getRole(@PathVariable("id")Long id){
        return roleService.getRole(id);
    }
    @PutMapping
    public ResponseResult updateRole(@RequestBody RoleUpdateDto roleUpdateDto){
        return roleService.updateRole(roleUpdateDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable("id")Long id){
        return roleService.deleteRole(id);
    }

}
