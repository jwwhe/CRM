package com.hxw.wscrm.pages.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    private Integer isPool;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime poolTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastFollowTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime nextFollowTime;

    @TableField(value = "user_id", updateStrategy = FieldStrategy.IGNORED)
    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
