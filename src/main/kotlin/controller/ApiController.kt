package com.diploma.server.controller

import com.diploma.server.service.ApiService
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.File

@RestController
@RequestMapping("/api")
class ApiController (
    private val service: ApiService
){

    @GetMapping("/check")
    fun check(@RequestParam(value = "name", defaultValue = "World") name: String): String {
        return String.format("Hello %s!", name)
    }

    @GetMapping("/auth")
    fun hello(): ResponseEntity<String> {
        return ResponseEntity.ok("ok!")
    }

    @GetMapping("/report")
    fun report(): ResponseEntity<Resource> {
        val file = service.generateReport()
        val resource = InputStreamResource(file.inputStream())

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${file.name}")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .contentLength(file.length())
            .body(resource)
    }

//    @GetMapping("/report")
//    fun report(): File {
//        return service.generateReport()
//    }
}