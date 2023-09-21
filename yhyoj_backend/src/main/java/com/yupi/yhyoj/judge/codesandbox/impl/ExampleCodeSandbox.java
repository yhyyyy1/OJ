package com.yupi.yhyoj.judge.codesandbox.impl;

import com.yupi.yhyoj.judge.codesandbox.CodeSandbox;
import com.yupi.yhyoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.yhyoj.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 实例代码沙箱（仅为了跑通业务流程）
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("实例代码沙箱");
        return null;
    }
}
