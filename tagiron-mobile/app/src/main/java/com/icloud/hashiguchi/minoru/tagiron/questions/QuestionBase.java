package com.icloud.hashiguchi.minoru.tagiron.questions;

import com.icloud.hashiguchi.minoru.tagiron.TileViewModel;
import com.icloud.hashiguchi.minoru.tagiron.constants.Constant;

import java.util.List;
import java.util.stream.Collectors;

public abstract class QuestionBase implements Cloneable {
    protected static final boolean D = Constant.D;
    //    private static final Logger logger = LogManager.getLogger("");
    protected String text;

    protected List<Integer> answers;

    protected Integer selectedNo = null;

    public Integer getSelectedNo() {
        return selectedNo;
    }

    public void setSelectedNo(Integer specifiedNo) {
        this.selectedNo = specifiedNo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Integer> answers) {
        this.answers = answers;
    }

    public abstract String getSummaryText();

    public String getAnswerText() {
        if (answers == null) {
            return "";
        }
        String answerStr = answers.size() < 1
                ? "なし"
                : answers.stream().map(v -> {
            v++;
            return v + getAnswerUnit();
        }).collect(Collectors.joining(", "));
        return answerStr;
    }

    public abstract List<Integer> answer(List<TileViewModel> opponentTiles);

    public abstract String getAnswerUnit();

    public String getStringPirntQuestion() {
        return " " + text;
    }

    public void printAll() {
        String selectStr = this instanceof QuestionWhereNoBySelect
                ? String.format("[%sを選択]", selectedNo)
                : "";

        String answerStr = getAnswerText();
//        logger.debug("!! {} {} -> [{}]",
//                text,
//                selectStr,
//                answerStr);
    }

    @Override
    public QuestionBase clone() {
        try {
            QuestionBase obj = (QuestionBase) super.clone();
            // 回答をクリアして返却
            obj.answers = null;
            return obj;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
