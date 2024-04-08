package org.example.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Order;
import org.example.entity.Result;
import org.example.entity.User;
import org.example.entity.UserOrderDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private DiscoveryClient discoveryClient;
    @GetMapping("/getUserOrderById")
    public Result<UserOrderDTO>getUserOrderById(@RequestParam("userId") Integer userId){
        log.info("用户id:{}",userId);
        User user=new User();
        user.setName("王三");
        user.setId(userId);
        user.setAge(18);

        //通过服务提供者名获取服务注册列表；
        List<ServiceInstance> serviceInstanceList=discoveryClient.getInstances("provider");
        for (ServiceInstance s:serviceInstanceList
        ) {
            log.info("服务提供者ip:{} 端口号:{}",s.getHost(),s.getPort());
        }
        ServiceInstance serviceInstance=serviceInstanceList.get(0);
        Result<Order> result=(Result<Order>) restTemplate.getForObject("http://"+serviceInstance.getHost()+":"+serviceInstance.getPort()+"/order/getByUserId?userId="+userId,Result.class);
       // Result<Order> result=(Result<Order>) restTemplate.getForObject("http://localhost:11000/order/getByUserId?userId="+userId,Result.class);

        log.info("返回结果：{}",result);

        Map<String, Object> orderMap = (Map<String, Object>) result.getData();
        UserOrderDTO userOrderDTO=new UserOrderDTO();
        BeanUtils.copyProperties(user,userOrderDTO);
        userOrderDTO.setOrderId((Integer) orderMap.get("id"));
        userOrderDTO.setUserId((Integer) orderMap.get("userId"));


        return  Result.success(userOrderDTO,result.getMessage());
    }
}
