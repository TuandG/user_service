package org.example.user_service.utils;

import org.example.user_service.exception.AppException;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

public class ExceptionUtils {
    public static final HashMap<Error, AppException> ERROR_MESSAGES = new HashMap<>();

    public enum Error {
        E_ROLE_NOT_FOUND,
        E_USER_NOT_FOUND,
        E_USER_DELETED,
        E_UNAUTHORIZED,
        E_INTERNAL_SERVER,
    }

    static {
        ERROR_MESSAGES.put(Error.E_ROLE_NOT_FOUND, new AppException("Role is not found", HttpStatus.NOT_FOUND));
        ERROR_MESSAGES.put(Error.E_UNAUTHORIZED, new AppException("Unauthorized", HttpStatus.UNAUTHORIZED));
        ERROR_MESSAGES.put(Error.E_USER_NOT_FOUND, new AppException("User is not found", HttpStatus.NOT_FOUND));
        ERROR_MESSAGES.put(Error.E_USER_DELETED, new AppException("User is deleted!", HttpStatus.BAD_REQUEST));
        ERROR_MESSAGES.put(Error.E_INTERNAL_SERVER, new AppException("Internal server", HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
