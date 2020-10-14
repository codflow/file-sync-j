package ink.codflow.sync.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.model.Bucket;
 
import ink.codflow.sync.consts.AuthDataType;
import ink.codflow.sync.consts.FileSyncMode;
import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.core.ClientEndpoint;
import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.task.SyncTask;
import ink.codflow.sync.task.SyncTaskConductor;
import ink.codflow.sync.transfer.Client;
import ink.codflow.sync.transfer.oss.OssClient;

public class FileSyncManager {

	Logger logger = LoggerFactory.getLogger(FileSyncManager.class);

	SyncTaskConductor conductor;

	public FileSyncManager(SyncTaskConductor conductor) {

		this.conductor = conductor;
	}

	public SyncTask createSyncTask(Link link, List<FileObject> selectedObjects, FileSyncMode mode)
			throws FileException {

		Endpoint distEndpointBO = link.getDestEndpoint();
		Endpoint srcEndpointBO = link.getSrcEndpoint();

		FileSyncMode mode0 = mode != null ? mode : link.getMode();
		conductor.registerEndpoint(srcEndpointBO);
		conductor.registerEndpoint(distEndpointBO);
		SyncTask task = conductor.createSyncTask(link, selectedObjects, mode0);
		return task;
	}

	public SyncTask createSyncTask(WorkerTask workerTask) throws FileException {
		return conductor.createSyncTask(workerTask);
	}

	public SyncTask createSyncTask(Task taskBO) throws FileException {

		return conductor.createSyncTask(taskBO);
	}

	public String launchTask(SyncTask task) {
		return conductor.launch(task);
	}

	public boolean testClient(Endpoint endpoint) {

		try {
			ClientEndpoint<?> clientEndpoint = conductor.getEndpoint(endpoint, true);

			String root = clientEndpoint.getRoot();
			String testPath = "";
			if (root == null) {
				testPath = "/";
			}
			AbstractObjectWapper<?> object = clientEndpoint.resolve(testPath);
			object.listChildren();
			return true;
		} catch (FileException e) {
			logger.error("chk error", e);
			return false;
		}
	}

	public boolean deleteClient(Endpoint clientEndpointBO) throws FileException {
		clientEndpointBO.getRootPath();
		ClientEndpoint<?> endpoint = conductor.getEndpoint(clientEndpointBO, true);
		try {
			AbstractObjectWapper<?> wapper = endpoint.resolve("");
			if (wapper.isExist()) {
				wapper.remove();
			}
			return true;
		} catch (FileException e) {
			return false;
		}
	}

	public boolean checkAndTryCancle(String traceId) {
		return conductor.checkAndTryCancle(traceId);
	}

	public int getCurrentActiveTaskNumber() {
		return conductor.countTaskInProgress();
	}

	public List<FileObject> listEndpointContent(Endpoint endpointBO, String uri) throws FileException {
		ClientEndpoint<?> clientEndpoint = conductor.getEndpoint(endpointBO);
		AbstractObjectWapper<?> rootWapper = clientEndpoint.resolve(uri);
		if (rootWapper.isExist()) {
			List<AbstractObjectWapper<?>> wappers = clientEndpoint.list(uri);
			List<FileObject> objectBOList = new ArrayList<FileObject>();
			for (AbstractObjectWapper<?> abstractObjectWapper : wappers) {
				String fileName = abstractObjectWapper.getBaseFileName();
				String uri0 = abstractObjectWapper.getUri();
				boolean file = abstractObjectWapper.isFile();
				FileObject objectBO = new FileObject();
				objectBO.setName(fileName);
				objectBO.setFile(file);
				if (file) {
					long size = abstractObjectWapper.getSize();
					objectBO.setSize(size);
				}
				objectBO.setUri(uri0);
				objectBOList.add(objectBO);
			}
			return objectBOList;
		}
		return new ArrayList<>();
	}

	public List<String> listBucket(Endpoint clientEndpointBO) throws FileException {
		ClientEndpoint<?> endpoint = conductor.getEndpoint(clientEndpointBO, true);
		Client<?> client = endpoint.getRandomClient();

		if (client instanceof OssClient) {

			ArrayList<String> bucketNameList = new ArrayList<String>();
			OssClient client0 = (OssClient) client;
			List<Bucket> buckets = client0.getClient().listBuckets();
			for (Bucket bucket : buckets) {
				String exEndpoint = bucket.getExtranetEndpoint();
				if (exEndpoint != null
						&& exEndpoint.equals(clientEndpointBO.getAuthentication().getParam(AuthDataType.HOST))) {
					String bucketName = bucket.getName();
					bucketNameList.add(bucketName);
				}

			}
			return bucketNameList;
		}

		throw new FileException("Wrong oss client arguments!");
	}

	public boolean testClientWithBK(Endpoint clientEndpointBO) {
		try {
			listEndpointContent(clientEndpointBO, "/");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
