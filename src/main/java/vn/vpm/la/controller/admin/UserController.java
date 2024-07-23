package vn.vpm.la.controller.admin;

import vn.vpm.la.domain.User;
import vn.vpm.la.service.UploadService;
import vn.vpm.la.service.UserService;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@Controller
public class UserController {

    // Dependency Injection
    private final UserService userService;
    private final UploadService uploadService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UploadService uploadService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.passwordEncoder = passwordEncoder;
    }

    // @RequestMapping("/")
    // public String getHomePage(Model model) {
    //     // List<User> listUser = this.userService.getFindAllUser();
    //     // System.out.println(listUser);
    //     // List<User> listByUser = this.userService.getFindByEmailUser("huy@gmail.com");
    //     // System.out.println(listByUser);
    //     // String test = this.userService.handHello();
    //     // model.addAttribute("test", test);
    //     return "hello";
    // }

    @RequestMapping("/admin/user") // nếu chỉ truyền String vào thì mặc định là doGet
    public String getViewTableUser(Model model) {
        List<User> users = this.userService.getFindAllUser();
        // System.out.println(">>> check user: "+users);
        model.addAttribute("users", users);
        return "admin/user/show";
    }

    // Start View Detail
    @RequestMapping("/admin/user/{id}")
    public String getDetailUserPage(Model model, @PathVariable Long id) {
        // System.out.println("Check path id: " + id);
        model.addAttribute("id", id);
        User users = this.userService.getUserById(id);
        model.addAttribute("userId", users);
        return "admin/user/detail";
    }

    @GetMapping("/admin/user/create")
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @PostMapping("/admin/user/create")
    public String CreateUserPage(Model model, @ModelAttribute("newUser") @Valid User user,
    BindingResult newUserBindingResult,
    @RequestParam("hoidanitFile") MultipartFile file) {// thêm ModelAttribue ở bên// form và controller

        // Validate
        // List<FieldError> errors = newUserBindingResult.getFieldErrors();
        // for (FieldError error : errors) {
        //     System.out.println(error.getField() + " - " + error.getDefaultMessage());
        // }

        if (newUserBindingResult.hasErrors()) {
            return "admin/user/create";
        }
        
        String avatar = this.uploadService.handleSaveUploadFile(file, "avatar");
        String hashPassword = this.passwordEncoder.encode(user.getPassword());

        user.setAvatar(avatar);
        user.setPassword(hashPassword);
        user.setRole(this.userService.getHashPassWord(user.getRole().getName()));

        this.userService.handleSaveUser(user);
        return "redirect:/admin/user";
    }

    // End View Detail

    // Start View Update

    @RequestMapping("/admin/user/update/{id}")
    public String getUpdateUserPage(Model model, @PathVariable Long id) {
        User currentUser = this.userService.getUserById(id);
        model.addAttribute("newUser", currentUser);
        return "admin/user/update";
    }

    @PostMapping("/admin/user/update")
    public String postUpdateUser(Model model, @ModelAttribute("newUser") @Valid User user
    ,BindingResult newUserBindingResult, @RequestParam("hoidanitFile") MultipartFile file) {

        User currentUser = this.userService.getUserById(user.getId());
        model.addAttribute("newUser", currentUser);
        if (currentUser != null) {

            if (!file.isEmpty()) {
                String avatar = this.uploadService.handleSaveUploadFile(file, "avatar");
                currentUser.setAvatar(avatar);
            }
            currentUser.setAddress(user.getAddress());
            currentUser.setFullName(user.getFullName());
            currentUser.setPhone(user.getPhone());
            // currentUser.setEmail(user.getEmail());

            // vì sử dụng đối tượng Role trong User nên phải gọi như vây
            currentUser.getRole().setName(user.getRole().getName());

            this.userService.handleSaveUser(currentUser);
        }
        return "redirect:/admin/user";
    }
    // End View Update

    // Start View Delete
    @GetMapping("/admin/user/delete/{id}")
    public String getDeleteUserPage(Model model, @PathVariable long id) {
        // User user = new User();
        // user.setId(id);
        model.addAttribute("id", id);
        model.addAttribute("newUser", this.userService.getUserById(id)); // lấy dữ liệu từ service thay vì tạo mới User (new User())
                                                                         
        return "admin/user/delete";
    }

    @PostMapping("/admin/user/delete")
    public String postDeletePage(Model model, @ModelAttribute("newUser") User user) {
        User users = this.userService.handleDeleteUser(user.getId());
        System.out.println("check delete id =" + users);
        return "redirect:/admin/user";
    }

}
