package com.hxw.wscrm.sys.model;


import com.hxw.wscrm.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysUserQueryDTO extends PageDTO {
    @ApiModelProperty("用户名称")
    private String username;
}
