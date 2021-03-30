package socialmedia;

import java.io.IOException;

public class Account{
    private int id;
    private String handle; //username
    private String description;

     public void createAccount(String handle, String description, int id) throws  IllegalHandleException, InvalidHandleException{
        this.handle = handle;
        this.description = description;
        this.id = id;
     }

    public String getHandle() {
        return handle;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }
}
