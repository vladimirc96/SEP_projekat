package com.sep.bankservice.controller;

import com.sep.bankservice.dto.CustomerDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
public class BankRegistrationController {

    @PostMapping(path = "/")
    public @ResponseBody
    ResponseEntity registerBank(@RequestBody CustomerDTO cDTO) {
        System.out.println(cDTO.getMerchantPassword());
        return new ResponseEntity(HttpStatus.OK);
    }
}
