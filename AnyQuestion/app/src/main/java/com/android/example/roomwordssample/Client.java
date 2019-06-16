package com.android.example.roomwordssample;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class Client {

    private static DatagramSocket socket;
    private InetAddress address;
    private int port;
    private String name;

    private static boolean running;


    public Client(String name, String address, int port) {
        try {
            this.name=name;
            this.address= InetAddress.getByName(address);
            this.port=port;
            socket = new DatagramSocket();

            running=true;
            listen();
            send("\\con:"+name);
            Log.d("ccccccc","Send fail,");

        }catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void send(String message) {
        if (!message.startsWith("\\")) {
            message = name + ": " + message;
        }

        message += "\\e";
        final byte[] data = message.getBytes();

        Thread sendThread=new Thread("ChatProgram Sender") {
            public void run() {
                try {
                    DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                    socket.send(packet);
                    Log.d("ccccccc", "Send Message To," + address.getHostAddress() + ":" + port);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("MYAPP", "exception", e);
                }
            }
        };sendThread.start();
    }


    public void listen() {
        Thread listenThread=new Thread("ChatProgram Listener") {
            public void run() {
                try{
                    while(running) {
                        byte[] data =new byte[1024];
                        DatagramPacket packet=new DatagramPacket(data,data.length);
                        socket.receive(packet);

                        String message = new String(data);
                        if(message.startsWith("\\W")) {
                            String temp_S=message.substring(message.indexOf("W"));
                            String name=temp_S.substring(1,temp_S.indexOf("\\Q"));

                            temp_S=temp_S.substring(temp_S.indexOf("Q"));
                            String que=temp_S.substring(1,temp_S.indexOf("\\N"));


                            Word word=new Word(name,que,0);
                            MainActivity.mWordViewModel.insert(word);

                        }if(message.startsWith("\\U")) {
                            String temp_S=message.substring(message.indexOf("U"));
                            String name=temp_S.substring(1,temp_S.indexOf("\\Q"));

                            temp_S=temp_S.substring(temp_S.indexOf("Q"));
                            String que=temp_S.substring(1,temp_S.indexOf("\\N"));


                            temp_S=temp_S.substring(temp_S.indexOf("N"));

                            temp_S=temp_S.substring(1,temp_S.indexOf("\\e"));
                            int num=Integer.parseInt(temp_S);

                            Word word=new Word(name,que,num);
                            MainActivity.mWordViewModel.updateWore(word);

                        }else {
                            message = message.substring(0, message.indexOf("\\e"));

                            //Manage message
                            if (!isCommand(message, packet)) {
                                //PRINT MESSAGE
                                MainActivity.printToconsole(message);

                            }
                        }
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                    Log.e("MYAPP", "exception", e);
                }

            }
        };listenThread.start();
    }
    private static boolean isCommand(String message, DatagramPacket packet) {
        if(message.startsWith("\\con:")) {
            //RUN CONNECTION CODE
            return true;
        }
        return false;
    }
}
