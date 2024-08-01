package vn.vpm.la.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import vn.vpm.la.domain.*;
import vn.vpm.la.repository.*;
import vn.vpm.la.service.Specification.ProductSpecs;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartDetailRepository cartDetailRepository;
    private final CartRepository cartRepository;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public ProductService(ProductRepository productRepository, CartDetailRepository cartDetailRepository,
                          CartRepository cartRepository, UserService userService,
                          OrderRepository orderRepository, OrderDetailRepository orderDetailRepository) {
        this.productRepository = productRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public Product handleSaveProduct(Product product) {

        return this.productRepository.save(product);
    }

    ////////////////////////////////////

    public Page<Product> getAllProductWithSpec(Pageable page, ProductCriteriaDTO productCriteriaDTO) {
        if (productCriteriaDTO.getTarget() == null
                && productCriteriaDTO.getFactory() == null
                && productCriteriaDTO.getPrice() == null ){
            return this.productRepository.findAll(page);
        }

        Specification<Product> combinedSpec = Specification.where(null);

        if (productCriteriaDTO.getTarget() != null && productCriteriaDTO.getTarget().isPresent()) {
            Specification<Product> currentSpecs = ProductSpecs.matchListTarget(productCriteriaDTO.getTarget().get());
            combinedSpec = combinedSpec.and(currentSpecs);
        }
        if (productCriteriaDTO.getFactory() != null && productCriteriaDTO.getFactory().isPresent()) {
            Specification<Product> currentSpecs = ProductSpecs.matchListFactory(productCriteriaDTO.getFactory().get());
            combinedSpec = combinedSpec.and(currentSpecs);
        }

        if (productCriteriaDTO.getPrice() != null && productCriteriaDTO.getPrice().isPresent()) {
            Specification<Product> currentSpecs = this.buildPriceSpecification(productCriteriaDTO.getPrice().get());
            combinedSpec = combinedSpec.and(currentSpecs);
        }

        return this.productRepository.findAll(combinedSpec, page);
        ////////////////////////////////////

        // case 6
        public Specification<Product> buildPriceSpecification(List<String> price) {
            Specification<Product> combinedSpec = Specification.where(null); // disconjunction
            for (String p : price) {
                double min = 0;
                double max = 0;

                // Set the appropriate min and max based on the price range string
                switch (p) {
                    case "duoi-10-trieu":
                        min = 0;
                        max = 10000000;
                        break;
                    case "10-15-trieu":
                        min = 10000000;
                        max = 15000000;
                        break;
                    case "15-20-trieu":
                        min = 15000000;
                        max = 20000000;
                        break;
                    case "tren-20-trieu":
                        min = 20000000;
                        max = 200000000;
                        break;
                }

                if (min != 0 && max != 0) {
                    Specification<Product> rangeSpec = ProductSpecs.matchMultiplePrice(min, max);
                    combinedSpec = combinedSpec.or(rangeSpec);
                }
            }

            return combinedSpec;
        }

// case 1
    // public Page<Product> fetchProductsWithSpec(Pageable page, double min) {
    // return this.productRepository.findAll(ProductSpecs.minPrice(min), page);
    // }

    // case 2
    // public Page<Product> fetchProductsWithSpec(Pageable page, double max) {
    // return this.productRepository.findAll(ProductSpecs.maxPrice(max), page);
    // }

    // case 3
    // public Page<Product> fetchProductsWithSpec(Pageable page, String factory) {
    // return this.productRepository.findAll(ProductSpecs.matchFactory(factory),
    // page);
    // }

    // case 4
    // public Page<Product> fetchProductsWithSpec(Pageable page, List<String>
    // factory) {
    // return this.productRepository.findAll(ProductSpecs.matchListFactory(factory),
    // page);
    // }

    // case 5
    // public Page<Product> fetchProductsWithSpec(Pageable page, String price) {
    // // eg: price 10-toi-15-trieu
    // if (price.equals("10-toi-15-trieu")) {
    // double min = 10000000;
    // double max = 15000000;
    // return this.productRepository.findAll(ProductSpecs.matchPrice(min, max),
    // page);

    // } else if (price.equals("15-toi-30-trieu")) {
    // double min = 15000000;
    // double max = 30000000;
    // return this.productRepository.findAll(ProductSpecs.matchPrice(min, max),
    // page);
    // } else
    // return this.productRepository.findAll(page);
    // }

    // case 6
    // public Page<Product> fetchProductsWithSpec(Pageable page, List<String> price)
    // {
    // Specification<Product> combinedSpec = (root, query, criteriaBuilder) ->
    // criteriaBuilder.disjunction();
    // int count = 0;
    // for (String p : price) {
    // double min = 0;
    // double max = 0;

    // // Set the appropriate min and max based on the price range string
    // switch (p) {
    // case "10-toi-15-trieu":
    // min = 10000000;
    // max = 15000000;
    // count++;
    // break;
    // case "15-toi-20-trieu":
    // min = 15000000;
    // max = 20000000;
    // count++;
    // break;
    // case "20-toi-30-trieu":
    // min = 20000000;
    // max = 30000000;
    // count++;
    // break;
    // // Add more cases as needed
    // }

    // if (min != 0 && max != 0) {
    // Specification<Product> rangeSpec = ProductSpecs.matchMultiplePrice(min, max);
    // combinedSpec = combinedSpec.or(rangeSpec);
    // }
    // }

    // // Check if any price ranges were added (combinedSpec is empty)
    // if (count == 0) {
    // return this.productRepository.findAll(page);
    // }

    // return this.productRepository.findAll(combinedSpec, page);
    // }

    ////////////////////////////////////

    public Page<Product> getAllProduct(Pageable pageable) {
        return this.productRepository.findAll(pageable);
    }

    public List<Product> getTop8Products() {
        return this.productRepository.findTop8ByOrderByIdAsc();
    }

    public Optional<Product> getfindByIdProduct(long id) {
        return this.productRepository.findById(id);
    }
    // public Product getfindByIdProduct (long id){
    // return this.productRepository.findById(id);
    // }

    public Product handleDeleteProduct(long id) {
        return this.productRepository.deleteById(id);
    }

    public void handleAddProductCart(String email, long prodctId, HttpSession session, long quantity) {
        User user = this.userService.getUserByEmail(email);
        if (user != null) {
            // check xem đã có Cart hay chưa
            Cart cart = this.cartRepository.findByUser(user);

            if (cart == null) {
                // tạo mới Cart
                Cart otheCart = new Cart();

                otheCart.setUser(user);
                otheCart.setSum(0);

                cart = this.cartRepository.save(otheCart);
            }
            // tìm product
            Optional<Product> productOptional = this.productRepository.findById(prodctId);
            if (productOptional.isPresent()) {
                Product otherProduct = productOptional.get();

                // Check sp đã tưnnfg có trong giỏ hàng chưa
                CartDetail oldDetail = this.cartDetailRepository.findByCartAndProduct(cart, otherProduct);

                if (oldDetail == null) {
                    CartDetail cartDetail = new CartDetail();

                    cartDetail.setCart(cart);
                    cartDetail.setProduct(otherProduct);
                    cartDetail.setPrice(otherProduct.getPrice());
                    cartDetail.setQuantity(quantity);

                    // save cart detail
                    this.cartDetailRepository.save(cartDetail);

                    // update Cart
                    int sum = cart.getSum() + 1;
                    cart.setSum(sum);
                    this.cartRepository.save(cart);
                    session.setAttribute("sum", sum);
                } else {
                    oldDetail.setQuantity(oldDetail.getQuantity() + quantity);

                    this.cartDetailRepository.save(oldDetail);
                }

            }

        }
    }

    public Cart fetchByUser(User user) {
        return this.cartRepository.findByUser(user);
    }

    public void handleDeleteCartToProduct(long cartDetailId, HttpSession session) {
        Optional<CartDetail> optional = this.cartDetailRepository.findById(cartDetailId);
        if (optional.isPresent()) {
            CartDetail cartDetail = optional.get();

            this.cartDetailRepository.deleteById(cartDetailId);

            Cart cart = cartDetail.getCart();

            if (cart.getSum() > 1) {
                int sum = cart.getSum() - 1;
                cart.setSum(sum);
                session.setAttribute("sum", sum);
                this.cartRepository.save(cart);
            } else {
                this.cartRepository.deleteById(cart.getId());
                session.setAttribute("sum", 0);
            }

        }
    }

    public void handleUpdateCartBeforeCheckout(List<CartDetail> cartDetails) {
        for (CartDetail cartDetail : cartDetails) {
            Optional<CartDetail> cdOptional = this.cartDetailRepository.findById(cartDetail.getId());
            if (cdOptional.isPresent()) {
                CartDetail currentCartDetail = cdOptional.get();
                currentCartDetail.setQuantity(cartDetail.getQuantity());
                this.cartDetailRepository.save(currentCartDetail);
            }
        }
    }

    public void handlePlaceOrder(User user, HttpSession session,
                                 String receiverName,
                                 String receiverAddress,
                                 String receiverPhone,
                                 String receiverNote) {

//        step 1 : get cart by user
        Cart cart = this.cartRepository.findByUser(user);
        if (cart != null){
            List<CartDetail> cartDetails = cart.getCartDetails();
            if (cartDetails != null){

//              create Order
                Order order = new Order();
                order.setUser(user);
                order.setReceiverName(receiverName);
                order.setReceiverAddress(receiverAddress);
                order.setReceiverPhone(receiverPhone);
                order.setReceiverNote(receiverNote);
                order.setStatus("PENDING");

                double sum = 0;
                for(CartDetail cartDetail : cartDetails) {
                    sum += cartDetail.getPrice();
                }
                order.setTotalPrice(sum);
                order = this.orderRepository.save(order);

//             create orderDetail
                for (CartDetail cds : cartDetails){
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrder(order);
                    orderDetail.setProduct(cds.getProduct());
                    orderDetail.setQuantity(cds.getQuantity());
                    orderDetail.setPrice((long)cds.getPrice());

                    this.orderDetailRepository.save(orderDetail);
                }
//            step 2: delete cartDetail and Cart
                for (CartDetail cds : cartDetails){
                    this.cartDetailRepository.deleteById(cds.getId());
                }

                this.cartRepository.deleteById(cart.getId());

//                step 3: update Session
                session.setAttribute("sum",0);

            }

        }
    }
}
