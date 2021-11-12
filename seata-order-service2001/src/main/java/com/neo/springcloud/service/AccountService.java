package com.neo.springcloud.service;/**
 * @Author : neo
 * @Date 2021/11/12 17:02
 * @Description : TODO
 */

import com.neo.springcloud.domain.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/12 17:02
 */
@FeignClient(value = "seata-account-service")
public interface AccountService {

    @PostMapping(value = "/account/decrease")
    CommonResult decrease(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money);
}
