package com.icloud.hashiguchi.minoru.tagiron.questions;

import java.util.Arrays;

public class QuestionWhereNoBySelect extends QuestionWhereNo {

    public QuestionWhereNoBySelect(int... selectNumbers) {
        super(selectNumbers);
        if (selectNumbers.length < 2) {
            throw new RuntimeException("パラメータ設定エラー：" + Arrays.toString(selectNumbers));
        } else {
            this.text = String.format("%sまたは%sはどこ？\n（どちらかひとつ選ぶ）",
                    selectNumbers[0], selectNumbers[1]);
        }
    }
}
