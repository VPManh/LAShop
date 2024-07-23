package vn.vpm.la.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.vpm.la.domain.Role;
import vn.vpm.la.domain.User;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User hodanit);

    List<User> findOneByEmail(String email);

    User findById(long id);

    User deleteById(long id);

    boolean existsByEmail(String email);

    User findByEmail(String email);

    long countByRole(Role role);
}
