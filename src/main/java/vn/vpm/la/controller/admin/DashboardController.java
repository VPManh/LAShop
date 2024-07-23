package vn.vpm.la.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.vpm.la.service.UserService;


@Controller
public class DashboardController {

    private final UserService userService;

    public DashboardController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String getDashboard(Model model){
        model.addAttribute("countUser",this.userService.countUser());
        model.addAttribute("countOrder",this.userService.countOrder());
        model.addAttribute("countProduct",this.userService.countProduct());
        return "admin/dashboard/show";
    }
}
