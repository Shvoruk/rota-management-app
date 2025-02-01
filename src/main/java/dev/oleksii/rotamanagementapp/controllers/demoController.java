package dev.oleksii.rotamanagementapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pizdec")
public class demoController {

    @GetMapping
    public ResponseEntity<String> yaEbanat(){
        return ResponseEntity.ok("Ya tebe lybu!");
    }
}
