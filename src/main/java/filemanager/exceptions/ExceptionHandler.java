package filemanager.exceptions;

/**
 * Interface for show application exceptions.
 */
public interface ExceptionHandler {

    /**
     * Handle exceptions in program.
     *
     * @param message for show
     * @param exception for log file or for show
     */
    void handleException(String message, Exception exception);
}