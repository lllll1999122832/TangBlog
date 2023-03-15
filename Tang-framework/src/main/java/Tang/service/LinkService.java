package Tang.service;

import Tang.pojo.Link;
import Tang.pojo.ResponseResult;
import Tang.pojo.dto.LinkDto;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sg_link(友链)】的数据库操作Service
* @createDate 2023-03-04 11:17:38
*/
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult getPageList(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addLink(LinkDto linkDto);

    ResponseResult getLink(Long id);
}
