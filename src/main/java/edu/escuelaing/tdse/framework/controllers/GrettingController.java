package edu.escuelaing.tdse.framework.controllers;

import edu.escuelaing.tdse.framework.annotations.GetMapping;
import edu.escuelaing.tdse.framework.annotations.RequestParam;
import edu.escuelaing.tdse.framework.annotations.RestController;

@RestController
public class GrettingController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "world") String name) {
        return "Hello " + name + " !";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Spring Boot!";
    }

}