package de.soulworks.error;

import java.time.LocalDateTime;

public class SoulworksError extends Throwable {
    private ErrorType errorType;
    private LocalDateTime timestamp;

    public enum ErrorType {
        GENERAL,
        SYSTEM,
        GAMEPLAY,
        NETWORK,
        DEBUG
    }

    public SoulworksError(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
        this.timestamp = LocalDateTime.now();
    }

    public SoulworksError(String message, ErrorType errorType, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Additional methods can be added here for error handling or analysis.
}


