package com.sep.sellers.controller;

import com.sep.sellers.dto.SellerDTO;
import com.sep.sellers.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sellers")
public class SellerController {

    @Autowired
    SellerService sellerService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getSeller(@PathVariable long id) {
        return new ResponseEntity<>(sellerService.getSeller(id), HttpStatus.OK);
    }
}
