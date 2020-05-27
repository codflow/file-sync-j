package ink.codflow.sync.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ink.codflow.bo.AuthenticationBO;
import ink.codflow.bo.ClientEndpointBO;
import ink.codflow.sync.consts.AuthDataType;
import ink.codflow.sync.consts.AuthenticationType;
import ink.codflow.transfer.fsclient.SftpVfsClient;

public class ClientEndpointPool {

    Map<Integer, ClientEndpoint> endpointMap = new ConcurrentHashMap<Integer, ClientEndpoint>();

    String root;

    public ClientEndpoint getEndpoint(int clientId) {
        return endpointMap.get(clientId);

    }

    public ClientEndpoint create(ClientEndpointBO endpointBO) {
        // TODO re

        ClientEndpoint clientEndpoint = new ClientEndpoint();
        int id = endpointBO.getId();
        AuthenticationBO auth = endpointBO.getAuthenticationBO();
        AuthenticationType authType = auth.getAuthType();
        String authId = auth.getId();
        if (authType.equals(AuthenticationType.PASSWORD)) {
            String password = auth.getParam(AuthDataType.PASSWD);
            String userName = auth.getParam(AuthDataType.USERNAME);
            String host = auth.getParam(AuthDataType.HOST);
            SftpVfsClient sftpVfsClient = new SftpVfsClient(host);
            sftpVfsClient.addPasswordIdentity(null, userName, password);
            clientEndpoint.setId(id);
            clientEndpoint.addClient(sftpVfsClient);
            
        }
        String rootPath = endpointBO.getRootPath();
        this.root = rootPath;

        endpointMap.put(id, clientEndpoint);
        return clientEndpoint;

    }

}
