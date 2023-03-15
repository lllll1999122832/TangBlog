package Tang.controller;

import Tang.pojo.Link;
import Tang.pojo.ResponseResult;
import Tang.pojo.dto.LinkAddDto;
import Tang.pojo.dto.LinkDto;
import Tang.service.LinkService;
import Tang.utils.BeanCopyUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    LinkService linkService;
    @GetMapping("/list")
    public ResponseResult getPageList(Integer pageNum,Integer pageSize,String name,String status){
        return linkService.getPageList(pageNum,pageSize,name,status);
    }
    @PostMapping
    public ResponseResult addLink(@RequestBody LinkDto linkDto){
        return linkService.addLink(linkDto);
    }
    @GetMapping("{id}")
    public ResponseResult getLink(@PathVariable("id")Long id){
        return linkService.getLink(id);
    }
    @PutMapping
    public ResponseResult updateLink(@RequestBody LinkAddDto linkAddDto){
        linkService.updateById( BeanCopyUtils.copyBean(linkAddDto, Link.class));
        return ResponseResult.okResult();
    }
    @DeleteMapping("{id}")
    public ResponseResult deleteLink(@PathVariable("id")Long id){
        linkService.removeById(id);
        return ResponseResult.okResult();
    }
}
