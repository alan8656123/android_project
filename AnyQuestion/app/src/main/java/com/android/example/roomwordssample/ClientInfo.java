package com.android.example.roomwordssample;
import java.net.InetAddress;

public class ClientInfo {
    private InetAddress address;
    private int port;
    private String name;
    private int id;

    public ClientInfo(String name, int id, InetAddress address, int port) {
        this.name=name;
        this.id=id;
        this.address=address;
        this.port=port;
    }

    public String getName(){
        return name;
    }
    public int getid(){
        return id;
    }
    public int getport(){
        return port;
    }
    public InetAddress getaddress(){
        return address;
    }
}
