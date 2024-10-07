package mmc.warehousemanagementsystem.dto;

import lombok.Data;

/**
 * @author Dantence
 * @date 2022/9/7
 */
@Data
public class ResultDTO<T> {
    private Integer code;
    private String msg;
    private T data;
}
