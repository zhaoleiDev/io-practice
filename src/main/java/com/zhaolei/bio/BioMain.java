package com.zhaolei.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO  服务端程序
 * 使用线程池接收客服端请求
 * 测试使用wind 命令行来作为客服端
 * telnet命令  ctrl+] 进入命令  send+发送内容
 * bio问题:
 * 每一个客户端连接需要创建一个线程，并且当客户端无数据输入时read方法阻塞 导致该线程被占用
 * @author ZHAOLEI
 * @date 2021-10-30 16:02
 */
public class BioMain {
    public static void main(String[] args) throws IOException {
        //创建线程池
        ExecutorService executorService =  Executors.newFixedThreadPool(20);
        //创建socket
        final ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器开始启动 监听端口为6666");

        while(true){
            //accept为阻塞方法
            final Socket socket = serverSocket.accept();
            System.out.println("获取客户端连接");
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    handler(socket);
                }
            });
        }
    }

    public static void handler(Socket socket){
        try{
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            //read方法为阻塞方法  当客户端无数据输入时 就会阻塞该线程
            while(inputStream.read(bytes) != -1){
                System.out.println("收到消息:"+new String(bytes));
                bytes = new byte[1024];
            }
        }catch(Exception e){
            System.out.println(e.fillInStackTrace());
        }finally{
            try {
                System.out.println("关闭客户端了连接");
                socket.close();
            } catch (IOException e) {
                System.out.println("关闭客户端连接失败");
                e.printStackTrace();
            }
        }
    }
}
