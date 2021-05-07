package org.antonaleks.pdd.model;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface Quiz {
    void run() throws JsonProcessingException;
    void checkAnswer(boolean condition);
    void finish();

}
