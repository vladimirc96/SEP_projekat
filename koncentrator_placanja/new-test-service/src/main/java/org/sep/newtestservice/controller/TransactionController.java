package org.sep.newtestservice.controller;

import org.sep.newtestservice.dto.PaymentDTO;
import org.sep.newtestservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class TransactionController {

    @Autowired
    TransactionService _transactionService;

    @PostMapping(path = "/")
    public @ResponseBody
    ResponseEntity createPayment(@RequestBody PaymentDTO pDTO) {
        return new ResponseEntity(_transactionService.createPayment(pDTO), HttpStatus.OK);

    }
}
