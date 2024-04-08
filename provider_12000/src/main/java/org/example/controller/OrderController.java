package org.example.controller;

import org.example.entity.Order;
import org.example.entity.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    @GetMapping("/getByUserId")
    public Result<Order>getByUserId(@RequestParam("userId") Integer userId){
        Order order=new Order();
        order.setId(1111);
        order.setUserId(userId);
        return Result.success(order);
    }
}
