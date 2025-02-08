package com.icloud.hashiguchi.minoru.tagiron.questions;

import com.icloud.hashiguchi.minoru.tagiron.constants.Constant;

public interface ShareableQuestion {
	public default String getPrefixText(String text) {
		return Constant.TEXT_SHARED_Q + text;
	}
}
