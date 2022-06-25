package com.shashi.bol.mancala.game.v1.controller;

import com.shashi.bol.mancala.game.v1.model.ErrorResponse;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ErrorController extends AbstractErrorController {

    public ErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping
    public ResponseEntity<ErrorResponse> error(HttpServletRequest request) {
        Map<String, Object> errorAttributes = getErrorAttributes(request,ErrorAttributeOptions.of(getALLErrorAttribute()));
        String message = errorAttributes != null ? errorAttributes
                .getOrDefault("message","")
                .toString() : "";

        HttpStatus httpStatus = getStatus(request);
        ErrorResponse errorResponse = getErrorDetails(message, httpStatus);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    private ErrorResponse getErrorDetails(final String message,final HttpStatus httpStatus) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDetails(httpStatus.getReasonPhrase());
        errorResponse.setTimestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        errorResponse.setHttpStatus(httpStatus);
        errorResponse.setMessage(message);
        return errorResponse;
    }

    private ErrorAttributeOptions.Include[] getALLErrorAttribute(){
        return ErrorAttributeOptions.Include.values();
    }
}
