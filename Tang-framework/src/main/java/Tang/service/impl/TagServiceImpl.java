package Tang.service.impl;

import Tang.mapper.TagMapper;
import Tang.pojo.ResponseResult;
import Tang.pojo.Tag;
import Tang.pojo.dto.TagDto;
import Tang.pojo.dto.TagListDto;
import Tang.service.TagService;
import Tang.utils.BeanCopyUtils;
import Tang.vo.PageVo;
import Tang.vo.TagVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sg_tag(标签)】的数据库操作Service实现
* @createDate 2023-03-11 16:14:52
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService {

    @Override
    public ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //分页查询
        LambdaQueryWrapper<Tag>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        lambdaQueryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        Page<Tag>page=new Page<>(pageNum,pageSize);
        this.page(page,lambdaQueryWrapper);
        //封装数据查询
        return ResponseResult.okResult(new PageVo(page.getRecords(),page.getTotal()));
    }

    @Override
    public ResponseResult addTag(TagListDto tagListDto) {
        this.save(BeanCopyUtils.copyBean(tagListDto,Tag.class));
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(Long id) {
        this.removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTag(Long id) {
        //查询相应的tag
        Tag tag = this.getById(id);
        //封装返回
        return ResponseResult.okResult(BeanCopyUtils.copyBean(tag,TagVo.class));
    }

    @Override
    public ResponseResult updateTag(TagDto tagDto) {
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        this.updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getAllTag() {
        LambdaQueryWrapper<Tag>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(Tag::getId,Tag::getName);
        List<Tag> tags = this.list(lambdaQueryWrapper);
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(tags, TagVo.class);
        return ResponseResult.okResult(tags);
    }
}




