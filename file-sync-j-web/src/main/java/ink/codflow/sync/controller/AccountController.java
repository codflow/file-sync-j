package ink.codflow.sync.controller;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ink.codflow.sync.core.RestApiResponse;
import ink.codflow.sync.dto.AccountDTO;
import ink.codflow.sync.service.AccountService;

@RestController
@RequestMapping(path = "/user")
public class AccountController {


    AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService){
        this.accountService = accountService;

    }

    RestApiResponse<AccountDTO> getAccount(String id){
        AccountDTO result = accountService.getById(id);
        return RestApiResponse.successResponse(result);
    }

    RestApiResponse<?> createAccount(AccountDTO accountDTO){
        int result = accountService.create(accountDTO);
        return null;
    }
    RestApiResponse<?> updateAccount(AccountDTO accountDTO){
        int result = accountService.update(accountDTO);
        return null;
    }

    RestApiResponse<?> deleteAccount(String id){
        int result = accountService.delete(id);
        return null;
    }
    
}