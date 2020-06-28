package ink.codflow.sync;


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


}
