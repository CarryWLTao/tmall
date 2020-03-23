package com.tmall.order.client;/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:AddressClient
 * Author:  Administrator
 * Date:    2020-03-17 10:23
 * Description: 模拟收件人地址
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */

import com.tmall.order.dto.AddressDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:AddressClient
 * Author:  Administrator
 * Date:    2020-03-17 10:23
 * Description: 模拟收件人地址
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
public class AddressClient {
    public static final List<AddressDTO> addressList = new ArrayList<AddressDTO>() {
        {
            AddressDTO address = new AddressDTO();
            address.setId(1L);
            address.setAddress("新泺大街颖秀路1299号");
            address.setCity("济南");
            address.setDistrict("高新区");
            address.setName("小哥");
            address.setPhone("18766666666");
            address.setState("济南");
            address.setZipCode("000000");
            address.setIsDefault(true);
            add(address);
            AddressDTO address2 = new AddressDTO();
            address2.setId(1L);
            address2.setAddress("潘家园颖秀路1299号");
            address2.setCity("北京");
            address2.setDistrict("高新区");
            address2.setName("大哥");
            address2.setPhone("13966666666");
            address2.setState("北京");
            address2.setZipCode("000000");
            address2.setIsDefault(true);
            add(address2);
        }
    };
    public static AddressDTO findById(Long id){
        for (AddressDTO addressDTO : addressList){
            if (addressDTO.getId() == id)
                return addressDTO;
        }
        return  null;
    }
}
