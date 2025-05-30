package com.example.i18n;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
@Slf4j
public record DemoController(I18nService i18nService) {

    @GetMapping("/hello")
    public String hello() {
        log.info(i18nService.getLogMessage("hello.world.log"));
        return i18nService.getMessage("hello.world");
    }

    @GetMapping("/hello/{name}")
    public String helloSingleArg(@PathVariable String name) {
        log.info(i18nService.getLogMessage("hello.name.log"), name);
        return i18nService.getMessage("hello.name", name);
    }

    @GetMapping("/hello/multi")
    public String helloMultiArg() {
        log.info(i18nService.getLogMessage("hello.multi.log"), "John", 30, "Oakland");
        return i18nService.getMessage("hello.multi", "John", String.valueOf(30), "Oakland");
    }
}