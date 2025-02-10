package org.wildcodeschool.myblog.exception;

import org.apache.coyote.BadRequestException;
import org.wildcodeschool.myblog.model.Category;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
