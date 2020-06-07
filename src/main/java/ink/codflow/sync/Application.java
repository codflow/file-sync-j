package ink.codflow.sync;

import java.util.concurrent.CountDownLatch;

import ink.codflow.sync.api.command.CommandApi;
import ink.codflow.sync.manager.FileSyncManager;
import ink.codflow.sync.task.SyncTaskConductor;
import ink.codflow.sync.ui.ExceptionHandler;
import ink.codflow.sync.ui.MainInterface;

public class Application {

	public static void main(String[] args) {

		SyncTaskConductor conductor = new SyncTaskConductor();

		FileSyncManager fileSyncManager = new FileSyncManager(conductor);
		ExceptionHandler.registerExceptionHandler();
		MainInterface mainInterface = new MainInterface(fileSyncManager);

		mainInterface.loadMainPanel();

	}

	static boolean process(String[] args) {

		boolean isServerMode = false;
		CommandApi commandApi = new CommandApi();

		commandApi.dispatch(args);

		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
			case "-S":

				break;

			default:
				break;
			}

		}

		return false;
	}

}
