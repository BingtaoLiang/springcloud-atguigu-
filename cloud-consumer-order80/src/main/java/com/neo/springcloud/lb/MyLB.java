package com.neo.springcloud.lb;/**
 * @Author : neo
 * @Date 2021/11/3 9:00
 * @Description : TODO
 */

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 自定义实现ribbon负载均衡中的轮训策略
 * @author: neo
 * @time: 2021/11/3 9:00
 */
@Component
public class MyLB implements LoadBalancer {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    /***
     * @Author neo
     * @Description rest接口第几次请求数 % 服务器集群总数量 = 实际调用服务器位置下标，每次服务重启动后rest接口计数从1开始
     *               这个方法的功能就是获得当前是第几次请求
     * @Date 9:12 2021/11/3
     * @params []
     * @return int
     */
    public final int getAndIncrement() {
        int current;
        int next;
        do {                    //自旋锁
            current = this.atomicInteger.get();
            next = current >= 2147483647 ? 0 : current + 1;
            //选2147483617这个值是因为其实Integer的MaxValue
        } while (!this.atomicInteger.compareAndSet(current, next));  //CAS
        System.out.println("*******第几次访问next: " + next);    //next就是当前访问次数
        return next;
    }

    /**
     * @return org.springframework.cloud.client.ServiceInstance 返回的是根据当前负载均衡策略选中的要使用的服务器实例
     * @Author neo
     * @Description 这个方法就是对ribbon负载均衡中轮训算法的实现，
     * @Date 9:13 2021/11/3
     * @params [serviceInstances] 参数代表服务器集群中的所有实例
     */
    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {
        int index = getAndIncrement() % serviceInstances.size();    //选中的服务器实例下标
        return serviceInstances.get(index);
    }
}
