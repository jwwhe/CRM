package com.hxw.wscrm.pages.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@TableName("tb_order")
@ApiModel(value = "TbOrder对象", description = "")
public class TbOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单id")
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("客户ID")
    private Long customerId;

    @ApiModelProperty("订单金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("订单状态")
    private Integer status;

    @ApiModelProperty("下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("支付方式")
    private String paymentMethod;

    @ApiModelProperty("地址")
    private String shippingAddress;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户名")
    @TableField(exist = false)
    private String userName;

    @ApiModelProperty("联系电话")
    private String customerPhone;

    @ApiModelProperty("关联合同ID")
    private Long contractId;

    @ApiModelProperty("物流公司")
    private String logisticsCompany;

    @ApiModelProperty("物流单号")
    private String logisticsNo;

    @ApiModelProperty("发货状态")
    private String deliveryStatus;

    @ApiModelProperty("支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    @ApiModelProperty("优惠金额")
    private BigDecimal discountAmount;

    @ApiModelProperty("订单产品列表")
    @TableField(exist = false)
    private List<TbOrderProduct> productList;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public LocalDateTime getPayTime() {
        return payTime;
    }

    public void setPayTime(LocalDateTime payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public List<TbOrderProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<TbOrderProduct> productList) {
        this.productList = productList;
    }

    @Override
    public String toString() {
        return "TbOrder{" +
                "orderId=" + orderId +
                ", orderNumber='" + orderNumber + '\'' +
                ", customerName='" + customerName + '\'' +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", createTime=" + createTime +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", remark='" + remark + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
