 package com.hxw.wscrm.pages.model;

import com.hxw.wscrm.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TbCustomerQueryDTO extends PageDTO {

    @ApiModelProperty("客户名称")
    private String customerName;
    @ApiModelProperty("联系电话")
    private String contactPhone;
    @ApiModelProperty("客户状态")
    private String customerStatus;
}
