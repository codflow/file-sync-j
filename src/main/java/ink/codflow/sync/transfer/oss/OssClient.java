package ink.codflow.sync.transfer.oss;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.SimplifiedObjectMeta;

import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.transfer.Client;

public class OssClient implements Client<OssObject> {

	OssAuthentication authentication;

	OSS client;

	public OssClient(OssAuthentication authentication) {

		String endpoint = authentication.getEndpoint();
		String accessKeyId = authentication.getAccessKeyId();
		String accessKeySecret = authentication.getAccessKeySecret();

		client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

	}

	@Override
	public OssObject[] list(String path) throws FileException {
		int index = path.indexOf("/",1);
				
		String bucketName = path.substring(1, index);
		String keyPrefix = path.substring(index,path.length()-1);
		ObjectListing list = client.listObjects(bucketName, keyPrefix);
		List<OSSObjectSummary> lObjectSummaries = list.getObjectSummaries();

		List<OssObject> ossObjectList = new ArrayList<OssObject>(lObjectSummaries.size());
		for (OSSObjectSummary ossObjectSummary : lObjectSummaries) {
			String eTag = ossObjectSummary.getETag();
			Date lastMod = ossObjectSummary.getLastModified();
			long size = ossObjectSummary.getSize();
			OssObject ossObject = new OssObject();
			ossObject.seteTag(eTag);
			ossObject.setLastModified(lastMod);
			ossObject.setSize(size);
			ossObject.setOss(client);
			ossObjectList.add(ossObject);
		}
		return ossObjectList.toArray(new OssObject[0]);
	}

	@Override
	public OssObject resolve(String path) throws FileException {

		int index = path.indexOf("/");
		String bucketName = path.substring(0, index);
		String key = path.substring(index);

		SimplifiedObjectMeta meta = client.getSimplifiedObjectMeta(bucketName, key);

		long size = meta.getSize();
		String eTag = meta.getETag();
		Date lastMod = meta.getLastModified();
		OssObject ossObject = new OssObject();
		ossObject.seteTag(eTag);
		ossObject.setLastModified(lastMod);
		ossObject.setSize(size);
		ossObject.setOss(client);
		return ossObject;
	}

	@Override
	public boolean isRemote() {
		return true;
	}

	@Override
	public AbstractObjectWapper<OssObject> resolveWapper(String uri) {
		OssObjectWapper objectWapper = new OssObjectWapper(uri);
		return objectWapper;
	}

	public void setAuthentication(OssAuthentication authentication) {
		this.authentication = authentication;
	}
	
	
	public void close() {
		this.client.shutdown();
	}

}
