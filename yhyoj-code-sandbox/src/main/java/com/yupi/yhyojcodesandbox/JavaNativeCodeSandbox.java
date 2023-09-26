package com.yupi.yhyojcodesandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.yupi.yhyojcodesandbox.model.ExecuteCodeRequest;
import com.yupi.yhyojcodesandbox.model.ExecuteCodeResponse;
import com.yupi.yhyojcodesandbox.model.ExecuteMessage;
import com.yupi.yhyojcodesandbox.model.JudgeInfo;
import com.yupi.yhyojcodesandbox.security.DefaultSecurityManager;
import com.yupi.yhyojcodesandbox.utils.ProcessUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class JavaNativeCodeSandbox implements CodeSandbox {

    //魔法值？
    private static final String GLOBAL_CODE_DIR_NAME = "tmpCode";
    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";
    private static final String SECURITY_MANAGER_PATH = "D:\\Project\\OJ\\yhyoj-code-sandbox\\src\\main\\resources\\security";
    private static final String SECURITY_MANAGER_CLASS_NAME = "MySecurityManager";
    private static final long TIME_OUT = 5000L;
    private static final List<String> blacklist = Arrays.asList("Files", "exec");

    private static final WordTree WORD_TREE;

    static {
        //初始化字典树
        WORD_TREE = new WordTree();
        WORD_TREE.addWords(blacklist);
    }

    public static void main(String[] args) {
        JavaNativeCodeSandbox javaNativeCodeSandbox = new JavaNativeCodeSandbox();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();

        //String code = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);
        String code = ResourceUtil.readStr("testCode/unsafeCode/RunFileError.java", StandardCharsets.UTF_8);
        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");
        executeCodeRequest.setInputList(Arrays.asList("1 2", "1 3"));
        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandbox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();
        List<String> inputList = executeCodeRequest.getInputList();

//        //wordTree 找到了匹配的关键词
//        FoundWord foundWord = WORD_TREE.matchWord(code);
//        if (foundWord != null) {
//            System.out.println("包含禁止词" + foundWord.getFoundWord());
//            return null;
//        }

        //1. 把用户的代码保存为文件
        String userDir = System.getProperty("user.dir");
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        //判断全局代码目录是否存在，没有就新建
        if (!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }

        //因为提交的代码都是Main，所以不可能都放在同一个目录下——分级
        //将用户的代码分级存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID(); //父目录
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME; //实际文件路径
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);

        //2. 编译代码文件，得到class文件
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsoluteFile());
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            System.out.println(executeMessage);
        } catch (Exception e) {
            return getErrorResponse(e);
        }

        //3. 执行代码得到输出结果
        List<ExecuteMessage> executeMessageList = new ArrayList<ExecuteMessage>();
        for (String inputArgs : inputList) {
            //String runCmd = String.format("java -Xmx256m -Dfile.encoding=utf-8 -cp %s Main %s", userCodeParentPath, inputArgs);

            //带有安全管理器的Java程序
            String runCmd = String.format("java -Xmx256m -Dfile.encoding=utf-8 -cp %s;%s -Djava.security.manager=%s Main %s", userCodeParentPath, SECURITY_MANAGER_PATH, SECURITY_MANAGER_CLASS_NAME, inputArgs);

            try {
                Process runProcess = Runtime.getRuntime().exec(runCmd);
                // 超时控制：为避免超时，新建的守护线程
                new Thread(() -> {
                    try {
                        Thread.sleep(TIME_OUT);
                        System.out.println("超时了，中断");
                        runProcess.destroy();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();

                //ExecuteMessage executeMessage = ProcessUtils.runInterProcessAndGetMessage(runProcess, "运行", inputArgs);
                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
                System.out.println(executeMessage);
                executeMessageList.add(executeMessage);
            } catch (Exception e) {
                return getErrorResponse(e);
            }
        }

        //4. 收集整理输出结果
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<String>();

        //取最大值，用于判断是否超时
        long maxtime = 0;
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotEmpty(errorMessage)) {
                //有错误信息
                executeCodeResponse.setMessage(errorMessage);
                //3 表示用户提供的代码存在错误
                executeCodeResponse.setStatus(3);
                break;
            }
            outputList.add(executeMessage.getMessage());
            Long time = executeMessage.getTime();
            if (time != null) {
                maxtime = Math.max(maxtime, time);
            }
        }
        executeCodeResponse.setOutputsList(outputList);

        if (outputList.size() == executeMessageList.size()) {
            //正常完成
            executeCodeResponse.setStatus(1);
        }
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxtime);
        //获取内存占用很麻烦，此处不做实践
        //judgeInfo.setMemory();
        executeCodeResponse.setJudgeInfo(judgeInfo);

        //5. 文件清理，释放空间（对应的tmpCode）
        if (userCodeFile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("删除" + (del ? "成功" : "失败"));
        }

        return executeCodeResponse;
    }

//6. 错误处理，提升程序健壮性

    /**
     * 获取错误响应
     *
     * @param e
     * @return
     */
    private ExecuteCodeResponse getErrorResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputsList(new ArrayList<String>());
        executeCodeResponse.setMessage(e.getMessage());
        //2表示代码沙箱错误
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }
}
