package com.yupi.yhyojcodesandbox;

import com.yupi.yhyojcodesandbox.model.ExecuteCodeRequest;
import com.yupi.yhyojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Java原生实现，直接复用模板方法（模板就是根据原生来的 hhhh）
 */
@Component
public class JavaNativeCodeSandbox extends JavaCodeSandboxTemplate {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }
}
