package ink.codflow.sync.transfer.oss;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.SimplifiedObjectMeta;

import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.transfer.Client;

public class OssClient implements Client<OssObject> {

	static final String EMPTY_KEY = "";

	String bucketName;

	OssAuthentication authentication;

	OSS client;

	public OssClient(OssAuthentication authentication) {

		String endpoint = authentication.getEndpoint();
		String accessKeyId = authentication.getAccessKeyId();
		String accessKeySecret = authentication.getAccessKeySecret();
		this.bucketName = authentication.getBucketName();
		client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

	}

	@Override
	public OssObject[] list(String uri) throws FileException {

		String key0 = uri.substring(1);
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
		listObjectsRequest.setDelimiter("/");
		//TODO page gen for large dir
		listObjectsRequest.setMaxKeys(800);
		if (!key0.isEmpty()) {
			listObjectsRequest.setPrefix(key0);

		}
		ObjectListing list = client.listObjects(listObjectsRequest);
		
		List<OSSObjectSummary> lObjectSummaries = list.getObjectSummaries();
		List<String> dirList = list.getCommonPrefixes();
		List<OssObject> ossObjectList = new ArrayList<OssObject>(lObjectSummaries.size());
		for (OSSObjectSummary ossObjectSummary : lObjectSummaries) {
			String eTag = ossObjectSummary.getETag();
			Date lastMod = ossObjectSummary.getLastModified();
			long size = ossObjectSummary.getSize();
			String key = ossObjectSummary.getKey();
			String bucket = ossObjectSummary.getBucketName();
			OssObject ossObject = new OssObject();
			ossObject.setBucketName(bucket);
			ossObject.setKey(key);
			ossObject.seteTag(eTag);
			ossObject.setLastModified(lastMod);
			ossObject.setSize(size);
			ossObject.setOss(client);
			ossObject.setUri("/"+key);
			ossObjectList.add(ossObject);
		}
		
		for (String ossDirObject : dirList) {
			
			String key = key0+ossDirObject;
			String bucket = bucketName;
			OssObject ossObject = new OssObject();
			ossObject.setBucketName(bucket);
			ossObject.setKey(key);
			ossObject.setOss(client);
			ossObject.setUri("/"+key);
			ossObjectList.add(ossObject);
			
		}
		return ossObjectList.toArray(new OssObject[0]);
	}

	@Override
	public OssObject resolve(String uri) throws FileException {
		//TODO 
		String bucketName0 = this.bucketName;
		String key = uri.substring(1,uri.length());
		
		OssObject ossObject = new OssObject();

		
		if (!key.isEmpty()) {
			SimplifiedObjectMeta meta = client.getSimplifiedObjectMeta(bucketName0, key);
			long size = meta.getSize();
			String eTag = meta.getETag();
			Date lastMod = meta.getLastModified();
			ossObject.setSize(size);
			ossObject.seteTag(eTag);
			ossObject.setLastModified(lastMod);
		}else {
			ossObject.setSize(0);

		}
		ossObject.setBucketName(bucketName0);
		ossObject.setKey(key);
		ossObject.setUri("/"+key);
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
