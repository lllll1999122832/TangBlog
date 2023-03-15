package Tang.controller;

import Tang.enums.AppHttpCodeEnum;
import Tang.pojo.Category;
import Tang.pojo.ResponseResult;
import Tang.pojo.dto.CategoryDto;
import Tang.pojo.dto.CategoryUpdateDto;
import Tang.service.CategoryService;
import Tang.utils.BeanCopyUtils;
import Tang.utils.WebUtils;
import Tang.vo.CategoriesVo;
import Tang.vo.ExcelCategoryVo;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson.JSON;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<CategoriesVo> categoriesVos = categoryService.listAllCategory();
        return ResponseResult.okResult(categoriesVos);
    }
    @GetMapping("list")
    public ResponseResult pageList(Integer pageNum,Integer pageSize,String name,String status){
        return categoryService.pageList(pageNum,pageSize,name,status);
    }
    @GetMapping("/export")
    @PreAuthorize("@ps.hasPermission('content:category:export')") //从spring容器中找到ps容器进行判断
    public void export(HttpServletResponse response) throws IOException {
        try {
            //设置文件下载的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //设置需要导出的数据
            List<Category> categories = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categories, ExcelCategoryVo.class);
            //把数据写入Excel
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            e.printStackTrace();
            response.reset();
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
    @PostMapping
    public ResponseResult addCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.addCategory(categoryDto);
    }
    @GetMapping("{id}")
    public ResponseResult getCategoryById(@PathVariable("id")Long id){
        return categoryService.getCategoryById(id);
    }
    @PutMapping
    public ResponseResult updateCategory(@RequestBody CategoryUpdateDto categoryUpdateDto){
        return categoryService.updateCategory(categoryUpdateDto);
    }
    @DeleteMapping("{id}")
    public ResponseResult deleteCategory(@PathVariable("id")Long id){
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }
}
