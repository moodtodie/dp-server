package com.diploma.server.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ApiController {

    @GetMapping("/check")
    fun check(@RequestParam(value = "name", defaultValue = "World") name : String) : String {
        return String.format("Hello %s!", name);
    }
    
    @GetMapping("/auth")
    fun hello(): ResponseEntity<String> {
        return ResponseEntity.ok("ok!")
    }
}