package com.hxw.wscrm.pages.model;

import com.hxw.wscrm.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TbCustomerQueryDTO extends PageDTO {

    @ApiModelProperty("客户名称")
    private String customerName;
    @ApiModelProperty("电话")
    private String phone;
    @ApiModelProperty("状态")
    private Integer status;
}
