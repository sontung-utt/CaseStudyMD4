package com.codegym.casestudymd4.controller;

import com.codegym.casestudymd4.model.Cart;
import com.codegym.casestudymd4.model.CartDetail;
import com.codegym.casestudymd4.model.Customer;
import com.codegym.casestudymd4.model.Product;
import com.codegym.casestudymd4.service.implement.CartDetailService;
import com.codegym.casestudymd4.service.implement.CartService;
import com.codegym.casestudymd4.service.implement.CustomerService;
import com.codegym.casestudymd4.service.implement.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final CartDetailService cartDetailService;
    private final CustomerService customerService;
    private final ProductService productService;

    public CartController(CartService cartService,
                          CartDetailService cartDetailService,
                          CustomerService customerService,
                          ProductService productService){
        this.cartService = cartService;
        this.cartDetailService = cartDetailService;
        this.customerService = customerService;
        this.productService = productService;
    }

    public String formatLocalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return localDateTime.format(formatter);
    }

    @GetMapping
    public ModelAndView showCartDetail(HttpSession session){
        Long idLogin = (Long) session.getAttribute("idLogin");
        if (idLogin == null) {
            return new ModelAndView("user/login");
        }
        ModelAndView modelAndView = new ModelAndView("cart/list");
        Long idCustomer = customerService.getIdByUserId(idLogin);
        Long idCart = cartService.getIdByIdCustomer(idCustomer);
        List<CartDetail> cartDetailList = cartDetailService.listCartDetailByCartId(idCart);
        if (cartDetailList.isEmpty()){
            modelAndView.addObject("errorMessage", "Giỏ hàng hiện tại đang trống!");
            modelAndView.addObject("cartDetailList", cartDetailList);
            return modelAndView;
        }
        for (CartDetail cartDetail : cartDetailList){
            cartDetail.setCreated_at(LocalDateTime.parse(formatLocalDateTime(cartDetail.getCreated_at()), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            cartDetail.setFormattedCreatedAt(formatLocalDateTime(cartDetail.getCreated_at()));
        }
        modelAndView.addObject("cartDetailList", cartDetailList);
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView addProductToCart(HttpSession session,
                                         @RequestParam Long idProduct){
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
        Long idCart = cartService.getIdByIdCustomer(idCustomer);
        Optional<Product> productOptional = productService.findById(idProduct);
        Product product = productOptional.get();

        if (cartDetailService.existProductInCart(idCart, idProduct)){
            ModelAndView modelAndView = new ModelAndView("cart/edit");
            CartDetail cartDetail = cartDetailService.findByProductId(idCart, idProduct);
            modelAndView.addObject("cartDetail", cartDetail);
            modelAndView.addObject("idProduct", idProduct);
            modelAndView.addObject("product", product);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("cart/add");
            modelAndView.addObject("cartDetail", new CartDetail());
            modelAndView.addObject("idProduct", idProduct);
            modelAndView.addObject("product", product);
            return modelAndView;
        }
    }

    @PostMapping("/add")
    public String addProductToCart(@RequestParam Long idProduct,
                                   @RequestParam Long quantity,
                                   HttpSession session){
        Long idLogin = (Long) session.getAttribute("idLogin");
        Long idCustomer = customerService.getIdByUserId(idLogin);
        Long idCart = cartService.getIdByIdCustomer(idCustomer);
        Optional<Cart> cartOptional = cartService.findById(idCart);
        Optional<Product> productOptional = productService.findById(idProduct);
        Product product = productOptional.get();
        Cart cart = cartOptional.get();
        CartDetail cartDetail = new CartDetail(cart, product, quantity, product.getPrice());
        cartDetailService.save(cartDetail);
        return "redirect:/cart";
    }

    @PostMapping("edit")
    public String editProductToCart(@RequestParam Long idProduct,
                                    @RequestParam Long quantity,
                                    HttpSession session){
        Long idLogin = (Long) session.getAttribute("idLogin");
        Long idCustomer = customerService.getIdByUserId(idLogin);
        Long idCart = cartService.getIdByIdCustomer(idCustomer);
        Optional<Cart> cartOptional = cartService.findById(idCart);
        Optional<Product> productOptional = productService.findById(idProduct);
        Product product = productOptional.get();
        Cart cart = cartOptional.get();
        CartDetail existCartProduct = cartDetailService.findByProductId(idCart, idProduct);
        CartDetail cartDetail = new CartDetail(existCartProduct.getId(), cart, product, quantity, product.getPrice());
        cartDetail.setCreated_at(existCartProduct.getCreated_at());
        cartDetailService.save(cartDetail);
        return "redirect:/cart";
    }

    @GetMapping("/delete")
    public String deleteProductToCart(@RequestParam Long id){
        cartDetailService.delete(id);
        return "redirect:/cart";
    }
}
