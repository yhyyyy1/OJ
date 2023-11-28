package com.yupi.yhyojcodesandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.yupi.yhyojcodesandbox.model.ExecuteCodeRequest;
import com.yupi.yhyojcodesandbox.model.ExecuteCodeResponse;
import com.yupi.yhyojcodesandbox.model.ExecuteMessage;
import com.yupi.yhyojcodesandbox.model.JudgeInfo;
import com.yupi.yhyojcodesandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public abstract class JavaCodeSandboxTemplate implements CodeSandbox {
    //主要是流程
    private static final String GLOBAL_CODE_DIR_NAME = "tmpCode";
    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";
    //    private static final String SECURITY_MANAGER_PATH = "D:\\Project\\OJ\\yhyoj-code-sandbox\\src\\main\\resources\\security";
//    private static final String SECURITY_MANAGER_CLASS_NAME = "MySecurityManager";
    private static final long TIME_OUT = 5000L;

    /**
     * 1. 把用户的代码保存为文件
     *
     * @param code 用户代码
     * @return 代码文件
     */
    public File saveCodeToFile(String code) {
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
        return userCodeFile;
    }

    /**
     * 2. 编译代码文件，得到class文件
     *
     * @param userCodeFile 代码文件
     * @return 执行信息（编译后）
     */
    public ExecuteMessage compileFile(File userCodeFile) {
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsoluteFile());
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            if (executeMessage.getExitValue() != 0) {
                return null;
            }
            return executeMessage;
        } catch (Exception e) {
            //return getErrorResponse(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 3. 执行代码得到输出结果
     *
     * @param userCodeFile 用户代码文件
     * @param inputList    输入样例
     * @return 结果列表
     */
    public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();

        List<ExecuteMessage> executeMessageList = new ArrayList<ExecuteMessage>();
        for (String inputArgs : inputList) {
            //String runCmd = String.format("java -Xmx256m -Dfile.encoding=utf-8 -cp %s Main %s", userCodeParentPath, inputArgs);

            //带有安全管理器的Java程序
            //String runCmd = String.format("java -Xmx256m -Dfile.encoding=utf-8 -cp %s;%s -Djava.security.manager=%s Main %s", userCodeParentPath, SECURITY_MANAGER_PATH, SECURITY_MANAGER_CLASS_NAME, inputArgs);
            String runCmd = String.format("java -Xmx256m -Dfile.encoding=utf-8 -cp %s Main %s", userCodeParentPath, inputArgs);

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

                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
                System.out.println(executeMessage);
                executeMessageList.add(executeMessage);
            } catch (Exception e) {
                throw new RuntimeException("程序执行异常", e);
            }
        }
        return executeMessageList;
    }

    /**
     * 4. 收集整理输出结果
     *
     * @param executeMessageList 代码执行结果
     * @return 输出结果
     */
    public ExecuteCodeResponse getOutputResponse(List<ExecuteMessage> executeMessageList) {
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
        return executeCodeResponse;
    }

    /**
     * 5. 文件清理，释放空间（对应的tmpCode）
     *
     * @param userCodeFile 用户代码文件
     * @return true
     */
    public boolean deleteFile(File userCodeFile) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        if (userCodeFile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("删除" + (del ? "成功" : "失败"));
            return del;
        }
        return true;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse outputResponse = new ExecuteCodeResponse();
        //1. 把用户的代码保存为文件
        File userCodeFile = saveCodeToFile(code);

        //2. 编译代码文件，得到class文件
        ExecuteMessage compileFileExecuteMessage = compileFile(userCodeFile);
        System.out.println(compileFileExecuteMessage);
        //3. 执行代码得到输出结果
        if (compileFileExecuteMessage != null) {
            List<ExecuteMessage> executeMessageList = runFile(userCodeFile, inputList);
            //4. 收集整理输出结果
            outputResponse = getOutputResponse(executeMessageList);
        } else {
            System.out.println("编译失败");
            outputResponse.setStatus(3);
        }

        //5. 文件清理，释放空间（对应的tmpCode）
        boolean b = deleteFile(userCodeFile);
        if (!b) {
            log.error("deleteFile error, userCodeFile = " + userCodeFile.getAbsolutePath());
        }

        return outputResponse;
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
        executeCodeResponse.setStatus(3);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }


}
