package vn.vpm.la.controller.client;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.RequestParam;
import vn.vpm.la.domain.Order;
import vn.vpm.la.domain.Product;
import vn.vpm.la.domain.User;
import vn.vpm.la.domain.dto.RegisterDTO;
import vn.vpm.la.service.OrderService;
import vn.vpm.la.service.ProductService;
import vn.vpm.la.service.UserService;


import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class HomePageController {
    
    private final ProductService productService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final OrderService orderService;

    public HomePageController(ProductService productService, UserService userService,
                              PasswordEncoder passwordEncoder, OrderService orderService) {
        this.productService = productService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String getHomepage(Model model) {

        List<Product> listProduct = this.productService.getTop8Products();
        model.addAttribute("products", listProduct);
        return "client/homepage/show";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerUser",new RegisterDTO());
        return "client/auth/register";
    }

    @PostMapping("/register")
    public String handleRegister(Model model, @ModelAttribute("registerUser") @Valid RegisterDTO registerDTO
    ,BindingResult registerUserBindingResult) {

        // // Validate
        // List<FieldError> errors = registerUserBindingResult.getFieldErrors();
        // for (FieldError error : errors) {
        //     System.out.println(error.getField() + " - " + error.getDefaultMessage());
        // }

        if (registerUserBindingResult.hasErrors()) {
            return "client/auth/register";
            
        }

        User user = this.userService.registerDTOtoUser(registerDTO);

        String password = this.passwordEncoder.encode(user.getPassword());

        user.setPassword(password);
        user.setRole(this.userService.getRoleByName("USER"));
        this.userService.handleSaveUser(user);
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String getLoginPage(Model model) {

        return "client/auth/login";
    }

    @GetMapping("/access-deny")
    public String getDenyPage(Model model) {

        return "client/auth/accessdeny";
    }

    @GetMapping("/order-history")
    public String getOrderHistoryPage(Model model, HttpServletRequest request) {
        User currentUser = new User();// null
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        currentUser.setId(id);

        List<Order> orders = this.orderService.fetchOrderByUser(currentUser);
        model.addAttribute("orders", orders);

        return "client/cart/order-history";
    }

}
