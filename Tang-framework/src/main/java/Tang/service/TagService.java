package Tang.service;

import Tang.pojo.ResponseResult;
import Tang.pojo.Tag;
import Tang.pojo.dto.TagDto;
import Tang.pojo.dto.TagListDto;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sg_tag(标签)】的数据库操作Service
* @createDate 2023-03-11 16:14:52
*/
public interface TagService extends IService<Tag> {

    ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(TagListDto tagListDto);

    ResponseResult deleteTag(Long id);

    ResponseResult getTag(Long id);

    ResponseResult updateTag(TagDto tagDto);

    ResponseResult getAllTag();
}
