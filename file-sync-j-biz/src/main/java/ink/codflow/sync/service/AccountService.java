package ink.codflow.sync.service;

import org.springframework.stereotype.Service;

import ink.codflow.sync.manager.AccountManager;

@Service
public class AccountService {


    AccountManager accountManager;

    public void create(AccountDTO account){
        boolean exist = accountManager.checkExist();
        if (exist) {
            
        }
        accountManager.save();
    }




    
}