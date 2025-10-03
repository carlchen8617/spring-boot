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
public class HelloController {
	/*
	@GetMapping(
			value = "/",
			produces = MediaType.IMAGE_GIF_VALUE
	)
	public @ResponseBody byte[] getImage() throws IOException {
		InputStream in = getClass()
				.getResourceAsStream("/images/paris-games-begin-6753651837110444.3-law.gif");
		return IOUtils.toByteArray(in);
	}
   */

    @GetMapping("/images")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getImageDynamicType(@RequestParam("jpg") int jpg) {
        MediaType contentType;
        InputStream in;

        switch(jpg) {
            case 1:
                contentType =  MediaType.IMAGE_JPEG;
                in =  getClass().getResourceAsStream("/images/welcome.jpeg");
                break;
            case 2:
                contentType =  MediaType.IMAGE_PNG;
                in = getClass().getResourceAsStream("/images/bird.png");
                break;
            case 3:
                contentType =  MediaType.IMAGE_GIF;
                in = getClass().getResourceAsStream("/images/paris-games-begin-6753651837110444.3-law.gif");
                break;
            default:
                contentType = MediaType.TEXT_HTML;
                in = getClass().getResourceAsStream("/templates/welcome.html");
        }
        /* MediaType contentType = jpg ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG;
        InputStream in = jpg ?
                getClass().getResourceAsStream("/images/welcome.jpeg") :
                getClass().getResourceAsStream("/images/images/bird.png");
        */
        return ResponseEntity.ok()
                .contentType(contentType)
                .body(new InputStreamResource(in));
    }
}
