package com.codegym.casestudymd4.controller;

import com.codegym.casestudymd4.model.*;
import com.codegym.casestudymd4.service.implement.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final CustomerService customerService;
    private final CustomerAccountService customerAccountService;
    private final ProductService productService;
    private final CartService cartService;
    private final CartDetailService cartDetailService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    @Autowired
    public OrderController(CustomerService customerService,
                           ProductService productService,
                           CartService cartService,
                           CartDetailService cartDetailService,
                           OrderService orderService,
                           OrderDetailService orderDetailService,
                           CustomerAccountService customerAccountService){
        this.customerService = customerService;
        this.productService = productService;
        this.cartService = cartService;
        this.cartDetailService = cartDetailService;
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.customerAccountService = customerAccountService;
    }

    public String formatLocalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return localDateTime.format(formatter);
    }

    @GetMapping("/list")
    public ModelAndView showOrderList(HttpSession session){
        Long idLogin = (Long) session.getAttribute("idLogin");
        if (idLogin == null) {
            return new ModelAndView("user/login");
        }
        Long idCustomer = customerService.getIdByUserId(idLogin);
        if (idCustomer == null){
            ModelAndView modelAndView = new ModelAndView("customer/add");
            modelAndView.addObject("customer", new Customer());
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("order/list");
        Iterable<Order> orderList = orderService.findAll();
        if (!orderList.iterator().hasNext()){
            modelAndView.addObject("errorMessage", "Bạn chưa có đơn hàng nào!");
            return modelAndView;
        }
        for (Order order : orderList){
            order.setTime(LocalDateTime.parse(formatLocalDateTime(order.getTime()), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            order.setFormattedTime(formatLocalDateTime(order.getTime()));
        }
        modelAndView.addObject("orderList", orderList);
        return modelAndView;
    }

    @GetMapping("/view")
    public ModelAndView showViewOrder(@RequestParam Long id,
                                      HttpSession session){
        Long idLogin = (Long) session.getAttribute("idLogin");
        if (idLogin == null) {
            return new ModelAndView("user/login");
        }
        Long idCustomer = customerService.getIdByUserId(idLogin);
        if (idCustomer == null){
            ModelAndView modelAndView = new ModelAndView("customer/add");
            modelAndView.addObject("customer", new Customer());
            return modelAndView;
        }
        BigDecimal total = orderService.getTotalByOrderId(id);
        ModelAndView modelAndView = new ModelAndView("order/view");
        List<OrderDetail> list = orderDetailService.getOrderDetailByOrderId(id);
        modelAndView.addObject("list", list);
        modelAndView.addObject("idOrder", id);
        modelAndView.addObject("total", total);
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView showFormAdd(HttpSession session,
                                    Model model){
        Long idLogin = (Long) session.getAttribute("idLogin");
        if (idLogin == null) {
            return new ModelAndView("user/login");
        }
        Long idCustomer = customerService.getIdByUserId(idLogin);
        if (idCustomer == null){
            ModelAndView modelAndView = new ModelAndView("customer/add");
            modelAndView.addObject("customer", new Customer());
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("order/add");
        Long idCart = cartService.getIdByIdCustomer(idCustomer);
        List<CartDetail> cartDetailList = cartDetailService.listCartDetailByCartId(idCart);
        if (cartDetailList.isEmpty()){
            Iterable<Product> products = productService.findAll();
            String customerUsername = customerAccountService.getUsernameById(idLogin);
            modelAndView.addObject("errorMessage", "Giỏ hàng hiện tại đang trống!");
            modelAndView.addObject("cartDetailList", cartDetailList);
            model.addAttribute("products", products);
            model.addAttribute("customerUsername", customerUsername);
            return new ModelAndView("customer/home");
        }
        for (CartDetail cartDetail : cartDetailList){
            cartDetail.setCreated_at(LocalDateTime.parse(formatLocalDateTime(cartDetail.getCreated_at()), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            cartDetail.setFormattedCreatedAt(formatLocalDateTime(cartDetail.getCreated_at()));
        }
        BigDecimal total = cartDetailService.getTotalByCartId(idCart);
        modelAndView.addObject("cartDetailList", cartDetailList);
        modelAndView.addObject("total", total);
        return modelAndView;
    }

    @PostMapping("/add")
    public String addOrder(HttpSession session, RedirectAttributes redirectAttributes){
        Long idLogin = (Long) session.getAttribute("idLogin");
        Long idCustomer = customerService.getIdByUserId(idLogin);
        Long idCart = cartService.getIdByIdCustomer(idCustomer);
        List<CartDetail> cartDetailList = cartDetailService.listCartDetailByCartId(idCart);

        Optional<Customer> customerOptional = customerService.findById(idCustomer);
        Optional<Cart> cartOptional = cartService.findById(idCart);
        Customer customer = customerOptional.get();
        Cart cart = cartOptional.get();
        BigDecimal total = cartDetailService.getTotalByCartId(idCart);
        Order order = new Order(total,cart,customer);
        orderService.save(order);

        for (CartDetail cartDetail : cartDetailList){
            OrderDetail orderDetail = new OrderDetail(order, cartDetail.getProduct(), cartDetail.getQuantity(), cartDetail.getPrice());
            orderDetailService.save(orderDetail);
        }
        cartDetailService.deleteCartDetailByCartId(idCart);
        redirectAttributes.addFlashAttribute("successMessage", "Đơn hàng đã được thêm thành công!");
        return "redirect:/order/list";
    }

}
