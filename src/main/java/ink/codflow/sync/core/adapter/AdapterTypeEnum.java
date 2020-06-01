package ink.codflow.sync.core.adapter;

import org.apache.commons.vfs2.FileObject;

import ink.codflow.sync.consts.ClientTypeEnum;
import ink.codflow.sync.exception.AdapterTypeNotFoundException;
import ink.codflow.sync.transfer.oss.OssObject;

public enum AdapterTypeEnum {

	VFS2OSS, VFS2VFS;

	public static AdapterTypeEnum resolve(Class<?> srcClazz, Class<?> destClazz) {

		if (FileObject.class.equals(srcClazz) && FileObject.class.equals(destClazz)) {
			return VFS2VFS;
		}
		if (FileObject.class.equals(srcClazz) && OssObject.class.equals(destClazz)) {
			return VFS2OSS;
		}
		throw new AdapterTypeNotFoundException();
	}

	public static AdapterTypeEnum resolve(ClientTypeEnum srcType, ClientTypeEnum destType) {

		if (ClientTypeEnum.LOCAL.equals(srcType) && ClientTypeEnum.LOCAL.equals(destType)) {
			return VFS2VFS;
		}
		if (ClientTypeEnum.SFTP.equals(srcType) && ClientTypeEnum.LOCAL.equals(destType)) {
			return VFS2VFS;
		}
		if (ClientTypeEnum.LOCAL.equals(srcType) && ClientTypeEnum.SFTP.equals(destType)) {
			return VFS2VFS;
		}
		if (ClientTypeEnum.LOCAL.equals(srcType) && ClientTypeEnum.OSS.equals(destType)) {
			return VFS2OSS;
		}
		if (ClientTypeEnum.SFTP.equals(srcType) && ClientTypeEnum.OSS.equals(destType)) {
			return VFS2OSS;
		}
		throw new AdapterTypeNotFoundException();

	}
}
