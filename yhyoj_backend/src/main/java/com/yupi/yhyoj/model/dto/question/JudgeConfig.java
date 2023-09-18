package com.yupi.yhyoj.model.dto.question;

import lombok.Data;

@Data
public class JudgeConfig {
//    {"timeLimit": 1000,
//            "memoryLimit": 1000,
//            "stackLimit": 1000
//    }
    /**
     * 时间限制,ms
     */
    private long timeLimit;

    /**
     * 内存限制,kb
     */
    private long memoryLimit;

    /**
     * 堆栈限制,kb
     */
    private long stackLimit;
}
