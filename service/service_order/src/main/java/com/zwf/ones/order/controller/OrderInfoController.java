package com.zwf.ones.order.controller;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zwf.ones.model.order.OrderInfo;
import com.zwf.ones.order.service.OrderInfoService;
import com.zwf.ones.vo.order.OrderInfoQueryVo;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import com.zwf.ones.result.Result;
import java.util.Map;

/**
 * <p>
 * 订单表 订单表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-03-24
 */
@Api(tags = "订单管理")
@RestController
@RequestMapping("/admin/order/orderInfo")

public class OrderInfoController {

    @Resource
    private OrderInfoService orderInfoService;


    @GetMapping("{page}/{limit}")
    public Result listOrder(@PathVariable Long page,
                            @PathVariable Long limit,
                            OrderInfoQueryVo orderInfoQueryVo){

        Page<OrderInfo> pageParam=new Page<>(page,limit);

        Map<String,Object> map=orderInfoService.selectOrderInfoPage(pageParam,orderInfoQueryVo);

        return Result.ok(map);
    }


}

