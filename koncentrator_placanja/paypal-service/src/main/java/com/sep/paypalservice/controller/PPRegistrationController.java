package com.sep.paypalservice.controller;

import com.sep.paypalservice.dto.PPClientDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
public class PPRegistrationController {

    @PostMapping(path = "/")
    public @ResponseBody
    ResponseEntity registerPayPal(@RequestBody PPClientDTO ppcDTO) {
        System.out.println(ppcDTO.getClientSecret());
        return new ResponseEntity(HttpStatus.OK);
    }
}
