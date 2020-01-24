package com.sep.paypalservice.controller;

import com.sep.paypalservice.dto.PPClientDTO;
import com.sep.paypalservice.repository.ClientsRepository;
import com.sep.paypalservice.service.ClientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/registration")
public class PPRegistrationController {

    @Autowired
    private ClientsService clientsService;

    @PostMapping(path = "/")
    public @ResponseBody
    ResponseEntity registerPayPal(@RequestBody PPClientDTO ppcDTO) {
        try {
            return new ResponseEntity(clientsService.registerSeller(ppcDTO), HttpStatus.OK);
        } catch (AccessDeniedException ade) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }
}
