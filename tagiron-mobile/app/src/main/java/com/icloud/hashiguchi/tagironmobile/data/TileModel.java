package com.icloud.hashiguchi.tagironmobile.data;


import com.icloud.hashiguchi.tagironmobile.constants.Mark;
import com.icloud.hashiguchi.tagironmobile.constants.TileColor;

public class TileModel implements Cloneable {

    protected Integer no = null;
    protected TileColor tileColor = null;

    private Mark mark = Mark.UNKNOWN;
    private String reazon = null;

    public TileModel() {

    }

    public TileModel(int no, TileColor tileColor) {
        this.no = no;
        this.tileColor = tileColor;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer number) {
        this.no = number;
    }

    public TileColor getColor() {
        return tileColor;
    }

    public void setColor(TileColor tileColor) {
        this.tileColor = tileColor;
    }

//    public String getPrintString() {
//        return String.format(
//                "[%s]%s",
//                StringUtils.defaultIfBlank(color.getKanji(), "未確定"),
//                StringUtils.defaultIfBlank(StringUtils.EMPTY + getNo(), "未確定"));
//    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof TileModel) {
            TileModel t = (TileModel) object;
            return this.getNo().intValue() == t.getNo().intValue() && this.tileColor == t.getColor();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (this.tileColor.toString() + this.no).hashCode();
    }

    @Override
    public String toString() {
        String strColor = tileColor.toString();
        String num = no == null ? "" : no.toString();
        return strColor + num;
    }

    @Override
    public TileModel clone() {
        try {
            return (TileModel) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void setIgnore(String reazon) {
        if (Mark.IGNORE != this.mark) {
//            logger.debug("{} : [{}] -> [{}] [{}]", this.toString(), this.mark, Mark.IGNORE, reazon);
            this.mark = Mark.IGNORE;
            this.reazon = reazon;
        }
    }

    public Mark getMark() {
        return mark;
    }

    public void setMark(Mark mark) {
        //		if (mark == Mark.IGNORE) {
        //			throw new RuntimeException("setIgnoreを利用すること");
        //		}
        if (mark != this.mark) {
//            logger.debug("{} : [{}] -> [{}]", this.toString(), this.mark, mark);
            this.mark = mark;
        }
    }

    public String getReazon() {
        return reazon;
    }
}
