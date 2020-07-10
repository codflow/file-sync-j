package ink.codflow.sync.core.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.hadoop.hdfs.protocol.proto.ClientNamenodeProtocolProtos.IsFileClosedRequestProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.CompleteMultipartUploadResult;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;

import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.transfer.oss.OssObject;
import ink.codflow.sync.transfer.oss.OssObjectAdapter;
import ink.codflow.sync.transfer.vfs.VfsObjectAdapter;

public class Vfs2OssObjectManiputationAdapter implements ObjectManipulationAdapter<FileObject, OssObject> {
	public static final Logger logger = LoggerFactory.getLogger(Vfs2OssObjectManiputationAdapter.class);
	OssObjectAdapter ossObjectAdapter = new OssObjectAdapter();
	VfsObjectAdapter vfsObjectAdapter = new VfsObjectAdapter();
	static final long MULTISIZE = (4294967296L) ;
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
			return;
		} else if (vfsObjectAdapter.isDir(src) && ossObjectAdapter.isDir(dest)) {
			copyDirToDir(src, dest);

		} else {
			throw new FileException("Unable to copy dir to file");
		}

	}

	@Override
	public void copyFileToFile(FileObject srcFile, OssObject destFile) throws FileException {
		String bucket = destFile.getBucketName();
		String key = destFile.getKey();
		logger.info("Start copy:" + key);
		try {
			long fileLength = srcFile.getContent().getSize();

			OSS ossClient = destFile.getOss();
			if (ossClient.doesObjectExist(bucket, key)) {
				ossClient.deleteObject(bucket, key);
			}

			if (fileLength < MULTISIZE) {
				InputStream inputStream = srcFile.getContent().getInputStream();

				// ossClient.putObject(bucket, key, inputStream);
				PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, inputStream);
				// ObjectMetadata metadata = new ObjectMetadata();
				// putObjectRequest.setMetadata(metadata);
				ossClient.putObject(putObjectRequest);

			} else {

				// 创建InitiateMultipartUploadRequest对象。
				InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucket, key);

				// 如果需要在初始化分片时设置文件存储类型，请参考以下示例代码。
				// ObjectMetadata metadata = new ObjectMetadata();
				// metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS,
				// StorageClass.Standard.toString());
				// request.setObjectMetadata(metadata);

				// 初始化分片。
				InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
				// 返回uploadId，它是分片上传事件的唯一标识，您可以根据这个uploadId发起相关的操作，如取消分片上传、查询分片上传等。
				String uploadId = upresult.getUploadId();
				List<PartETag> partETags = new ArrayList<PartETag>();

				final long partSize = 10 * 1024 * 1024L; // 1MB
				int partCount = (int) (fileLength / partSize);
				if (fileLength % partSize != 0) {
					partCount++;
				}

				for (int i = 0; i < partCount; i++) {
					long startPos = i * partSize;
					long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
					InputStream instream = srcFile.getContent().getInputStream();

					// 跳过已经上传的分片。
					instream.skip(startPos);
					UploadPartRequest uploadPartRequest = new UploadPartRequest();
					uploadPartRequest.setBucketName(bucket);
					uploadPartRequest.setKey(key);
					uploadPartRequest.setUploadId(uploadId);
					uploadPartRequest.setInputStream(instream);
					// 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100 KB。
					uploadPartRequest.setPartSize(curPartSize);
					// 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出这个范围，OSS将返回InvalidArgument的错误码。
					uploadPartRequest.setPartNumber(i + 1);
					// 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
					UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
					// 每次上传分片之后，OSS的返回结果包含PartETag。PartETag将被保存在partETags中。
					partETags.add(uploadPartResult.getPartETag());
				}

				// 创建CompleteMultipartUploadRequest对象。
				// 在执行完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
				CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(
						bucket, key, uploadId, partETags);

				// 如果需要在完成文件上传的同时设置文件访问权限，请参考以下示例代码。
				// completeMultipartUploadRequest.setObjectACL(CannedAccessControlList.PublicRead);

				// 完成上传。
				CompleteMultipartUploadResult completeMultipartUploadResult = ossClient
						.completeMultipartUpload(completeMultipartUploadRequest);
					
			}

		} catch (IOException e) {
			throw new FileException();
		}

	}

	@Override
	public void copyFileToDir(FileObject srcFile, OssObject destDir) throws FileException {
		String bucket = destDir.getBucketName();
		String key = destDir.getKey();
		String baseName = srcFile.getName().getBaseName();
		try {
			logger.info("Start copy:" + key);

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
					if (vfsObjectAdapter.isDir(fileObject)) {
						String baseName = fileObject.getName().getBaseName();
						OssObject childOnObject = createChildObject(destDir, baseName, true);
						copyDirToDir(fileObject, childOnObject);
					} else {
						copyFileToDir(fileObject, destDir);
					}
				}
			} else {

				String baseName = srcDir.getName().getBaseName();
				String key = destDir.getKey();
				String key0 = new StringBuilder(key).append(baseName).toString();

				String bucket = destDir.getBucketName();
				OSS oss = destDir.getOss();

				createOssDir(bucket, key0, oss);
			}

		} catch (FileSystemException e) {
			throw new FileException(e);
		}
	}

	OssObject createChildObject(OssObject parent, String baseName, boolean isDir) {
		String bucketName0 = parent.getBucketName();
		String key0 = parent.getKey();
		OSS client = parent.getOss();
		// String uri0 = parent.getUri();
		OssObject ossObject = new OssObject();

		StringBuilder sbKey = key0 != null && !key0.isEmpty() ? new StringBuilder(key0) : new StringBuilder();
		sbKey.append(baseName);
		if (isDir) {
			sbKey.append("/");
		}
		if (isDir) {
			ossObject.setSize(0);
		}
		String key = sbKey.toString();
		String uri = "/" + key;
		ossObject.setBucketName(bucketName0);
		ossObject.setKey(key);
		ossObject.setOss(client);
		ossObject.setUri(uri);
		return ossObject;

	}

	void createOssDir(String bucket, String key, OSS oss) throws FileException {
		OssObject object = new OssObject();
		object.setOss(oss);
		object.setKey(key);
		object.setSize(0);

		object.setBucketName(bucket);
		// TODO
		object.setUri("/" + key);
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
