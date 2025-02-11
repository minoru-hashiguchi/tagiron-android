package com.icloud.hashiguchi.minoru.tagiron.constants;

import com.icloud.hashiguchi.minoru.tagiron.TileViewModel;
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionBase;
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionHowColor;
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionHowEven;
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionHowOdd;
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionHowPairNo;
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionMinusMinFromMax;
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionSumTotalColor;
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionSumTotalRange;
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionSumTotalRangeAll;
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionWhereNo;
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionWhereNoBySelect;
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionWhereNoSameColor;
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionWhereNoSequential;
import com.icloud.hashiguchi.minoru.tagiron.questions.QuestionWhichCenterNo;

public class Constant {
    public static final boolean D = false;
    public static final String LOG_TAG = "TAGIRON_LOG";
    public static final String TEXT_SHARED_Q = "[共有情報カード]";
    public static final TileViewModel[] TILES = new TileViewModel[]{
            new TileViewModel(0, Color.RED),
            new TileViewModel(1, Color.RED),
            new TileViewModel(2, Color.RED),
            new TileViewModel(3, Color.RED),
            new TileViewModel(4, Color.RED),
            new TileViewModel(5, Color.YELLOW),
            new TileViewModel(6, Color.RED),
            new TileViewModel(7, Color.RED),
            new TileViewModel(8, Color.RED),
            new TileViewModel(9, Color.RED),
            new TileViewModel(0, Color.BLUE),
            new TileViewModel(1, Color.BLUE),
            new TileViewModel(2, Color.BLUE),
            new TileViewModel(3, Color.BLUE),
            new TileViewModel(4, Color.BLUE),
            new TileViewModel(5, Color.YELLOW),
            new TileViewModel(6, Color.BLUE),
            new TileViewModel(7, Color.BLUE),
            new TileViewModel(8, Color.BLUE),
            new TileViewModel(9, Color.BLUE),
    };
    public static final QuestionBase[] QUESTIONS = new QuestionBase[]{

            // 数字の場所を問う系
            //	０はどこ？
            //	１または２はどこ？（どちらかひとつ選ぶ）
            //	３または４はどこ？（どちらかひとつ選ぶ）
            //	５はどこ？
            //	６または７はどこ？（どちらかひとつ選ぶ）
            //	８または９はどこ？（どちらかひとつ選ぶ）
            new QuestionWhereNo(0),
            new QuestionWhereNoBySelect(1, 2),
            new QuestionWhereNoBySelect(3, 4),
            new QuestionWhereNo(5),
            new QuestionWhereNoBySelect(6, 7),
            new QuestionWhereNoBySelect(8, 9),

            // 同じ色が隣り合う場所を問う
            //	同じ色がとなり合っている数字タイルはどこ？
            new QuestionWhereNoSameColor(),

            // 連続する数字の場所を問う
            //	数が連続している数字タイルはどこ？
            new QuestionWhereNoSequential(),

            // 特定条件の枚数を問う系
            //	奇数は何枚ある？
            //	偶数は何枚ある？（０も含む）
            //	奇数は何枚ある？
            //	偶数は何枚ある？（０も含む）
            new QuestionHowColor(Color.BLUE),
            new QuestionHowColor(Color.RED),
            new QuestionHowOdd(),
            new QuestionHowEven(),

            // ペア組数を問う
            //	同じ数字タイルのペアは何組ある？
            new QuestionHowPairNo(),

            // 合計値を問う系
            //	大きいほうから３枚の数の合計は？
            //	小さいほうから３枚の合計は？
            //	中央の３枚の数の合計は？
            //	［共有情報カード］数字タイルすべての数の合計は？
            //	青の数の合計は？
            //	赤の数の合計は？
            new QuestionSumTotalRange(TotalValueType.LEFT),
            new QuestionSumTotalRange(TotalValueType.CENTER),
            new QuestionSumTotalRange(TotalValueType.RIGHT),
            new QuestionSumTotalRangeAll(TotalValueType.ALL),
            new QuestionSumTotalColor(Color.BLUE),
            new QuestionSumTotalColor(Color.RED),

            // 中央の数字の大なり小なりを問う
            //	［共有情報カード］中央の数字タイルは５以上？４以下？
            new QuestionWhichCenterNo(),

            // 最大から最小を引いた数を問う
            //	［共有情報カード］数字タイルの最大の数から、最小の数を引いた数は？
            new QuestionMinusMinFromMax(),

    };
    public static final int OPEN_QUESTIONS_COUNT = QUESTIONS.length;
}
