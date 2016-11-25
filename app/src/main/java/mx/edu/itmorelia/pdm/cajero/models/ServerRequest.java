package mx.edu.itmorelia.pdm.cajero.models;

/**
 * Created by Martin Alanis on 25/11/2016.
 */

public class ServerRequest {
    private String operation;
    private User user;

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

