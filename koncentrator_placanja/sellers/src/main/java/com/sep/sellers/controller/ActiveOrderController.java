package com.sep.sellers.controller;

import com.sep.sellers.dto.ActiveOrderDTO;
import com.sep.sellers.dto.FinalizeOrderDTO;
import com.sep.sellers.dto.InitOrderRequestDTO;
import com.sep.sellers.dto.InitOrderResponseDTO;
import com.sep.sellers.service.ActiveOrderService;
import net.bytebuddy.dynamic.TypeResolutionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/active-order")
public class ActiveOrderController {

    @Autowired
    private ActiveOrderService activeOrderService;

    @RequestMapping(value = "/init", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<InitOrderResponseDTO> init(@RequestBody InitOrderRequestDTO initOrderRequestDTO){
        return new ResponseEntity<>(activeOrderService.create(initOrderRequestDTO), HttpStatus.OK);
    }

    @RequestMapping(value = "/{activeOrderId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<ActiveOrderDTO> get(@PathVariable("activeOrderId") String activeOrderId){
        return new ResponseEntity<>(activeOrderService.findOneById(Long.parseLong(activeOrderId)), HttpStatus.OK);
    }

    @RequestMapping(value = "/finalize", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity finalizeActiveOrder(@RequestBody FinalizeOrderDTO foDTO){
        activeOrderService.finalizeOrder(foDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/status", method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity setActiveOrderStatus(@RequestBody ActiveOrderDTO aoDTO){
        try {
            activeOrderService.setActiveOrderStatus(aoDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException ise) {
            return new ResponseEntity(ise.getMessage(), HttpStatus.CONFLICT);
        }
    }




}
