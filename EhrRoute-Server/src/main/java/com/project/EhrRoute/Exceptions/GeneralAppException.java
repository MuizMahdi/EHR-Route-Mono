package com.project.EhrRoute.Exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.ACCEPTED)
public class GeneralAppException extends RuntimeException
{
    public GeneralAppException(String message) {
        super(message);
    }

    public GeneralAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
