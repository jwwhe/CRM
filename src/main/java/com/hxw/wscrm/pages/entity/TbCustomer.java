package com.hxw.wscrm.pages.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("tb_customer")
public class TbCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "customer_id", type = IdType.AUTO)
    private Long customerId;

    private String customerName;

    private String contactPerson;

    private String contactPhone;

    private String customerAddress;

    private String customerSource;

    private String customerLevel;

    private String customerStatus;

    private Long userId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
