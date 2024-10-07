package mmc.warehousemanagementsystem.dto;

import lombok.Data;

/**
 * @author Dantence
 * @date 2022/9/7
 */
@Data
public class WarehouseDTO {
    private Long id;
    private String creatorName;
    private Integer capacity;
    private Integer occupy;
    private Integer status;
    private Double rate;
}
