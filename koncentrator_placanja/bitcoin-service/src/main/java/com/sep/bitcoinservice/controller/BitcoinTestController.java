package com.sep.bitcoinservice.controller;

import com.sep.bitcoinservice.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@RestController
@RequestMapping("/bitcoin-test")
public class BitcoinTestController {

    @Autowired
    CryptoService cryptoService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getTestResponse(HttpServletRequest request)
    {
        return new ResponseEntity<>("All Ok.", HttpStatus.OK);
    }
}
