package com.example.passportdemo.socket;

import java.io.*;
import java.net.Socket;

/**
 * SocketTest
 *
 * @author muxiaorui
 * @create 2018-06-07 10:35
 **/
public class SocketTest {
    public static void main(String[] args) {
        for(int i=0;i<5;i++) {
            writeToServer("localhost", 22);
        }
    }

    /**
     * 向服务器写数据
     * @param serverHost socket服务器地址
     * @param port 端口
     * @author zzj
     */
    public static void writeToServer(String serverHost,int port){
        Socket socket = null;
        try {
            //1.建立客户端socket连接，指定服务器位置及端口
            socket =new Socket("localhost",22);

            //2.得到socket读写流
            OutputStream os=socket.getOutputStream();
            PrintWriter pw=new PrintWriter(os);

            //输入流
            InputStream is=socket.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is));

            //3.利用流按照一定的操作，对socket进行读写操作
            String info="用户名：1,用户密码：1";
            pw.write(info);
            pw.flush();
            socket.shutdownOutput();
            //接收服务器的相应
            String reply=null;
            while(!((reply=br.readLine())==null)){
                System.out.println("接收服务器的信息："+reply);
            }
            //4.关闭资源
            br.close();
            is.close();
            pw.close();
            os.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if (socket!=null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
