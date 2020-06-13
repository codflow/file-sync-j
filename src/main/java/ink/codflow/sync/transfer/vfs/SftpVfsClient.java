package ink.codflow.sync.transfer.vfs;

import java.io.File;
import java.net.URI;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.sftp.IdentityInfo;
import org.apache.commons.vfs2.provider.sftp.IdentityProvider;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

import ink.codflow.sync.consts.ClientTypeEnum;
import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.security.LocalSecurityManager;
import ink.codflow.sync.transfer.Client;

public class SftpVfsClient implements Client<FileObject> {

	public static final ClientTypeEnum TYPE = ClientTypeEnum.SFTP;

	boolean remote = true;
	// StandardFileSystemManager fileSystemManager;
	FileSystemManager manager;
	FileSystemOptions opts;
	String prefix = "sftp://";
	String userPrefx = null;
	String host;

	public SftpVfsClient(String host) {
		this.host = host;
	}

	public void addPublicKeyIdentity(String user, String privateKeyPath) throws FileException {
		opts = new FileSystemOptions();
		try {
			String username = user;
			this.userPrefx = username + "@";
			File privateKeyFile = new File(privateKeyPath);
			IdentityProvider id = new IdentityInfo(privateKeyFile);
			SftpFileSystemConfigBuilder sftpConfigBuilder = SftpFileSystemConfigBuilder.getInstance();
			sftpConfigBuilder.setIdentityProvider(opts, id);
			sftpConfigBuilder.setUserDirIsRoot(opts, false);
		} catch (FileSystemException e) {
			throw new FileException(e);
		}
	}

	public void addPasswordIdentity(String domain, String user, String password) throws FileException {
		StaticUserAuthenticator auth = new StaticUserAuthenticator(domain, user, password);
		opts = new FileSystemOptions();
		try {
			DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
			SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);

		} catch (FileSystemException e) {
			throw new FileException(e);
		}
	}
	

	public FileObject[] list(String path) throws FileException {
		try {
			manager = VFS.getManager();
			StringBuilder sb = new StringBuilder(prefix).append(host).append(path);
			String uri = sb.toString();
			FileObject fo = VFS.getManager().resolveFile(uri, opts);
			FileObject[] files = fo.getChildren();
			return files;
		} catch (FileSystemException e) {
			throw new FileException(e);
		}

	}

	public FileObject resolve(String path) throws FileException {
		try {
			FileObject fo = VFS.getManager().resolveFile(getPath(path), opts);
			return fo;
		} catch (FileSystemException e) {
			throw new FileException(e);
		}


	}

	URI getUri(String absolutePath) {
		StringBuilder sb = new StringBuilder(prefix);
		if (userPrefx != null) {
			sb.append(userPrefx);
		}
		sb.append(host).append(absolutePath);
		return URI.create(sb.toString());
	}

	String getPath(String absolutePath) {
		StringBuilder sb = new StringBuilder(prefix);
		if (userPrefx != null) {
			sb.append(userPrefx);
		}
		sb.append(host).append(absolutePath);
		return sb.toString();
	}

	@Override
	public boolean isRemote() {
		return true;
	}

	@Override
	public AbstractObjectWapper<FileObject> resolveWapper(String uri) {

		VfsObjectWapper wapper = new VfsObjectWapper(uri);
		return wapper;
	}

}
