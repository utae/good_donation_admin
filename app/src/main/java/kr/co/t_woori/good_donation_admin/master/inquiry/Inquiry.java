package kr.co.t_woori.good_donation_admin.master.inquiry;

import java.io.Serializable;

/**
 * Created by rladn on 2017-09-29.
 */

public class Inquiry implements Serializable {

    private String id;
    private String title;
    private String question;
    private String answer;

    public Inquiry(String id, String title, String question) {
        this(id, title, question, null);
    }

    public Inquiry(String id, String title, String question, String answer) {
        this.id = id;
        this.title = title;
        this.question = question;
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
