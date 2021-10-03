package edu.fzu.zookeeper.curb;


import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.List;

public class ZKCurd {
    //定义会话的超时时间
    private final static int SESSION_TIME=30000;
    //定义zk集群的ip地址
    private final static String ZK_SERVERS = "192.168.241.172:2181,192.168.241.173:2181,192.168.241.174:2181";
    //日志对象
    private final static Logger LOGGER = Logger.getLogger(ZKCurd.class);
    private ZooKeeper zkCli = null;
    private Watcher watcher = new Watcher() {
        @Override
        public void process(WatchedEvent watchedEvent) {
            LOGGER.info("event:"+watchedEvent.toString());
        }
    };
    @Before
    public void connect() throws IOException {
        zkCli = new ZooKeeper(ZK_SERVERS,SESSION_TIME,watcher);
        //获取当前会话的sessionId
        long sessionId = zkCli.getSessionId();
        LOGGER.info("sessionId:"+sessionId);
    }
    @After
    public void close() throws InterruptedException {
        zkCli.close();
    }
    /**创建节点
     *final String path:要创建节点的全路径
     * byte data[]:节点中数据内容
     * List<ACL> acl：节点权限
     * CreateMode createMode:节点类型
     * PERSISTENT:普通持久几点
     * PERSISTENT_SEQUENTIAL：顺序持久节点
     * EPHEMERAL：普通临时节点
     * EPHEMERAL_SEQUENTIAL：顺序临时节点
     */
    @Test
    public void create(){
        String result = "";
        try {
            /*result = zkCli.create("/itbaizhan", "good".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);*/
            result = zkCli.create("/zk002", "good".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            //抛出AssertionError
            Assert.fail();
        }
        LOGGER.info("create result:{"+result+"}");
    }
    @Test
    public void exists(){
        try {
            Stat stat = zkCli.exists("/bjsxt", false);
            if(stat==null){
                System.out.println("/bjsxt not exists");
            }else{
                System.out.println("/bjsxt exists");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
    @Test
    public void getData(){
        String result = null;
        try {
            byte[] data = zkCli.getData("/itbaizhan", null, null);
            result = new String(data);
        } catch (KeeperException e) {
            LOGGER.error(e.getMessage());
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
        LOGGER.info("getData result------{"+result+"}");
    }
    @Test
    public void getDataStat(){
        String result = null;
        Stat stat = new Stat();
        try {
            byte[] data = zkCli.getData("/itbaizhan", null, stat);
            result = new String(data);
        } catch (KeeperException e) {
            LOGGER.error(e.getMessage());
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
        LOGGER.info("getData result------{"+result+"}");
        LOGGER.info("getData stat------{"+stat+"}");
    }
    //获取子节点的名称
    @Test
    public void getChildren(){
        try {
            List<String> childrenList = zkCli.getChildren("/", true);
            //遍历子节点
            for(String child:childrenList){
                LOGGER.info("child:{"+child+"}");
            }
        } catch (KeeperException e) {
            LOGGER.error(e.getMessage());
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }
    //获取子节点的数据
    @Test
    public void getChildrenData(){
        try {
            List<String> childrenList = zkCli.getChildren("/", true);
            String data = null;
            //遍历子节点
            for(String child:childrenList){
                data = new String(zkCli.getData("/" + child, null, null));
                LOGGER.info("child:{"+child+"},value:{"+data+"}");
            }
        } catch (KeeperException e) {
            LOGGER.error(e.getMessage());
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }
    @Test
    public void setData(){
        Stat stat = null;
        try {
            //path：被修改节点的全路径，第二个参数：修改后的value，version：-1表示不管version的值为几都可以修改
            stat = zkCli.setData("/zk001","setData01".getBytes(),-1);
        } catch (KeeperException e) {
            LOGGER.error(e.getMessage());
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
        //dataVersion:getVersion获取是getVersion，代表节点值的版本，每被修改一次该值加1
        LOGGER.info("version:{"+stat.getVersion()+"}");
    }
    @Test
    public void delete(){
        try {
            zkCli.delete("/wzyy/zhanshi0000000003",-1);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        } catch (KeeperException e) {
            LOGGER.error(e.getMessage());
        }
    }
    @Test
    public void getDataWatcher(){
        String result = null;
        System.out.println("get:");
        try {
            byte[] data = zkCli.getData("/zk001", new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    LOGGER.info("getDataWatcher event type:{" + event.getType() + "}");
                    System.out.println("watcher ok!!");
                }
            }, null);
            result = new String(data);
        } catch (KeeperException e) {
            LOGGER.error(e.getMessage());
            Assert.fail();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
            Assert.fail();
        }
        LOGGER.info("getData result:{"+result+"}");
        //修改节点值
        try {
            System.out.println("-------set1------");
            zkCli.setData("/zk001","update1".getBytes(),-1);
            System.out.println("-------set2------");
            zkCli.setData("/zk001","update2".getBytes(),-1);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            Assert.fail();
        }
        System.out.println("test over");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
