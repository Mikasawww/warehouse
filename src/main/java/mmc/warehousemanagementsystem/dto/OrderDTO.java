package mmc.warehousemanagementsystem.dto;

import lombok.Data;

/**
 * @author Dantence
 * @date 2022/9/8
 */
@Data
public class OrderDTO {
    private Long id;
    private String materialName;
    private String adminName;
    private Integer quantity;
    private Integer sum;
    private String typeName;
    private Long gmtCreated;
}
