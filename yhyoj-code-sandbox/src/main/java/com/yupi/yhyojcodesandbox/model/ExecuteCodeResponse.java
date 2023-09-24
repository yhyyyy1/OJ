package com.yupi.yhyojcodesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {
    private List<String> outputsList;
    /**
     * 接口执行信息
     */
    private String message;
    /**
     * 执行状态
     */
    private Integer status;
    /**
     * code程序执行信息
     * 包括memory time message
     */
    private JudgeInfo judgeInfo;
}
