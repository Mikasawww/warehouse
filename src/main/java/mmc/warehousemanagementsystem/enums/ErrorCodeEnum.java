package mmc.warehousemanagementsystem.enums;

/**
 * @author Dantence
 * @date 2022/9/7
 */
public enum ErrorCodeEnum {
    SUCESS(200,"操作成功!"),
    REMOVE_WAREHOUSE_PERMISSION_DENIED(401,"您没有权限删除该仓库!"),
    REMOVE_WAREHOUSE_WAREHOUSE_NOT_EMPTY(402,"请先清空仓库再删除!"),
    SYSTEM_ERROR(403,"系统异常，请重试!"),
    USER_NOT_LOGIN(404,"用户未登录!")
    ;
    private final Integer code;
    private final String msg;

    public Integer getCode(){return code;}
    public String getMsg(){return msg;}
    ErrorCodeEnum(Integer code, String msg){
        this.msg = msg;
        this.code = code;
    }

}
