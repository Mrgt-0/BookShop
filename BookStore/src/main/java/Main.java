import Controller.OrderController;
import Controller.RequestController;
import DI.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.transaction.SystemException;

public class Main {
    public static void main(String[] args) throws SystemException {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Попробуйте получить OrderController из контекста
        OrderController orderController = context.getBean(OrderController.class);
    }
}