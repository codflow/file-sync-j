package ink.codflow.sync.manager;

import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import ink.codflow.sync.dao.AccountDOMapper;
import ink.codflow.sync.entity.AccountDO;
import ink.codflow.sync.qo.AuthenticationQuery;

@Service
public class AccountManager {

    AccountDOMapper accountDOMapper;

    public int save(AccountDO account) {
        String password  = account.getPassword();
        String encodedPwd = encodePassword(password);
        account.setPassword(encodedPwd);
        return accountDOMapper.insert(account);
    }

    public boolean checkExistByName(String username) {
        AccountDO accountDO = accountDOMapper.selectByName(username);
        return accountDO != null;
    }

    /**
     * @description: TODO
     * @param id
     * @param password void
     */
    public int updatePassword(String id, String password) {
        AccountDO accountDO = new AccountDO();
        accountDO.setId(id);
        accountDO.setPassword(encodePassword(password));
        return accountDOMapper.updateByPrimaryKeySelective(accountDO);

    }

    /**
     * @description: TODO
     * @param id
     * @return AccountDO
     */
    public AccountDO getById(String id) {
        return accountDOMapper.selectByPrimaryKey(id);
    }

    public AccountDO getByUsernameAndPassword(AuthenticationQuery authenticationQuery) {
        
        String rawPassword =authenticationQuery.getPassword();
        String encodedPwd = encodePassword(rawPassword);
        authenticationQuery.setPassword(encodedPwd);
        AccountDO accountDO = accountDOMapper.getByAuth(authenticationQuery);
        return accountDO;
    }
    
    String encodePassword(String password) {
       return DigestUtils.md5DigestAsHex(password.getBytes());
    }

    /**
     * @description: TODO
     * @param id void
     */
    public void deleteAccount(String id) {
        // TODO Auto-generated method stub
        
    }
    
}