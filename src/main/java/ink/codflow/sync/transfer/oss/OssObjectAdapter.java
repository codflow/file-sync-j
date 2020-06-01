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
		char lastChar = key.charAt(key.length() - 1);
		return ((size == 0) && SP_CHAR == lastChar);
	}

	@Override
	public boolean isDir(OssObject object) throws FileException {
		return !isFile(object);
	}

	public void createDir(OssObject object) throws FileException {

		try {
			String bucketName = object.getBucket();
			String key = object.getKey();
			String keySuffixWithSlash = checkAndModDirKey(key);

			OSS client = object.getOss();
			client.putObject(bucketName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
		} catch (Exception e) {
			throw new FileException(e);
		}

	}

	public String checkAndModDirKey(String dirKey) {
		if (SP_CHAR == dirKey.charAt(dirKey.length() - 1)) {
			return dirKey;
		}
		return new StringBuilder(dirKey).append(SP_CHAR).toString();
	}
}
