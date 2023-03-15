package Tang.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuTreeByIdVo {
    private List<MenuTreeVo> menus;
    //跟权限关联的id
    private Long checkedKeys;
}
