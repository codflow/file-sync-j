package ink.codflow.sync.core.adapter;

import java.io.InputStream;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;

import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.transfer.oss.OssObject;
import ink.codflow.sync.transfer.oss.OssObjectAdapter;
import ink.codflow.sync.transfer.vfs.VfsObjectAdapter;

public class Vfs2OssObjectManiputationAdapter implements ObjectManipulationAdapter<FileObject, OssObject> {

	OssObjectAdapter ossObjectAdapter = new OssObjectAdapter();
	VfsObjectAdapter vfsObjectAdapter = new VfsObjectAdapter();

	public static final AdapterTypeEnum TYPE_TAG_0 = AdapterTypeEnum.VFS2OSS;

	@Override
	public Class<FileObject> getSrcClassType() {
		return FileObject.class;
	}

	@Override
	public Class<OssObject> getDestClassType() {
		return OssObject.class;
	}

	@Override
	public void copy(FileObject src, OssObject dest) throws FileException {

		if (vfsObjectAdapter.isFile(src)) {
			if (ossObjectAdapter.isDir(dest)) {
				copyFileToDir(src, dest);
			} else {
				copyFileToFile(src, dest);
			}
		}
		if (vfsObjectAdapter.isDir(src) || ossObjectAdapter.isDir(dest)) {
			copyDirToDir(src, dest);

		} else {
			throw new FileException("Unable to copy dir to file");
		}

	}

	@Override
	public void copyFileToFile(FileObject srcFile, OssObject destFile) throws FileException {
		String bucket = destFile.getBucket();
		String key = destFile.getKey();
		try {
			InputStream inputStream = srcFile.getContent().getInputStream();

			OSS ossClient = destFile.getOss();
			ossClient.putObject(bucket, key, inputStream);
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, inputStream);
			ObjectMetadata metadata = new ObjectMetadata();
			putObjectRequest.setMetadata(metadata);
			ossClient.putObject(putObjectRequest);

		} catch (FileSystemException e) {
			throw new FileException();
		}

	}

	@Override
	public void copyFileToDir(FileObject srcFile, OssObject destDir) throws FileException {
		String bucket = destDir.getBucket();
		String key = destDir.getKey();
		String baseName = srcFile.getName().getBaseName();
		try {
			InputStream inputStream = srcFile.getContent().getInputStream();
			String key0 = new StringBuilder(key).append(baseName).toString();
			destDir.getOss().putObject(bucket, key0, inputStream);
		} catch (FileSystemException e) {
			throw new FileException();
		}
	}

	@Override
	public void copyDirToDir(FileObject srcDir, OssObject destDir) throws FileException {
		try {
			FileObject[] srcFileObjects = srcDir.getChildren();
			if (srcFileObjects != null && srcFileObjects.length > 0) {
				for (FileObject fileObject : srcFileObjects) {
					if (ossObjectAdapter.isDir(destDir)) {

					} else {
						copyFileToDir(fileObject, destDir);
					}
				}
			} else {

				String baseName = srcDir.getName().getBaseName();
				String key = destDir.getKey();
				String key0 = new StringBuilder(key).append(baseName).toString();

				String bucket = destDir.getBucket();
				OSS oss = destDir.getOss();

				createOssDir(bucket, key0, oss);
			}

		} catch (FileSystemException e) {
			throw new FileException(e);
		}
	}

	void createOssDir(String bucket, String key, OSS oss) throws FileException {
		OssObject object = new OssObject();
		object.setOss(oss);
		object.setKey(key);
		object.setBucket(bucket);
		// TODO
		object.setUri(bucket + "/" + key);
		ossObjectAdapter.createDir(object);

	}

	@Override
	public boolean checkDiff(FileObject fileObject0, OssObject fileObject1) throws FileException {
		try {
			long size0 = fileObject0.getContent().getSize();
			long lastMod0 = fileObject0.getContent().getLastModifiedTime();
			long size1 = fileObject1.getSize();
			long lastMod1 = fileObject1.getLastModified().getTime();
			return (size0 == size1) && (lastMod0 == lastMod1);
		} catch (FileSystemException e) {
			throw new FileException(e);
		}
	}
}
