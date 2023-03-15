package Tang.service;

import Tang.pojo.Category;
import Tang.pojo.ResponseResult;
import Tang.pojo.dto.CategoryDto;
import Tang.pojo.dto.CategoryUpdateDto;
import Tang.vo.CategoriesVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 曾梦想仗剑走天涯
* @description 针对表【sg_category(分类表)】的数据库操作Service
* @createDate 2023-03-02 18:15:28
*/
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    List<CategoriesVo> listAllCategory();

    ResponseResult pageList(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addCategory(CategoryDto categoryDto);

    ResponseResult getCategoryById(Long id);

    ResponseResult updateCategory(CategoryUpdateDto categoryUpdateDto);
}
