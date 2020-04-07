package com.mertbarutcuoglu.CourseAnalyzer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotEnoughDataException extends Exception {
    public NotEnoughDataException() {
        super("There is not enough data for the given course!");
    }
}
