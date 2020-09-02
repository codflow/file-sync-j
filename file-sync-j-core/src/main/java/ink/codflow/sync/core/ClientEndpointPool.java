package ink.codflow.sync.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ink.codflow.sync.manager.*;
import ink.codflow.sync.consts.AuthDataType;
import ink.codflow.sync.consts.AuthenticationType;
import ink.codflow.sync.consts.ClientTypeEnum;
import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.security.LocalSecurityManager;
import ink.codflow.sync.transfer.Client;
import ink.codflow.sync.transfer.oss.OssAuthentication;
import ink.codflow.sync.transfer.oss.OssClient;
import ink.codflow.sync.transfer.vfs.LocalVfsClient;
import ink.codflow.sync.transfer.vfs.SftpVfsClient;

public class ClientEndpointPool {

	Map<Integer, ClientEndpoint<?>> endpointMap = new ConcurrentHashMap<>();

	public ClientEndpoint<?> getEndpoint(int clientId) {
		return endpointMap.get(clientId);

	}

	public ClientEndpoint<?> create(Endpoint endpointBO) throws FileException {

		ClientEndpoint<?> clientEndpoint = new ClientEndpoint<>();
		int id = endpointBO.getId();
		@SuppressWarnings("rawtypes")
		Client client = doCreateClient(endpointBO);
		clientEndpoint.addClient(client);
		String rootPath = endpointBO.getRootPath();
		clientEndpoint.root = rootPath;
		clientEndpoint.setId(id);
		endpointMap.put(id, clientEndpoint);
		return clientEndpoint;

	}

	Client<?> doCreateClient(Endpoint endpointBO) throws FileException {
		ClientTypeEnum type = endpointBO.getType();

		switch (type) {
		case LOCAL:
			return createLocalClient();

		case SFTP:
			Authentication sftpAuth = endpointBO.getAuthentication();
			return createSftpClient(sftpAuth);
		case OSS:

			Authentication ossAuth = endpointBO.getAuthentication();
			return createOssClient(ossAuth);

		default:
			break;
		}

		return null;
	}

	private Client<?> createOssClient(Authentication ossAuth) {
		
		String endpoint = ossAuth.getParam(AuthDataType.HOST);
		String ak = ossAuth.getParam(AuthDataType.AK);
		String sk = ossAuth.getParam(AuthDataType.SK);
		String bucketName = ossAuth.getParam(AuthDataType.BKT);
		OssAuthentication authentication = new OssAuthentication();
		authentication.setAccessKeyId(ak);
		authentication.setAccessKeySecret(sk);
		authentication.setEndpoint(endpoint);
		authentication.setBucketName(bucketName);
		OssClient client = new OssClient(authentication);
		return client;
	}

	private LocalVfsClient createLocalClient() {
		LocalVfsClient cLocalVfsClient = new LocalVfsClient();
		return cLocalVfsClient;
	}

	SftpVfsClient createSftpClient(Authentication auth) throws FileException {

		AuthenticationType authType = auth.getAuthType();
		String host = auth.getParam(AuthDataType.HOST);
		String userName = auth.getParam(AuthDataType.USERNAME);

		switch (authType) {
		case PASSWORD:

			String password = auth.getParam(AuthDataType.PASSWD);
			SftpVfsClient sftpVfsClient0 = new SftpVfsClient(host);
			sftpVfsClient0.addPasswordIdentity(null, userName, password);
			return sftpVfsClient0;
		case PUBKEY:
			String privateKeyPath = LocalSecurityManager.seekLocalUserPrivateKeyPath();
			SftpVfsClient sftpVfsClient = new SftpVfsClient(host);
			sftpVfsClient.addPublicKeyIdentity(userName, privateKeyPath);
			return sftpVfsClient;
		default:
			return null;
		}
	}

}
