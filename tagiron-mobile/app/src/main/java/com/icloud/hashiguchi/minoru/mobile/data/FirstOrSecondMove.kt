package com.icloud.hashiguchi.minoru.mobile.data

import kotlin.random.Random

enum class FirstOrSecondMove(override var displayName: String) : ListViewable {
    FIRST("先攻"),
    SECOND("後攻"),
    RANDOM("おまかせ"),
    ;

    fun getValue(): Boolean {
        return when (this) {
            FIRST -> true
            SECOND -> false
            RANDOM -> Random.nextBoolean()
        }
    }
}