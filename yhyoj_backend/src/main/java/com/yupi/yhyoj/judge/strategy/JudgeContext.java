package com.yupi.yhyoj.judge.strategy;

import com.yupi.yhyoj.model.dto.question.JudgeCase;
import com.yupi.yhyoj.model.dto.questionsubmit.JudgeInfo;
import com.yupi.yhyoj.model.entity.Question;
import com.yupi.yhyoj.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 判题上下文对象
 */
@Data
public class JudgeContext {
    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;

}
