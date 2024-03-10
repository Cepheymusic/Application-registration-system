package test.application.demo.exceptions;

public class RequestStatusException extends RuntimeException {
    public RequestStatusException(String massage) {
        super(massage);
    }
}
