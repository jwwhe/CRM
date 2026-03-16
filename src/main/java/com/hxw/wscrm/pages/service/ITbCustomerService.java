package com.hxw.wscrm.pages.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxw.wscrm.common.util.PageUtils;
import com.hxw.wscrm.pages.entity.TbCustomer;
import com.hxw.wscrm.pages.model.TbCustomerQueryDTO;
import com.hxw.wscrm.pages.model.TbOrederQueryDTO;

import java.util.List;

public interface ITbCustomerService extends IService<TbCustomer> {

    PageUtils getMyCustomerList(TbCustomerQueryDTO queryDTO);

    PageUtils getPublicCustomerList(TbCustomerQueryDTO queryDTO);

    void saveCustomer(TbCustomer customer);

    void updateCustomer(TbCustomer customer);

    TbCustomer getCustomerDetail(Long customerId);

    void deleteCustomer(Long customerId);

    void toPublicCustomer(Long customerId);

    void receiveCustomer(Long customerId);
    
    void receiveCustomer(Long customerId, Long userId);
    
    List<TbCustomer> checkDuplicate(String customerName, String contactPhone);
    
    void mergeCustomers(Long mainCustomerId, List<Long> duplicateCustomerIds);
}
