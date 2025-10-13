package com.hxw.wscrm.sys.model;


import com.hxw.wscrm.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class SysLogQueryDTO extends PageDTO {
    @ApiModelProperty("查询字段")
    private String msg;
}
