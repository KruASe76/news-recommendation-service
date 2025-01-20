package org.hsse.news.database.user.exceptions;

public class EmailConflictException extends RuntimeException {
    public EmailConflictException(final Throwable cause) {
        super(cause);
    }
}
