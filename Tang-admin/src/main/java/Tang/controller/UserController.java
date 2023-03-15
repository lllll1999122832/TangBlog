package Tang.controller;

import Tang.pojo.ResponseResult;
import Tang.pojo.dto.AddUserDto;
import Tang.pojo.dto.UpdateUserDto;
import Tang.pojo.dto.UserDto;
import Tang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseResult getUserList(Integer pageNum, Integer pageSize, UserDto userDto){
        return userService.getUserList(pageNum,pageSize,userDto);
    }
    @PostMapping
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto){
        return userService.addUser(addUserDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable("id") Long id){
        return userService.deleteUser(id);
    }
    @GetMapping("/{id}")
    public ResponseResult getUserRole(@PathVariable("id") Long id){
        return userService.getUserRole(id);
    }
    @PutMapping
    public ResponseResult updateUser(@RequestBody UpdateUserDto updateUserDto){
        return userService.updateUser(updateUserDto);
    }
}
