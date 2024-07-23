package vn.vpm.la.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.vpm.la.domain.Cart;
import vn.vpm.la.domain.User;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long>{
    Cart findByUser(User user);
}
