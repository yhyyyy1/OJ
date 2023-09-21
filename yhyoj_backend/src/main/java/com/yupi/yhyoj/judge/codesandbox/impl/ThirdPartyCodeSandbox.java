package com.yupi.yhyoj.judge.codesandbox.impl;

import com.yupi.yhyoj.judge.codesandbox.CodeSandbox;
import com.yupi.yhyoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.yhyoj.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 第三方代码沙箱（调用非自己开发的代码沙箱）
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        System.out.println("第三方代码沙箱");
        return null;
    }
}
