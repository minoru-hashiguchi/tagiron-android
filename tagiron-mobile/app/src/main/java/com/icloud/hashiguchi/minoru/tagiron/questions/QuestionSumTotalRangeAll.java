package com.icloud.hashiguchi.minoru.tagiron.questions;

import com.icloud.hashiguchi.minoru.tagiron.constants.TotalValueType;

public class QuestionSumTotalRangeAll extends QuestionSumTotalRange implements ShareableQuestion {

    public QuestionSumTotalRangeAll(TotalValueType type) {
        super(type);
        this.text = this.getPrefixText(this.text);
    }

    @Override
    public String getSummaryText() {
        return "[共]" + super.getSummaryText();
    }
}
