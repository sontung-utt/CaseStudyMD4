package com.codegym.casestudymd4.service.implement;

import com.codegym.casestudymd4.model.Order;
import com.codegym.casestudymd4.repository.IOrderRepository;
import com.codegym.casestudymd4.service.IOrderService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService implements IOrderService {
    private final IOrderRepository iOrderRepository;
    public OrderService(IOrderRepository iOrderRepository){
        this.iOrderRepository = iOrderRepository;
    }
    @Override
    public Iterable<Order> findAll() {
        return iOrderRepository.findAll();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return iOrderRepository.findById(id);
    }

    @Override
    public void save(Order order) {
        iOrderRepository.save(order);
    }

    @Override
    public void delete(Long id) {
        iOrderRepository.findById(id);
    }
}
