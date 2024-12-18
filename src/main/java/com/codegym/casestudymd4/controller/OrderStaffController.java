package com.codegym.casestudymd4.controller;

import com.codegym.casestudymd4.model.Customer;
import com.codegym.casestudymd4.model.Order;
import com.codegym.casestudymd4.model.OrderDetail;
import com.codegym.casestudymd4.model.Status;
import com.codegym.casestudymd4.service.implement.CustomerService;
import com.codegym.casestudymd4.service.implement.OrderDetailService;
import com.codegym.casestudymd4.service.implement.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/order_staff")
public class OrderStaffController {
    private final CustomerService customerService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    @Autowired
    public OrderStaffController(CustomerService customerService,
                                OrderService orderService,
                                OrderDetailService orderDetailService){
        this.customerService = customerService;
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
    }

    public String formatLocalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return localDateTime.format(formatter);
    }

    @GetMapping("/view")
    public ModelAndView showViewOrder(@RequestParam Long id){
        BigDecimal total = orderService.getTotalByOrderId(id);
        ModelAndView modelAndView = new ModelAndView("order_staff/view");
        List<OrderDetail> list = orderDetailService.getOrderDetailByOrderId(id);
        modelAndView.addObject("list", list);
        modelAndView.addObject("idOrder", id);
        modelAndView.addObject("total", total);
        return modelAndView;
    }

    @GetMapping("/list")
    public ModelAndView showListOrder(){
        ModelAndView modelAndView = new ModelAndView("order_staff/list");
        Iterable<Order> orderList = orderService.findAll();
        if (!orderList.iterator().hasNext()){
            modelAndView.addObject("errorMessage", "Không có đơn hàng nào trong hệ thống!");
            modelAndView.addObject("orderList",orderList);
            return modelAndView;
        }
        for (Order order : orderList){
            order.setTime(LocalDateTime.parse(formatLocalDateTime(order.getTime()), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            order.setFormattedTime(formatLocalDateTime(order.getTime()));
        }
        modelAndView.addObject("orderList",orderList);
        return modelAndView;
    }

    @GetMapping("/edit")
    public ModelAndView showFormEdit(@RequestParam Long id){
        ModelAndView modelAndView = new ModelAndView("order_staff/edit");
        Optional<Order> orderOptional = orderService.findById(id);
        Iterable<Order> orderList = orderService.findAll();
        List<Status> statusList = Arrays.asList(Status.values());
        if (!orderOptional.isPresent()){
            modelAndView.addObject("errorMessage", "Không tìm thấy mã đơn hàng "+ id + "!");
            modelAndView.addObject("orderList",orderList);
            return new ModelAndView("order_staff/list");
        }
        Order order = orderOptional.get();
        modelAndView.addObject("order", order);
        modelAndView.addObject("statusList", statusList);
        order.setTime(LocalDateTime.parse(formatLocalDateTime(order.getTime()), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        order.setFormattedTime(formatLocalDateTime(order.getTime()));
        return modelAndView;
    }

    @PostMapping("/edit")
    public String editOrder(@RequestParam Long id, @RequestParam String status){
        Optional<Order> orderOptional = orderService.findById(id);
        Order order = orderOptional.get();
        order.setStatus(status);
        orderService.save(order);
        return "redirect:/order_staff/list";
    }
}
