package vn.vpm.la.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.vpm.la.domain.Role;
import vn.vpm.la.domain.User;
import vn.vpm.la.domain.dto.RegisterDTO;
import vn.vpm.la.repository.OrderRepository;
import vn.vpm.la.repository.ProductRepository;
import vn.vpm.la.repository.RoleRepository;
import vn.vpm.la.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       ProductRepository productRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public List<User> getFindAllUser(){
        return this.userRepository.findAll();
    }
    public List<User> getFindByEmailUser(String email){
        return this.userRepository.findOneByEmail(email);
    }

    public User getUserById (long id){
        return this.userRepository.findById(id);
    }

    public User handleSaveUser(User user){
        return this.userRepository.save(user);
    }

    public User handleDeleteUser(long id){
        return this.userRepository.deleteById(id);

    }
    public Role getHashPassWord(String name){
        return this.roleRepository.findByName(name);
    }

    public Role getRoleByName(String name) {
        return this.roleRepository.findByName(name);
    }

    // sử dụng dto (data transfer object) để lấy dữ liệu từ dto sang cho user
    public User registerDTOtoUser(RegisterDTO registerDTO){
         
        User user = new User();

        user.setFullName(registerDTO.getFirstName()+ " " +registerDTO.getLastName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());

        return user;
    }

    public boolean checkEmailExist(String email){
        return this.userRepository.existsByEmail(email);
    }
     
    public User getUserByEmail(String email){
        return this.userRepository.findByEmail(email);
    }

    public long countUser(){
        return this.userRepository.count();
    }
    public long countProduct(){
        return this.productRepository.count();
    }
    public long countOrder(){
        return this.orderRepository.count();
    }
//    public long countByRoleUser(Role role){
//            return this.userRepository.countByRole(role.setName("USER"));
//    }
}
