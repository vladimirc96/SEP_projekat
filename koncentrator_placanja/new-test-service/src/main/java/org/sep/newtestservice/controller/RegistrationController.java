package org.sep.newtestservice.controller;


import org.sep.newtestservice.dto.SellerDTO;
import org.sep.newtestservice.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    SellerService _sellerService;

    @PostMapping(path = "/")
    public @ResponseBody
    ResponseEntity registerBitcoin(@RequestBody SellerDTO sDTO) {
        try {
            return new ResponseEntity(_sellerService.registerSeller(sDTO), HttpStatus.OK);
        } catch (AccessDeniedException ade) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

    }


    @GetMapping(path = "/")
    public @ResponseBody ResponseEntity test() {
        return new ResponseEntity("hej", HttpStatus.OK);
    }
}
