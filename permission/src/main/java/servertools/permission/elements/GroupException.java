package servertools.permission.elements;

public class GroupException extends Exception {

    String message;

    public GroupException(String message) {

        this.message = message;
    }

    @Override
    public String toString() {

        return message;
    }
}
