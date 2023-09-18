package com.yupi.yhyoj.model.dto.questionsubmit;

import lombok.Data;

@Data
public class JudgeInfo {
//    {
//        "message": "程序执行信息",
//            "time": 1000, // 程序执行时间，单位为 ms
//            "memory": 1000, // 程序执行内存，单位为 kb
//    }
    /**
     * 程序执行信息
     */
    private String message;
    /**
     * 程序执行时间,ms
     */
    private Long time;
    /**
     * 程序执行内存,kb
     */
    private Long memory;
}
