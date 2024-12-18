package com.codegym.casestudymd4.service.implement;

import com.codegym.casestudymd4.model.CartDetail;
import com.codegym.casestudymd4.repository.ICartDetailRepository;
import com.codegym.casestudymd4.service.ICartDetailService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartDetailService implements ICartDetailService {
    private final ICartDetailRepository iCartDetailRepository;
    public CartDetailService(ICartDetailRepository iCartDetailRepository){
        this.iCartDetailRepository = iCartDetailRepository;
    }
    @Override
    public Iterable<CartDetail> findAll() {
        return iCartDetailRepository.findAll();
    }

    @Override
    public Optional<CartDetail> findById(Long id) {
        return iCartDetailRepository.findById(id);
    }

    @Override
    public void save(CartDetail cartDetail) {
        iCartDetailRepository.save(cartDetail);
    }

    @Override
    public void delete(Long id) {
        iCartDetailRepository.deleteById(id);
    }

    public Long getNumProduct(Long idCart, Long idCustomer){
        return iCartDetailRepository.getNumProduct(idCart, idCustomer);
    }

    public List<CartDetail> listCartDetailByCartId(Long idCart){
        return iCartDetailRepository.getListByCartId(idCart);
    }

    public boolean existProductInCart(Long idCart, Long idProduct){
        Long result = iCartDetailRepository.existProductInCart(idCart, idProduct);
        return result != null && result > 0;
    }

    public CartDetail findByProductId(Long idCart, Long idProduct){
        return iCartDetailRepository.findByIdProduct(idCart, idProduct);
    }

    public BigDecimal getTotalByCartId (Long idCart) {
        return iCartDetailRepository.getTotalByCartId(idCart);
    }

    public void deleteCartDetailByCartId (Long idCart) {
        iCartDetailRepository.deleteCartDetailByCartId(idCart);
    }
}
