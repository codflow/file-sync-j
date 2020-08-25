package ink.codflow.sync.service;

import java.util.List;

import ink.codflow.sync.dto.ClientDTO;
import ink.codflow.sync.entity.AccountDO;
import ink.codflow.sync.entity.ClientDO;
import ink.codflow.sync.manager.AccountManager;
import ink.codflow.sync.manager.ClientManager;

public class ClientService {

    ClientManager clientManager;

    AccountManager accountManager;

    List<ClientDTO> selectClientByAccountId(String accountId) {

        return null;
    }

    int updateClient(ClientDTO clientDTO) {
        return 0;

    }

    int deleteById(String clientId) {
        return 0;
    }

    int create(ClientDTO clientDTO) {

        String accountId = clientDTO.getAccountId();
        AccountDO accountDO = accountManager.getById(accountId);
        if (accountDO != null) {
            String name = clientDTO.getName();
            
            ClientDO clientDO = new ClientDO();
            clientDO.setName(name);
            
            clientManager.create(clientDO);
        }

        return 0;
    }

}