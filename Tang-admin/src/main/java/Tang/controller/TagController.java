package Tang.controller;

import Tang.pojo.ResponseResult;
import Tang.pojo.Tag;
import Tang.pojo.dto.TagDto;
import Tang.pojo.dto.TagListDto;
import Tang.service.TagService;

import Tang.utils.BeanCopyUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content")
public class TagController {
    @Autowired
    private TagService tagService;
    @GetMapping("/tag/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }
    @PostMapping("/tag")
    public ResponseResult addTag(@RequestBody TagListDto tagListDto){
        return tagService.addTag(tagListDto);
    }
    @DeleteMapping("/tag/{id}")
    public ResponseResult deleteTag(@PathVariable("id") Long id){
        return tagService.deleteTag(id);
    }
    @GetMapping("/tag/{id}")
    public ResponseResult getTag(@PathVariable("id")Long id){
       return tagService.getTag(id);
    }
    @PutMapping("/tag")
    public ResponseResult updateTag(@RequestBody TagDto tagDto){
        return tagService.updateTag(tagDto);
    }
    @GetMapping("/listAllTag")
    public ResponseResult getAllTag(){
        return tagService.getAllTag();
    }
}
