package ink.codflow.sync.transfer.oss;

import java.util.Date;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;

public class OssObject {

	public OssObject(){
		
	}
	
	OSS oss;

	private OSSObject realObject;

	private Long size;
	private Date lastModified;
	private String eTag;
	private String bucketName;
	private String key;
	private String uri;

	public OSS getOss() {
		return oss;
	}

	public void setOss(OSS oss) {
		this.oss = oss;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String geteTag() {
		return eTag;
	}

	public void seteTag(String eTag) {
		this.eTag = eTag;
	}

	public OSSObject getRealObject() {
		return realObject;
	}

	public void setRealObject(OSSObject realObject) {
		this.realObject = realObject;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucket) {
		this.bucketName = bucket;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	

	
}
