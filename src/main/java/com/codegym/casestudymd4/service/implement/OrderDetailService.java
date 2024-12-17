package com.codegym.casestudymd4.service.implement;

import com.codegym.casestudymd4.model.OrderDetail;
import com.codegym.casestudymd4.repository.IOrderDetailRepository;
import com.codegym.casestudymd4.service.IOrderDetailService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderDetailService implements IOrderDetailService {
    private final IOrderDetailRepository iOrderDetailRepository;
    public OrderDetailService(IOrderDetailRepository iOrderDetailRepository){
        this.iOrderDetailRepository = iOrderDetailRepository;
    }
    @Override
    public Iterable<OrderDetail> findAll() {
        return iOrderDetailRepository.findAll();
    }

    @Override
    public Optional<OrderDetail> findById(Long id) {
        return iOrderDetailRepository.findById(id);
    }

    @Override
    public void save(OrderDetail orderDetail) {
        iOrderDetailRepository.save(orderDetail);
    }

    @Override
    public void delete(Long id) {
        iOrderDetailRepository.deleteById(id);
    }
}
