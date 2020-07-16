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

    }

    RestApiResponse<AccountDTO> getAccount(AccountDTO accountDTO){
        return null;
    }

    RestApiResponse<?> createAccount(AccountDTO accountDTO){
        return null;
    }
    RestApiResponse<?> updateAccount(AccountDTO accountDTO){
        return null;
    }

    RestApiResponse<?> deleteAccount(AccountDTO accountDTO){
        return null;
    }
}