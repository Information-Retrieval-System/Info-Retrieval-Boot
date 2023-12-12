package ca.uwaterloo.cs451.indexer.exceptions;

public class ValueNotFoundException extends Exception {
    public ValueNotFoundException() {
        super("Value not found");
    }

    public ValueNotFoundException(String message) {
        super(message);
    }
}