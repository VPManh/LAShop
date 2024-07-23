package vn.vpm.la.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.vpm.la.domain.Cart;
import vn.vpm.la.domain.CartDetail;
import vn.vpm.la.domain.Product;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long>{

    boolean existsByCartAndProduct(Cart cart, Product product);
    
    CartDetail findByCartAndProduct(Cart cart, Product product);
}
