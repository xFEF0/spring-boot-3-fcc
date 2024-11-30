package com.xfef0.runnerz.run;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RunNotFoundException extends RuntimeException {

    public RunNotFoundException() {
        super("Run Not Found");
    }
}
