package com.uit.uit2013.model;

/**
 * Created by yszsyf on 16/2/25.
 */
public class Score {

    private  String name;
    private String pscore;
    private String qscore;
    private String kscore;

    public String getXuefen() {
        return xuefen;
    }

    public void setXuefen(String xuefen) {
        this.xuefen = xuefen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPscore() {
        return pscore;
    }

    public void setPscore(String pscore) {
        this.pscore = pscore;
    }

    public String getQscore() {
        return qscore;
    }

    public void setQscore(String qscore) {
        this.qscore = qscore;
    }

    public String getKscore() {
        return kscore;
    }

    public void setKscore(String kscore) {
        this.kscore = kscore;
    }

    private String xuefen;

}
