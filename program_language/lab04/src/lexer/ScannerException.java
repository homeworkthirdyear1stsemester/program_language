package lexer;


public class ScannerException extends RuntimeException {//Exception 발생해 주는 코드
    private static final long serialVersionUID = -5564986423129197718L;

    public ScannerException() {
        super();
    }

    public ScannerException(String details) {
        super(details);
    }
}
