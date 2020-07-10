package ink.codflow.sync.core.handler;

import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.task.LinkWorker;
import ink.codflow.sync.task.TaskSpecs;
import ink.codflow.sync.task.LinkWorker.AnalyseListener;
import ink.codflow.sync.task.LinkWorker.SyncListener;

public interface WorkerHandler {

	long doAnalyse(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject);

	long doAnalyse(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject, AnalyseListener listener,TaskSpecs specs);

	long doAnalyse(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject, AnalyseListener listener);

	void doSync(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject);

	void doSync(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject, SyncListener listener);

    void doSync(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject, SyncListener listener,
            TaskSpecs specs);
	
	

}
