package com.yupi.yhyojcodesandbox.model;

import lombok.Data;

/**
 * 进程执行信息
 */
@Data
public class ExecuteMessage {
    private Integer exitValue;

    private String message;
    private String errorMessage;
}
