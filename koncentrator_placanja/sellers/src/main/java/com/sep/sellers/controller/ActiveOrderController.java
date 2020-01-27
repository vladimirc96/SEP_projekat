package com.sep.sellers.controller;

import com.sep.sellers.dto.InitOrderRequestDTO;
import com.sep.sellers.dto.InitOrderResponseDTO;
import com.sep.sellers.service.ActiveOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/active-order")
public class ActiveOrderController {

    @Autowired
    private ActiveOrderService activeOrderService;

    @RequestMapping(value = "/init", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<InitOrderResponseDTO> init(@RequestBody InitOrderRequestDTO initOrderRequestDTO){
        return new ResponseEntity<>(activeOrderService.create(initOrderRequestDTO), HttpStatus.OK);
    }

}
