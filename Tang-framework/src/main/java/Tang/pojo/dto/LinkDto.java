package Tang.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkDto {

    private String name;

    private String logo;

    private String description;

    private String address;

    private String status;
}
