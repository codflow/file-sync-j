package ink.codflow.sync.transfer.vfs;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;

import ink.codflow.sync.consts.ClientTypeEnum;
import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.transfer.Client;

public class LocalVfsClient implements Client<FileObject> {

	public static final ClientTypeEnum TYPE = ClientTypeEnum.LOCAL;

	FileSystemManager fsManager;

	public LocalVfsClient() {
		StandardFileSystemManager standardFileSystemManager = new StandardFileSystemManager();
		try {
			standardFileSystemManager.init();
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
		fsManager = standardFileSystemManager;
	}

	@Override
	public FileObject[] list(String path) throws FileException {

		try {
			return fsManager.resolveFile(path).getChildren();
		} catch (FileSystemException e) {
			throw new FileException(e);
		}
	}

	public FileObject resolve(String path) throws FileException {
		try {
			FileObject dst = fsManager.resolveFile(path);

			return dst;
		} catch (FileSystemException e) {
			throw new FileException();
		}
	}

	@Override
	public boolean isRemote() {
		return false;
	}

	@Override
	public AbstractObjectWapper<FileObject> resolveWapper(String uri) {

		VfsObjectWapper wapper = new VfsObjectWapper(uri);
		return wapper;

	}

}
