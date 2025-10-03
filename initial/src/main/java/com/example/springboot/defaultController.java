package com.example.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class defaultController {

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<InputStreamResource> sayHello() {
        InputStream in;
        MediaType contentType;

        contentType = MediaType.TEXT_HTML;
        in = getClass().getResourceAsStream("/templates/default.html");
        return ResponseEntity.ok()
                .contentType(contentType)
                .body(new InputStreamResource(in));
    }

}