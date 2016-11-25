package mx.edu.itmorelia.pdm.cajero.models;

/**
 * Created by Martin Alanis on 25/11/2016.
 */

public class ServerResponse {
    private String result;
    private String message;
    private User user;

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
