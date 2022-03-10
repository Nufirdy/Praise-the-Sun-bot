package ent.otego.classickiryushabot.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/callback")
@Slf4j
public class CallbackController {

    @PostMapping
    public void vkCallback(@RequestBody JsonNode json) {
        log.info(json.toString());
    }
}
