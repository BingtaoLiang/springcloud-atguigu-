package com.neo.controller;/**
 * @Author : neo
 * @Date 2021/11/10 16:57
 * @Description : TODO
 */

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/10 16:57
 */
@RestController
@Slf4j
public class FlowLimitController {

    @GetMapping("/testA")
    public String testA() {
        log.info(Thread.currentThread().getName() + "\t" + "...testA");
        return "------testA";
    }

    @GetMapping("/testB")
    public String testB() {
        // log.info(Thread.currentThread().getName() + "\t" + "...testB");
        return "------testB";
    }


    @GetMapping("/testD")
    public String testD() {
        //这里设置每个请求的处理时间为1秒钟，当Sentinel的RT小于这个处理时间，且QPS>=5就会触发降级 ，
       try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
       log.info("testD 测试RT");
        //
        // log.info("testD 异常比例");
        // int age = 10 / 0;
        return "------testD";
    }

    @GetMapping("/testE")
    public String testE() {
        log.info("testE 测试异常数");
        int age = 10 / 0;
        return "------testE 测试异常数";
    }

    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey", blockHandler = "deal_fuckingHotKey")
    public String testHotKey(@RequestParam(value = "p1", required = false) String p1,
                             @RequestParam(value = "p2", required = false) String p2) {
        // int age = 10/0;
        return "------testHotKey";
    }

    public String deal_fuckingHotKey(String p1, String p2, BlockException exception) {
        return "------deal_testHotKey,o(╥﹏╥)o";  //sentinel系统默认的提示：Blocked by Sentinel (flow limiting)
    }

}
