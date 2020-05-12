package com.flamexander.netty.example.client;
import java.io.*;
import java.net.Socket;

public class ByteNetwork {
    private static Socket socket;
    private static DataOutputStream out;
    private static DataInputStream in;

    public static void start() {
        try {
            socket = new Socket("localhost", 8189);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


        public static void sendFile(File file ){

          try(FileInputStream fis= new FileInputStream(file)) {

            int nameSize=file.getName().getBytes().length;
                out.writeInt(nameSize);
                out.writeUTF(file.getName());
            long fileSize=file.length();
                out.writeLong(fileSize);
            int n=1024;
            byte[] mass =new byte[n];

            while (fileSize/n>=1){
                fis.read(mass);
                out.write(mass);
                fileSize-=n;
            }
            byte[] tail=new byte[(int)fileSize%n];
            fis.read(tail);
            out.write(tail);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void readFile( ){
        try {

            int nameSize=in.readInt();
            String fileName=in.readUTF();

            long fileSize=in.readLong();
            File file= new File("server_storage/"+fileName);
            FileOutputStream fos= new FileOutputStream(file);

            int n=1024;
            byte[] mass =new byte[n];
            while (fileSize/n>=1){
                in.read(mass);
                fos.write(mass);



            }
            byte[] end=new byte[(int)fileSize%n];
            in.read(end);
            fos.write(end);
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}

