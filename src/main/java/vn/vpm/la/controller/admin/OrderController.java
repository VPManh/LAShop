package vn.vpm.la.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import vn.vpm.la.domain.Order;
import vn.vpm.la.domain.Product;
import vn.vpm.la.service.OrderService;

import java.util.List;
import java.util.Optional;


@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/admin/order")
    public String getDashboard(Model model){

        List<Order> orders = this.orderService.fetchAllOrders();
        model.addAttribute("orders", orders);

        return "admin/order/show";
    }
    @GetMapping("/admin/order/{id}")
    public String getViewDetailOrder(Model model, @PathVariable long id){

        Optional<Order> optionalOrder = this.orderService.fetchOrderById(id);
        Order order = optionalOrder.get();
        model.addAttribute("order", order);
        model.addAttribute("id", id);
        model.addAttribute("orderDetails", order.getOrderDetails());

        return "admin/order/detail";
    }

//    Update
    @GetMapping("/admin/order/update/{id}")
    public String getPageUpdateOrder(Model model, @PathVariable long id){

        Optional<Order> currentOrder = this.orderService.fetchOrderById(id);
        model.addAttribute("newOrder", currentOrder.get());

        return "admin/order/update";
    }

    @PostMapping("/admin/order/update")
    public String postHandleUpdateOrder(Model model, @ModelAttribute("newOrder") Order order){

        this.orderService.handleUpdateOrder(order);

        return "redirect:/admin/order";
    }

//    Delete
    @GetMapping("/admin/order/delete/{id}")
    public String getPageDeleteOrder(Model model, @PathVariable long id){

        model.addAttribute("id", id);
        model.addAttribute("newOrder", new Order());

        return "admin/order/delete";
    }
    @PostMapping("/admin/order/delete")
    public String postHandleDeleteOrder(Model model, @ModelAttribute("newOrder") Order order){

        this.orderService.handleDeleteOrder(order.getId());

        return "redirect:/admin/order";
    }


}
