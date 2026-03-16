package com.hxw.wscrm.pages.controller;

import com.hxw.wscrm.common.constant.SystemConstant;
import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.pages.entity.TbCustomer;
import com.hxw.wscrm.pages.model.TbCustomerQueryDTO;
import com.hxw.wscrm.pages.model.TbOrederQueryDTO;
import com.hxw.wscrm.pages.service.ITbCustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author 小贺
 * @since 2025-09-19
 */
@RestController
@RequestMapping("/pages/tbCustomer")
@Api(tags = "客户管理", value = "Customer")
public class TbCustomerController {
    @Autowired
    private ITbCustomerService tbCustomerService;

    @ApiOperation(value = "分页查询我的客户", notes = "分页查询我的客户")
    @GetMapping("/my_list")
    @PreAuthorize("hasAuthority('customer:query')")
    public PageUtils getMyCustomerList(@ApiParam(value = "查询条件") TbCustomerQueryDTO queryDTO) {
        return tbCustomerService.getMyCustomerList(queryDTO);
    }

    @ApiOperation(value = "分页查询公海客户", notes = "分页查询公海客户")
    @GetMapping("/pulic_list")
    @PreAuthorize("hasAuthority('customer:query')")
    public PageUtils getPublicCustomerList(@ApiParam(value = "查询条件") TbCustomerQueryDTO queryDTO) {
        return tbCustomerService.getPublicCustomerList(queryDTO);
    }

    @ApiOperation(value = "添加客户", notes = "添加客户信息")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('customer:add')")
    public Map<String, Object> save(@RequestBody TbCustomer customer) {
        tbCustomerService.saveCustomer(customer);
        Map<String, Object> result = new HashMap<>();
        result.put("code", SystemConstant.CHECK_SUCCESS);
        result.put("data", customer);
        return result;
    }

    @ApiOperation(value = "编辑客户", notes = "编辑客户信息")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('customer:update')")
    public String update(@RequestBody TbCustomer customer) {
        tbCustomerService.updateCustomer(customer);
        return SystemConstant.CHECK_SUCCESS;
    }

    @ApiOperation(value = "客户详情", notes = "根据ID查询客户详情")
    @GetMapping("/detail")
    @PreAuthorize("hasAuthority('customer:query')")
    public TbCustomer detail(@ApiParam(value = "客户ID") Long customerId) {
        return tbCustomerService.getCustomerDetail(customerId);
    }

    @ApiOperation(value = "删除客户", notes = "根据ID删除客户")
    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('customer:delete')")
    public String delete(@ApiParam(value = "客户ID") Long customerId) {
        tbCustomerService.deleteCustomer(customerId);
        return SystemConstant.CHECK_SUCCESS;
    }

    @ApiOperation(value = "客户转入公海", notes = "将客户转入公海池")
    @PostMapping("/toPublic")
    @PreAuthorize("hasAuthority('customer:add')")
    public String toPublic(@RequestParam(value = "customerId") @ApiParam(value = "客户ID") Long customerId) {
        tbCustomerService.toPublicCustomer(customerId);
        return SystemConstant.CHECK_SUCCESS;
    }

    @ApiOperation(value = "领取客户", notes = "从公海池领取客户")
    @PostMapping("/receive")
    @PreAuthorize("hasAuthority('customer:update')")
    public String receive(@RequestParam("customerId") Long customerId, @RequestParam(value = "userId", required = false) Long userId) {
        tbCustomerService.receiveCustomer(customerId, userId);
        return SystemConstant.CHECK_SUCCESS;
    }

    @ApiOperation(value = "检查重复客户", notes = "通过公司名称和联系电话检查重复客户")
    @PostMapping("/checkDuplicate")
    @PreAuthorize("hasAuthority('customer:query')")
    public List<TbCustomer> checkDuplicate(@RequestBody Map<String, String> params) {
        String customerName = params.get("customerName");
        String contactPhone = params.get("contactPhone");
        return tbCustomerService.checkDuplicate(customerName, contactPhone);
    }

    @ApiOperation(value = "合并客户", notes = "合并重复客户，将数据关联至主客户")
    @PostMapping("/merge")
    @PreAuthorize("hasAuthority('customer:update')")
    public String merge(@RequestBody Map<String, Object> params) {
        Long mainCustomerId = Long.valueOf(params.get("mainCustomerId").toString());
        // 处理前端发送的duplicateCustomerIds数据
        List<Long> duplicateCustomerIds = new ArrayList<>();
        Object duplicateIdsObj = params.get("duplicateCustomerIds");
        if (duplicateIdsObj instanceof List) {
            List<?> list = (List<?>) duplicateIdsObj;
            for (Object item : list) {
                if (item instanceof Number) {
                    duplicateCustomerIds.add(((Number) item).longValue());
                } else if (item instanceof String) {
                    duplicateCustomerIds.add(Long.valueOf((String) item));
                }
            }
        }
        tbCustomerService.mergeCustomers(mainCustomerId, duplicateCustomerIds);
        return SystemConstant.CHECK_SUCCESS;
    }
}
