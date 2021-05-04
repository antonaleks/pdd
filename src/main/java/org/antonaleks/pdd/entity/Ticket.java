package org.antonaleks.pdd.entity;

import java.util.List;

public class Ticket {
    private List<Question> questions;

    public List<Question> getQuestions() {
        return questions;
    }

    public int getId() {
        return id;
    }

    private int id;

    public Ticket(List<Question> questions, int id) {
        this.questions = questions;
        this.id = id;
    }
}
