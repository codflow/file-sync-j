package ink.codflow.exception;

public class RemotingException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 375854618326093508L;

    public RemotingException(String message, Throwable cause) {

        super(message, cause);

    }

    public RemotingException() {

    }

    public RemotingException(String msg) {

    }
}
