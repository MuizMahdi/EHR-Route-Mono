package com.project.EhrRoute.Exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class NullUserNetworkException extends RuntimeException
{
    public NullUserNetworkException(String message) {
        super(message);
    }

    public NullUserNetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
