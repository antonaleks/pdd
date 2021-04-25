package org.antonaleks.pdd.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.Question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Training implements Quiz {

    private List<Question> questionList;

    public Training(int ticketNumber, Category cat) {
        try {
            this.questionList = MongoHelper.getInstance().getQuestionListByTicket(ticketNumber, cat);
        } catch (JsonProcessingException e) {
            this.questionList = new ArrayList<>();
        }

    }

    @Override
    public void run() {


    }

    @Override
    public void checkAnswer() {

    }

    @Override
    public void finish() {

    }


}
