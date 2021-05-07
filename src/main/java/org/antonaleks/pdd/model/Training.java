package org.antonaleks.pdd.model;

import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.Ticket;
import org.antonaleks.pdd.entity.Topic;

import java.util.HashMap;

public class Training implements Quiz {

    public Ticket getTicket() {
        return ticket;
    }

    public HashMap<Integer, Integer> getCorrectAnswers() {
        return correctAnswers;
    }

    private Ticket ticket;


    private HashMap<Integer, Integer> correctAnswers;

    public Training(Ticket ticket) {
        this.ticket = ticket;

    }

    public Training(int ticketNumber, Category cat) {
        this(MongoHelper.getInstance().getTicketByNumber(ticketNumber, cat));
    }

    public Training(Topic topic, Category cat) {
        this(MongoHelper.getInstance().getTicketByTopic(topic, cat));


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
