package Tang.service.impl;

import Tang.constants.SystemConstants;
import Tang.mapper.LinkMapper;
import Tang.pojo.Link;
import Tang.pojo.ResponseResult;
import Tang.pojo.dto.LinkDto;
import Tang.service.LinkService;
import Tang.utils.BeanCopyUtils;
import Tang.vo.LinkVo;
import Tang.vo.PageVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sg_link(友链)】的数据库操作Service实现
* @createDate 2023-03-04 11:17:38
*/
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link>
    implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        //查询所有审核通过的状态
        LambdaQueryWrapper<Link>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = this.list(lambdaQueryWrapper);
        //封装为vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult getPageList(Integer pageNum, Integer pageSize, String name, String status) {
        //先判断条件是否存在
        LambdaQueryWrapper<Link>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(name),Link::getName,name);
        lambdaQueryWrapper.eq(StringUtils.hasText(status),Link::getStatus,status);
        //分页
        Page<Link>page=new Page<>(pageNum,pageSize);
        //查询
        this.page(page,lambdaQueryWrapper);
        return ResponseResult.okResult(new PageVo(BeanCopyUtils.copyBeanList(page.getRecords(),LinkVo.class),page.getTotal()));
    }

    @Override
    public ResponseResult addLink(LinkDto linkDto) {
        //封装对象
        Link link = BeanCopyUtils.copyBean(linkDto, Link.class);
        //添加
        this.save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getLink(Long id) {
        Link link = this.getById(id);
        return ResponseResult.okResult(BeanCopyUtils.copyBean(link,LinkVo.class));
    }
}




