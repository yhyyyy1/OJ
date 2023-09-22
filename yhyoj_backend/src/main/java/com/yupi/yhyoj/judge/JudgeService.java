package com.yupi.yhyoj.judge;

import com.yupi.yhyoj.model.entity.QuestionSubmit;

/**
 * 判题服务
 */
public interface JudgeService {
    /**
     * 判题
     *
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
