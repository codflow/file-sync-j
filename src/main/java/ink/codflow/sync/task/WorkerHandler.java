package ink.codflow.sync.task;

import ink.codflow.sync.core.AbstractObjectWapper;

public interface WorkerHandler {

	long doAnalyse(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject);

	void doSync(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject);
}
