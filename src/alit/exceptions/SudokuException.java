package alit.exceptions;

/**
 * User: Alexander Litvinenko
 * Date: 6/9/11
 * Time: 10:30 AM
 */
public class SudokuException extends Exception{

    public SudokuException(String message) {
        super(message);
    }

    public SudokuException(String message, Throwable cause) {
        super(message, cause);
    }

    public SudokuException(Throwable cause) {
        super(cause);
    }
}
