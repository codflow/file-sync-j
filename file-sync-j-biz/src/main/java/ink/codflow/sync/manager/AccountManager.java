package ink.codflow.sync.manager;

import org.springframework.stereotype.Service;

import ink.codflow.sync.dao.AccountDOMapper;
import ink.codflow.sync.dao.AccountMapper;

@Service
public class AccountManager {

    AccountDOMapper accountDOMapper;

     public void save(AccountDO account){
        accountDOMapper.insert(account);
     }
    
}