package com.project.EMRChain.Exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceEmptyException extends RuntimeException
{
    public ResourceEmptyException(String message) {
        super(message);
    }

    public ResourceEmptyException(String message, Throwable cause) {
        super(message, cause);
    }
}
