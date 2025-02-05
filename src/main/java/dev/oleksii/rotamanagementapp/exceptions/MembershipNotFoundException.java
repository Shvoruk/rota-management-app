package dev.oleksii.rotamanagementapp.exceptions;

public class MembershipNotFoundException extends RuntimeException {
    public MembershipNotFoundException(String message) {
        super(message);
    }
}
