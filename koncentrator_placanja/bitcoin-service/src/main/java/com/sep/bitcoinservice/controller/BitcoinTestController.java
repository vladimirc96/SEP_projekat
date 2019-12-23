package com.sep.bitcoinservice.controller;

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

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getTestResponse(HttpServletRequest request)
    {

        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            System.out.println(headers.nextElement());
        }

        System.out.println(request.getHeader("user-agent"));
        System.out.println(request.getHeader("user-agent"));


        return new ResponseEntity<>("All Ok.", HttpStatus.OK);
    }
}
