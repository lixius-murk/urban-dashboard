package model.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recommendation_msg")
public class RecommendationMsg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String msg;


    public RecommendationMsg() {

    }

    public RecommendationMsg(Long id, String msg) {
        this.id = id;
        this.msg = msg;
    }
    public RecommendationMsg(String msg) {
        this.msg = msg;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
