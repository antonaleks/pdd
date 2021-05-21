package org.antonaleks.pdd.model;

import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.Ticket;

import java.util.HashMap;

public class Exam implements Quiz {

    public Ticket getTicket() {
        return ticket;
    }

    public HashMap<Integer, Integer> getCorrectAnswers() {
        return correctAnswers;
    }

    private Ticket ticket;


    private HashMap<Integer, Integer> correctAnswers;

    public Exam(Category category) {
        this.ticket = MongoHelper.getInstance().getTicketForExam(category);

    }

    @Override
    public void run() {


    }

    @Override
    public void checkAnswer(boolean condition) {
//        correctAnswers[questionId] = condition ? 1 : -1;
    }

    @Override
    public void finish() {

    }


}
