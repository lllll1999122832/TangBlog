package Tang.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {


    private Long id;
    private String username;
    private String nickName;


    private String status;

    private String email;

    private String phonenumber;

    private String sex;

    private List<Long> roleIds;
}
