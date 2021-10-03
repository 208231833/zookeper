package edu.fzu.zookeeper.rmi.client;
import edu.fzu.zookeeper.rmi.common.HelloService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
public class RmiClient {
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
            System.out.println("rmi client running");
            //定义url
            String url = "rmi://localhost:1099/com.edu.fzu.zookeeper.rmi.server.HelloServiceImpl";
            //找寻发布的服务，并返回对象
            Remote lookup = Naming.lookup(url);
            //强制类型转换
            HelloService helloService = (HelloService)lookup;
            //调用方法
            String result = helloService.sayHello("gtjin");
            System.out.println("client result:"+result);
    }
}
