package com.icloud.hashiguchi.minoru.tagiron.questions;

import com.icloud.hashiguchi.minoru.tagiron.constants.Constant;

public interface ShareableQuestion {
    default String getPrefixText(String text) {
        return Constant.SHAREABLE_QUESTION_TEXT + "\n" + text;
    }

    default String getShareableMark() {
        return isShared() ? Constant.SHARED_QUESTION_TEXT_SUMMARY : Constant.SHAREABLE_QUESTION_TEXT_SUMMARY;
    }

    boolean isShared();
}
