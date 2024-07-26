package vn.vpm.la.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    public String getDashboard(Model model, @RequestParam("page") Optional<String> optionalOrder) {

        int page = 1;
        try {
            if (optionalOrder.isPresent()) {
                page = Integer.parseInt(optionalOrder.get());
            }
            else {

            }
        }catch (Exception e) {

        }

        Pageable pageable = PageRequest.of(page - 1, 5);
        Page<Order> orderPage =  this.orderService.fetchAllOrders(pageable);

        List<Order> orders = orderPage.getContent();
        model.addAttribute("orders", orders);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
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
