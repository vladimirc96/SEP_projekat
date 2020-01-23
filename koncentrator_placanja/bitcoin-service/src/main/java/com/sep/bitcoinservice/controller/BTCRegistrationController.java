package com.sep.bitcoinservice.controller;

import com.sep.bitcoinservice.dto.SellerDTO;
import com.sep.bitcoinservice.service.SellerService;
import com.sep.bitcoinservice.service.contracts.ISellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/registration")
public class BTCRegistrationController {

    @Autowired
    ISellerService _sellerService;

    @PostMapping(path = "/")
    public @ResponseBody
    ResponseEntity registerBitcoin(@RequestBody SellerDTO sDTO) {
        try {
            return new ResponseEntity(_sellerService.registerSeller(sDTO), HttpStatus.OK);
        } catch (AccessDeniedException ade) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

    }
}
