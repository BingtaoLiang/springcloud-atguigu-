package com.neo.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * @Author : neo
 * @Date 2021/11/3 8:57
 * @Description : TODO
 */
public interface LoadBalancer {
    ServiceInstance instances (List<ServiceInstance> serviceInstances);
}
