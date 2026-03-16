package com.hxw.wscrm.pages.model;

import com.hxw.wscrm.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TbOrederQueryDTO extends PageDTO {

    @ApiModelProperty("订单编号")
    private String orderNumber;
    @ApiModelProperty("客户名称")
    private String customerName;
    @ApiModelProperty("订单状态")
    private String status;
    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("合同ID")
    private Long contractId;

    @ApiModelProperty("客户ID")
    private Long customerId;
}
