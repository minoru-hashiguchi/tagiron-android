package com.icloud.hashiguchi.minoru.tagiron.questions;

import java.util.stream.Collectors;

public abstract class QuestionWhereBase extends QuestionBase {

    @Override
    public String getAnswerText() {
        String answerStr = answers.size() < 1
                ? "なし"
                : answers.stream().map(v -> ++v + "").collect(Collectors.joining(",")) + getAnswerUnit();
        return answerStr;
    }

    @Override
    public String getAnswerUnit() {
        return "番目";
    }
}
