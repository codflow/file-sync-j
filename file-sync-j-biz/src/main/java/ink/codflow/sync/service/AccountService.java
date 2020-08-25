package ink.codflow.sync.service;

import java.util.Date;

import org.apache.ibatis.logging.Log;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ink.codflow.sync.dto.AccountDTO;
import ink.codflow.sync.dto.AccountPasswordUpdateDTO;
import ink.codflow.sync.entity.AccountDO;
import ink.codflow.sync.manager.AccountManager;
import ink.codflow.sync.qo.AuthenticationQuery;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountService {

    AccountManager accountManager;
    

    public int create(AccountDTO account) {
        
        
        
        String username = account.getUsername();
        
        String password = account.getPassword();
        
        if (username == null || password == null) {
            log.error("create account failed, args are not filled");
            return 0;
        }
        AccountDO accountDO = null;
        boolean exist = accountManager.checkExistByName(username);
        if (exist) {
            log.error("account exist,name:{}", username);
            return 0;
        }
        return accountManager.save(accountDO);
    }
    
    @Transactional
    public int updatePassword(AccountPasswordUpdateDTO accountPasswordUpdateDTO) {
        
        String originPassword = accountPasswordUpdateDTO.getOriginPassword();
        String password = accountPasswordUpdateDTO.getPassword();
        String username = accountPasswordUpdateDTO.getUsername();
        AuthenticationQuery authenticationQuery = new AuthenticationQuery();
        authenticationQuery.setUsername(username);
        authenticationQuery.setPassword(originPassword);
        AccountDO accountDO =  accountManager.getByUsernameAndPassword(authenticationQuery);
        
        if (accountDO != null) {
            String id = accountDO.getId();
            return doUpdatePassword(id,password);
        }
        return 0;
    }
    
    protected int doUpdatePassword(String id,String password) {
        return accountManager.updatePassword(id, password);
    }

    public AccountDTO getById(String id) {
        AccountDO accountDO = accountManager.getById(id);
        return convert2DTO(accountDO);
    }

    public int delete(String id) {
        
        accountManager.deleteAccount(id);
        
        return 0;
    }

    public AccountDTO convert2DTO(AccountDO accountDO) {
        String id = accountDO.getId();
        String type = accountDO.getType();
        //Date updateTime = accountDO.getUpdateTime();
        Date createTime = accountDO.getCreateTime();
        String username = accountDO.getUsername();
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(id);
        accountDTO.setUsername(username);
        accountDTO.setType(type);
        accountDTO.setCreateTime(createTime.getTime());
        return accountDTO;
    }

    /**
     * @description: TODO
     * @param accountDTO
     * @return int
     */
    public int update(AccountDTO accountDTO) {
        // TODO Auto-generated method stub
        return 0;
    }
    
}