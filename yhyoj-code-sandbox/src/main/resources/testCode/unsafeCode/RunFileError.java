import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * 向服务器写文件（植入危险程序）
 */
public class Main {
    public static void main(String[] args) throws Exception {
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + File.separator + "src/main/resources/木马程序.bat";
        Process process = Runtime.getRuntime().exec(filePath);
        process.waitFor();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder compileOutputStringBuilder = new StringBuilder();
        String compileOutputLine;
        while ((compileOutputLine = bufferedReader.readLine()) != null) {
            System.out.println(compileOutputLine);
        }

        System.out.println("执行异常程序成功，✌");
    }
}
