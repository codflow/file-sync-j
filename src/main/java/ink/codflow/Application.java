package ink.codflow;

import java.util.concurrent.CountDownLatch;

 

import ink.codflow.sync.api.command.CommandApi;
public class Application {

    public static void main(String[] args) {


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
