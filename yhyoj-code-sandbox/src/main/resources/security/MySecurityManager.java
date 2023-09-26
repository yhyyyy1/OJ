
import java.security.Permission;

/**
 * 默认安全管理器
 */
public class MySecurityManager extends SecurityManager {

    //检查所有权限
    @Override
    public void checkPermission(Permission perm) {
        //super.checkPermission(perm);
        //throw new SecurityException("checkPermission 权限异常：" + perm);
    }

    //检测程序是否可以执行文件
    @Override
    public void checkExec(String cmd) {
        throw new SecurityException("checkExec 权限异常：" + cmd);
    }

    //检测程序是否可以读文件
    @Override
    public void checkRead(String file) {
//        System.out.println(file);
//        if (file.contains("D:\\Project\\OJ\\yhyoj-code-sandbox")) {
//            return;
//        }
//        throw new SecurityException("checkRead 权限异常：" + file);
    }

    //检测程序是否可以写文件
    @Override
    public void checkWrite(String file) {
        //throw new SecurityException("checkWrite 权限异常：" + file);
    }

    //检测程序是否可以删除文件
    @Override
    public void checkDelete(String file) {
       //throw new SecurityException("checkDelete 权限异常：" + file);
    }

    //检测程序是否可以连接网络
    @Override
    public void checkConnect(String host, int port) {
        //throw new SecurityException("checkConnect 权限异常：" + host + ": " + port);
    }
}
