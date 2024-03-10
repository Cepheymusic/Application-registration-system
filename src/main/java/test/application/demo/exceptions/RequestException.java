package test.application.demo.exceptions;

public class RequestException extends RuntimeException {
    public RequestException(String massage) {
        super(massage);
    }
}
