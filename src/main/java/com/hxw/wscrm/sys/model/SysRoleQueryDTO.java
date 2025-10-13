package com.hxw.wscrm.sys.model;


import com.hxw.wscrm.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class SysRoleQueryDTO extends PageDTO {
    @ApiModelProperty("角色名称")
    private String roleName;
}
