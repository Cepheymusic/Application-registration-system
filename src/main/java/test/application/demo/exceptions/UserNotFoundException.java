package test.application.demo.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String massage) {
        super(massage);
    }

}
