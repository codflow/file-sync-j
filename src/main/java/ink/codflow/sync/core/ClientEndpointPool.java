package ink.codflow.sync.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import ink.codflow.bo.AuthenticationBO;
import ink.codflow.bo.ClientEndpointBO;
import ink.codflow.security.LocalSecurityManager;
import ink.codflow.sync.consts.AuthDataType;
import ink.codflow.sync.consts.AuthenticationType;
import ink.codflow.sync.consts.ClientTypeEnum;
import ink.codflow.transfer.vfs.Client;
import ink.codflow.transfer.vfs.LocalVfsClient;
import ink.codflow.transfer.vfs.SftpVfsClient;

public class ClientEndpointPool {

	Map<Integer, ClientEndpoint<?>> endpointMap = new ConcurrentHashMap<>();

	// String root;

	public ClientEndpoint<?> getEndpoint(int clientId) {
		return endpointMap.get(clientId);

	}

	public ClientEndpoint<?> create(ClientEndpointBO endpointBO) {

		ClientEndpoint<?>  clientEndpoint = new ClientEndpoint<>();
		int id = endpointBO.getId();
		Client sftpVfsClient = doCreateClient(endpointBO);
		clientEndpoint.addClient(sftpVfsClient);
		String rootPath = endpointBO.getRootPath();
		clientEndpoint.root = rootPath;
		endpointMap.put(id, clientEndpoint);
		return clientEndpoint;

	}

	Client<?> doCreateClient(ClientEndpointBO endpointBO) {
		ClientTypeEnum type = endpointBO.getType();

		switch (type) {
		case LOCAL:
			return createLocalClient();

		case SFTP:
			AuthenticationBO authenticationBO = endpointBO.getAuthenticationBO();
			return createSftpClient(authenticationBO);
		case OSS:

			break;
		default:
			break;
		}

		return null;
	}

	private LocalVfsClient createLocalClient() {
		LocalVfsClient cLocalVfsClient = new LocalVfsClient();
		return cLocalVfsClient;
	}

	SftpVfsClient createSftpClient(AuthenticationBO auth) {

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
