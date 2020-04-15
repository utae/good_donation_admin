package kr.co.t_woori.good_donation_admin.master.charity;

import java.io.Serializable;

/**
 * Created by rladn on 2017-08-14.
 */

public class Charity implements Serializable{

    private String idNum;
    private String name;
    private String introduction;
    private String appreciationPhrase;
    private String registration;
    private String follow;
    private String goal;
    private String accumulation;
    private String today;
    private String imgAmount;
    private boolean recommend;

    public Charity(String idNum, String name) {
        this.idNum = idNum;
        this.name = name;
    }

    public Charity(String idNum, String name, String introduction, String appreciationPhrase, String registration, String follow, String today, String accumulation, boolean recommend, String imgAmount) {
        this(idNum, name, introduction, appreciationPhrase, registration, follow, today, accumulation, recommend, imgAmount, null);
    }

    public Charity(String idNum, String name, String introduction, String appreciationPhrase, String registration, String follow, String today, String accumulation, boolean recommend, String imgAmount, String goal) {
        this.idNum = idNum;
        this.name = name;
        this.introduction = introduction;
        this.appreciationPhrase = appreciationPhrase;
        this.registration = registration;
        this.follow = follow;
        this.today = today;
        this.accumulation = accumulation;
        this.recommend = recommend;
        this.goal = goal;
        this.imgAmount = imgAmount;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public String getIdNum() {
        return idNum;
    }

    public String getName() {
        return name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getAppreciationPhrase() {
        return appreciationPhrase;
    }

    public String getRegistration() {
        return registration;
    }

    public String getFollow() {
        return follow;
    }

    public String getGoal() {
        return goal;
    }

    public String getAccumulation() {
        return accumulation;
    }

    public String getToday() {
        return today;
    }

    public String getImgAmount() {
        return imgAmount;
    }
}
