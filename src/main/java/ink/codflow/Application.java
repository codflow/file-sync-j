package ink.codflow;

import java.util.concurrent.CountDownLatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ink.codflow.sync.api.command.CommandApi;
@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

        CountDownLatch lock = new CountDownLatch(1);
        try {
            lock.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // process(args);
    }

    static boolean process(String[] args) {

        boolean isServerMode = false;
        CommandApi commandApi = new CommandApi();

        commandApi.dispatch(args);

        // for (int i = 0; i < args.length; i++) {
        // switch (args[i]) {
        // case "-S":

        // break;

        // default:
        // break;
        // }

        // }

        return false;
    }

}
