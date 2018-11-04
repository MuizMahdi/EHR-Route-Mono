package com.project.EMRChain.Exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/*
*   APIs will throw exceptions if the request is not valid or some unexpected situation occurs,
*   and a respond is made with different HTTP status codes for different types of exceptions.
*/


@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // The corresponding Response HTTP Status for the exception.
public class InternalErrorExcpetion extends RuntimeException
{
    public InternalErrorExcpetion(String message) {
        super(message); // Super calls the constructor of the extended class (RuntimeException's constructor).
    }

    public InternalErrorExcpetion(String message, Throwable cause) {
        super(message, cause); // Same as above, but with a Throwable cause.
    }
}
