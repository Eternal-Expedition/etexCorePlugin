package ur.anusdestroyer.etexcoreplugin.features.itemmanager;

import java.sql.Timestamp;

public class StashItem {

    /*

    Object used by Stash class

     */


    private int id;
    private byte[] itemStack;
    private Timestamp timestamp;

    public StashItem(int id, byte[] itemStack, Timestamp timestamp) {
        this.id = id;
        this.itemStack = itemStack;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public byte[] getItemStack() { return itemStack; }
    public Timestamp getTimestamp() { return timestamp; }
}
