package ink.codflow.sync.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.model.Bucket;

import ink.codflow.sync.bo.ClientEndpointBO;
import ink.codflow.sync.bo.LinkBO;
import ink.codflow.sync.bo.ObjectBO;
import ink.codflow.sync.bo.TaskBO;
import ink.codflow.sync.consts.FileSyncMode;
import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.core.ClientEndpoint;
import ink.codflow.sync.exception.ArgumentsException;
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

    public SyncTask createSyncTask(LinkBO link, List<ObjectBO> selectedObjects, FileSyncMode mode) {

        ClientEndpointBO distEndpointBO = link.getDestEndpoint();
        ClientEndpointBO srcEndpointBO = link.getSrcEndpoint();

        FileSyncMode mode0 = mode != null ? mode : link.getMode();
        conductor.registerEndpoint(srcEndpointBO);
        conductor.registerEndpoint(distEndpointBO);
        SyncTask task = conductor.createSyncTask(link, selectedObjects, mode0);
        return task;
    }

    public SyncTask createSyncTask(TaskBO taskBO) {

        return conductor.createSyncTask(taskBO);
    }

    public String launchTask(SyncTask task) {
        return conductor.launch(task);
    }

    public boolean testClient(ClientEndpointBO endpoint) {

        ClientEndpoint<?> clientEndpoint = conductor.getEndpoint(endpoint, true);

        try {
            AbstractObjectWapper<?> object = clientEndpoint.resolve("/");

            object.listChildren();
            return true;
        } catch (FileException e) {
            logger.error("chk error", e);
            return false;
        }
    }

    public boolean deleteClient(ClientEndpointBO clientEndpointBO) {
        clientEndpointBO.getRootPath();
        ClientEndpoint<?> endpoint = conductor.getEndpoint(clientEndpointBO, true);
        try {
            endpoint.resolve("").remove();
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

    public List<ObjectBO> listEndpointContent(ClientEndpointBO endpointBO, String uri) throws FileException {
        ClientEndpoint<?> clientEndpoint = conductor.getEndpoint(endpointBO);
        List<AbstractObjectWapper<?>> wappers = clientEndpoint.list(uri);
        List<ObjectBO> objectBOList = new ArrayList<ObjectBO>();
        for (AbstractObjectWapper<?> abstractObjectWapper : wappers) {
            String fileName = abstractObjectWapper.getBaseFileName();
            String uri0 = abstractObjectWapper.getUri();
            boolean file = abstractObjectWapper.isFile();
            ObjectBO objectBO = new ObjectBO();
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

    public List<String> listBucket(ClientEndpointBO clientEndpointBO) throws FileException {
        ClientEndpoint<?> endpoint = conductor.getEndpoint(clientEndpointBO, true);
        Client<?> client = endpoint.getRandomClient();

        if (client instanceof OssClient) {

            ArrayList<String> bucketNameList = new ArrayList<String>();
            OssClient client0 = (OssClient) client;
            List<Bucket> buckets = client0.getClient().listBuckets();
            for (Bucket bucket : buckets) {
                String bucketName = bucket.getName();
                bucketNameList.add(bucketName);
            }
            return bucketNameList;
        }

        throw new FileException("Wrong oss client arguments!");
    }



 

 
}
