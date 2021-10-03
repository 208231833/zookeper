package edu.fzu.zookeeper.rmi.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RmiServer {
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        System.out.println("rmi server running...");
        //定义发布RMI服务时使用的端口
        int port = 1099;
        //定义url
        String url = "rmi://localhost:1099/com.edu.fzu.zookeeper.rmi.server.HelloServiceImpl";
        //注册服务:在JNDI中创建了一个注册表
        LocateRegistry.createRegistry(port);
        //绑定服务：将RMI服务的实现类对象与url绑定
        Naming.rebind(url,new HelloServiceImpl());
    }
}
