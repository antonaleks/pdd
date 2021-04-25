package org.antonaleks.pdd.model;

import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.Question;
import org.antonaleks.pdd.entity.Topic;

import java.util.List;

public class Training implements Quiz {

    private List<Question> questionList;

    public Training(int ticketNumber, Category cat) {
        this.questionList = MongoHelper.getInstance().getQuestionListByTicket(ticketNumber, cat);

    }

    public Training(Topic topic, Category cat) {
        this.questionList = MongoHelper.getInstance().getQuestionListByTopic(topic, cat);

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
