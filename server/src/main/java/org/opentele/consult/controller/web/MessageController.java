package org.opentele.consult.controller.web;

import org.opentele.consult.framework.Translator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/message")
public class MessageController {
    @GetMapping()
    public String getMessage(@RequestParam("msg") String msg) {
        return Translator.toLocale(msg);
    }
}
