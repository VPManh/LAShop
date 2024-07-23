package vn.vpm.la.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.vpm.la.domain.Order;
import vn.vpm.la.domain.OrderDetail;
import vn.vpm.la.repository.OrderDetailRepository;
import vn.vpm.la.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
    }

    public List<Order> fetchAllOrders() {
        return this.orderRepository.findAll();
    }

    public Optional<Order> fetchOrderById(long id) {
        return this.orderRepository.findById(id);
    }

    public void handleDeleteOrder(Long id) {
        Optional<Order> orderOptional = this.fetchOrderById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            List<OrderDetail> orderDetails = order.getOrderDetails();
            for (OrderDetail orderDetail : orderDetails) {
                this.orderDetailRepository.deleteById(orderDetail.getId());
            }
        }

        this.orderRepository.deleteById(id);
    }

    public void handleUpdateOrder(Order order) {
        Optional<Order> orderOptional = this.fetchOrderById(order.getId());
        if (orderOptional.isPresent()) {
            Order orderToUpdate = orderOptional.get();
            orderToUpdate.setStatus(order.getStatus());
            this.orderRepository.save(orderToUpdate);
        }
    }

}
