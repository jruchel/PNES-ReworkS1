package org.ekipa.pnes.api.controllers;

import org.ekipa.pnes.api.configs.security.Controller;
import org.ekipa.pnes.api.configs.security.SecuredMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class HelloController extends Controller {

    @SecuredMapping(path = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "Hello";
    }
}
