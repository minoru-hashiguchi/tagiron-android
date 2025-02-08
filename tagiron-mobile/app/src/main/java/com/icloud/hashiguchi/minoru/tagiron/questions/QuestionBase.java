package com.icloud.hashiguchi.minoru.tagiron.questions;

import com.icloud.hashiguchi.minoru.tagiron.Tile;
import com.icloud.hashiguchi.minoru.tagiron.constants.Constant;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
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

    public abstract List<Integer> answer(List<Tile> opponentTiles);

    public abstract String getAnswerUnit();

    public boolean deletePatterns(List<Tile[]> patterns) {
//        logger.debug("current patterns count -> {} ", patterns.size());
        Iterator<Tile[]> itr = patterns.iterator();
        while (itr.hasNext()) {
            List<Tile> tiles = Arrays.asList(itr.next());
            List<Integer> verify = answer(tiles);
            boolean match = !Objects.deepEquals(answers, verify);
            if (match) {
//                if (D) {
//                    logger.debug("answer:{}, verify:{}, delete pattern -> {}", answers.toString(), verify.toString(),
//                            tiles.toString());
//                }
                itr.remove();
            }
        }
//        logger.debug("current patterns count -> {} ", patterns.size());

        return patterns.size() == 1;
    }

    public String getStringPirntQuestion() {
        return " " + text;
    }

    public void printAll() {
        String selectStr = this instanceof QuestionWhereNoBySelect
                ? String.format("[%sを選択]", selectedNo)
                : "";

        String answerStr = answers.size() < 1
                ? "なし"
                : answers.stream().map(v -> {
            v++;
            return v + getAnswerUnit();
        }).collect(Collectors.joining(", "));

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
