package ink.codflow.sync.transfer.oss;

import java.io.ByteArrayInputStream;

import com.aliyun.oss.OSS;

import ink.codflow.sync.core.adapter.ObjectAdapter;
import ink.codflow.sync.exception.FileException;

public class OssObjectAdapter implements ObjectAdapter<OssObject> {

	static final char SP_CHAR = '/';

	@Override
	public boolean isFile(OssObject object) throws FileException {

		long size = object.getSize();
		String key = object.getKey();
		if (key != null && !key.isEmpty()) {
			char lastChar = key.charAt(key.length() - 1);
			return !((size == 0) && SP_CHAR == lastChar);
		}
		return false;

	}

	@Override
	public boolean isDir(OssObject object) throws FileException {
		return !isFile(object);
	}

	public void createDir(OssObject object) throws FileException {

		try {
			String bucketName = object.getBucketName();
			String key = object.getKey();
			String keySuffixWithSlash = checkAndModDirKey(key);

			OSS client = object.getOss();
			client.putObject(bucketName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
		} catch (Exception e) {
			throw new FileException(e);
		}

	}

	public boolean isDir(String uri) {

		char lastChar = uri.charAt(uri.length() - 1);
		return SP_CHAR == lastChar;
	}

	public String getBucketName(String uri) {

		int bktSlashIndex = uri.indexOf('/', 1);
		String bucketName = uri.substring(1, bktSlashIndex);
		return bucketName;
	}

	public String getKey(String uri) {

		int bktSlashIndex = uri.indexOf('/', 1);
		String key = bktSlashIndex < uri.length() - 1 ? uri.substring(bktSlashIndex + 1) : null;

		return key;
	}

	public String getBaseFileName(String uri) {

		int length = uri.length();
		if (!isDir(uri)) {
			int preNameSlashIndex = lastIndexOfChar(uri, '/', 1);
			return uri.substring(preNameSlashIndex + 1, length - 1);
		} else {
			int preNameSlashIndex = uri.lastIndexOf('/');
			return uri.substring(preNameSlashIndex + 1);
		}
	}

	public String checkAndModDirKey(String dirKey) {
		if (SP_CHAR == dirKey.charAt(dirKey.length() - 1)) {
			return dirKey;
		}
		return new StringBuilder(dirKey).append(SP_CHAR).toString();
	}

	protected int lastIndexOfChar(String source, char target, int count) {
		int p = 0;
		char[] index = source.toCharArray();
		for (int i = index.length - 1; i >= 0; i--) {
			char c = index[i];
			if (target == c && ++p > count) {
				return i;
			}

		}
		return -1;
	}

	public boolean checkExist(OssObject object) {
		
		//TODO get summary and cache
		String key = object.getKey();
		String bucketName = object.getBucketName();
		
		return object.getOss().doesObjectExist(bucketName, key);
	}

}
