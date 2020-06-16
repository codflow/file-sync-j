package ink.codflow.sync.task;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.exception.FileException;

public class SelectedLinkWorker extends LinkWorker {

	Logger logger = LoggerFactory.getLogger(SelectedLinkWorker.class);

	List<SimpleObject> selectedList;
	
	
	public SelectedLinkWorker(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject ,List<SimpleObject> objectList){
		super(srcObject,destObject);
		this.selectedList = objectList;
	}
	

	@Override
	public long doAnalyse(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject) {

		List<SimpleObject> selectedList0 = getSelectedList();
		int size = 0;
		for (SimpleObject simpleObject : selectedList0) {
			String path = simpleObject.getPath();

			boolean isDir = simpleObject.isDir();
			try {
				AbstractObjectWapper<?> srcWapper = srcObject.createChild(path, isDir);
				
				AbstractObjectWapper<?> destWapper = destObject.createChild(path, isDir);
				size += super.doAnalyse(srcWapper, destWapper);
			} catch (FileException e) {
				logger.error("resolve file failed", e);

			}
		}

		return size;
	}

	@Override
	protected void doSync(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject) {

		List<SimpleObject> selectedList0 = getSelectedList();
		for (SimpleObject simpleObject : selectedList0) {
			String path = simpleObject.getPath();

			boolean isDir = simpleObject.isDir();
			try {
				AbstractObjectWapper<?> srcWapper = srcObject.createChild(path, isDir);
				String baseName = srcWapper.getBaseFileName();
				AbstractObjectWapper<?> destWapper = destObject.createChild(baseName, isDir);
				super.doSync(srcWapper, destWapper);
			} catch (FileException e) {
				logger.error("sync file failed", e);
			}
		}

	}

	public List<SimpleObject> getSelectedList() {
		return selectedList;
	}

	public void setSelectedList(List<SimpleObject> selectedList) {
		this.selectedList = selectedList;
	}

}
