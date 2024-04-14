package me.uranusdestroyer.etexcoreplugin.features.itemmanager;

public class StashItem {

    /*

    Object used by Stash class

     */


    private int id;
    private byte[] itemStack;
    private Long timestamp;

    public StashItem(int id, byte[] itemStack, Long timestamp) {
        this.id = id;
        this.itemStack = itemStack;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public byte[] getItemStack() { return itemStack; }
    public Long getTimestamp() { return timestamp; }
}
