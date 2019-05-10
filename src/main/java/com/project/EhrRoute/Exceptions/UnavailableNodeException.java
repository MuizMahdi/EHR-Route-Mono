package com.project.EhrRoute.Exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class UnavailableNodeException extends RuntimeException
{
    public UnavailableNodeException(String message) {
        super(message);
    }

    public UnavailableNodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
