package com.hxw.wscrm.sys.model;

import com.hxw.wscrm.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysMenuQueryDTO extends PageDTO {
    //查询字段
    @ApiModelProperty("菜单描述")
    private String label;

}
