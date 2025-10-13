package com.hxw.wscrm.pages.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author 小贺
 * @since 2025-09-19
 */
@TableName("tb_customer")
@ApiModel(value = "TbCustomer对象", description = "")
public class TbCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("客户唯一 ID")
    @TableId(value = "customer_id", type = IdType.AUTO)
    private Long customerId;

    @ApiModelProperty("客户姓名")
    private String customerName;

    @ApiModelProperty("客户手机号")
    private String phone;

    @ApiModelProperty("客户所属公司")
    private String company;

    @ApiModelProperty("客户状态：1 - 正常合作，0 - 暂停合作")
    private Integer status;

    @ApiModelProperty("客户归属用户 ID")
    private Long userId;

    @ApiModelProperty("客户备注")
    private String remark;

    @ApiModelProperty("	客户创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("客户信息更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("进入公海时间：	- 个人客户转公海时赋值	- 公海客户直接新增时赋值	- 个人客户时为NULL")
    private LocalDateTime enterPublicTime;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    public LocalDateTime getEnterPublicTime() {
        return enterPublicTime;
    }

    public void setEnterPublicTime(LocalDateTime enterPublicTime) {
        this.enterPublicTime = enterPublicTime;
    }

    @Override
    public String toString() {
        return "TbCustomer{" +
            "customerId=" + customerId +
            ", customerName=" + customerName +
            ", phone=" + phone +
            ", company=" + company +
            ", status=" + status +
            ", userId=" + userId +
            ", remark=" + remark +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", enterPublicTime=" + enterPublicTime +
        "}";
    }
}
