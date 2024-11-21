package BookStoreService;
import BookStoreModel.*;
import Repository.OrderRepository;
import Status.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.SystemException;

@Service
@RequestMapping
public class OrderService {
    @Autowired
    private BookStoreSerializable bookStoreSerializable;
    @Autowired
    private BookStore bookStore;
    @Autowired
    private OrderRepository orderRepository;
    private static final Logger logger = LogManager.getLogger(BookStoreService.class);

    public OrderService(BookStore bookStore){
        bookStoreSerializable=new BookStoreSerializable(bookStore);
        this.bookStore=bookStore;
    }

    @PostMapping
    public String createOrder(@ModelAttribute("books") Book book, OrderStatus status) throws SystemException {
        Order order=new Order(book, status);
        orderRepository.save(order);
        return "redirect:/orders";
    }

    public void updateStatus(Order order, OrderStatus status) throws SystemException {
        orderRepository.update(order);
    }

    @GetMapping("/cancel/{order}")
    public void cancelOrder(@ModelAttribute("order") Order order) throws SystemException {
        orderRepository.delete(order.getOrderId());
    }
}
