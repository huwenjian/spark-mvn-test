package com.kcm;


public class TermInput {

    public String word;

    /**
     * 词性
     */
    public String nature;

    /**
     * 在文本中的起始位置（需开启分词器的offset选项）
     */
    public int offset;

    public TermInput() {
    }

    public TermInput(String word, String nature, int offset) {
        this.word = word;
        this.nature = nature;
        this.offset = offset;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
