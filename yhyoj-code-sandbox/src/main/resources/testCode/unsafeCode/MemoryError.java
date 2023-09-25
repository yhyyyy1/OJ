
import java.util.ArrayList;
import java.util.List;

/**
 * 无限占用空间（浪费系统资源）
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<byte[]> bytes = new ArrayList<>();
        while (true) {
            bytes.add(new byte[10000]);
        }
    }
}
