package ink.codflow.sync.task;

import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.task.LinkWorker.AnalyseListener;
import ink.codflow.sync.task.LinkWorker.CopyListener;

public interface WorkerHandler {

	long doAnalyse(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject);

	long doAnalyse(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject, AnalyseListener listener);

	void doSync(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject);

	void doSync(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject, CopyListener listener);

}
