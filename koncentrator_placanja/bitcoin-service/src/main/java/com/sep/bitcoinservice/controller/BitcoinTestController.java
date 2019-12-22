package com.sep.bitcoinservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/bitcoin-test")
public class BitcoinTestController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getTestResponse()
    {
        return new ResponseEntity<>("All OK.",HttpStatus.OK);
    }
}
