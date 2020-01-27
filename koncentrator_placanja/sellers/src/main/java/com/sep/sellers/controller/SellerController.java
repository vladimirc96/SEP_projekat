package com.sep.sellers.controller;

import com.sep.sellers.dto.ActiveBillingPlanDTO;
import com.sep.sellers.dto.ApproveDTO;
import com.sep.sellers.dto.SellerDTO;
import com.sep.sellers.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.NotSupportedException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/sellers")
public class SellerController {

    @Autowired
    SellerService sellerService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getSeller(@PathVariable long id) {
        return new ResponseEntity<>(sellerService.getSeller(id), HttpStatus.OK);
    }

    @GetMapping(value = "/register")
    public @ResponseBody ResponseEntity initRegistration() {
        return new ResponseEntity(sellerService.initRegistration(), HttpStatus.CREATED);
    }



    @PostMapping(value = "/register")
    public @ResponseBody ResponseEntity postRegistration(@RequestBody SellerDTO sellerDTO) {
        try {
            return new ResponseEntity(sellerService.postRegistration(sellerDTO), HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity("Given payment id is not valid.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/register/approve")
    public @ResponseBody ResponseEntity approveRegistration(@RequestBody ApproveDTO approveDTO) {
        try {
            sellerService.approveRegistration(approveDTO);
            return new ResponseEntity(HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/createPlan")
    public @ResponseBody ResponseEntity createPlan(@RequestBody ActiveBillingPlanDTO dto) {
        return new ResponseEntity(sellerService.createPlan(dto), HttpStatus.CREATED);
    }

    @GetMapping(value = "/getActivePlan/{id}")
    public @ResponseBody ResponseEntity initRegistration(@PathVariable("id") long id) {
        return new ResponseEntity(sellerService.getActivePlan(id), HttpStatus.CREATED);
    }

}
