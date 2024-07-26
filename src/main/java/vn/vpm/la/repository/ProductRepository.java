package vn.vpm.la.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.vpm.la.domain.Product;
import java.util.Optional;



@Repository
public interface ProductRepository extends JpaRepository<Product ,Long>{
    Product save(Product product);
    Optional<Product> findById(long id);
    Product deleteById(long id);
    Page<Product> findAll(Pageable pageable);
    
}
