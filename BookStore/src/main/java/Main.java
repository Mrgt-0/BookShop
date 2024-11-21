
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.transaction.SystemException;

@SpringBootApplication
public class Main {
    public static void main(String[] args) throws SystemException {
        SpringApplication.run(Main.class, args);
    }
}