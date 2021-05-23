package org.antonaleks.pdd.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.antonaleks.pdd.entity.Session;

import java.io.IOException;

class MongoTimestampConverter extends JsonDeserializer<Long> {

    @Override
    public Long deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.readValueAsTree();
        return Long.valueOf(node.get("$numberLong").asText());
    }
}

public class Statistic {
    @JsonProperty("dateExam")
    @JsonDeserialize(using = MongoTimestampConverter.class)
    private long dateExam;
    @JsonProperty("mistakesCount")
    private int mistakesCount;
    @JsonProperty("rightCount")
    private int rightCount;

    public Statistic() {
    }

    public long getDateExam() {
        return dateExam;
    }

    public int getMistakesCount() {
        return mistakesCount;
    }

    public int getRightCount() {
        return rightCount;
    }

    public boolean isExamIsPassed() {
        return examIsPassed;
    }

    @JsonProperty("examIsPassed")
    private boolean examIsPassed;

    public int getCategory() {
        return category;
    }

    @JsonProperty("category")
    private int category;

    public Statistic(long dateExam, int mistakesCount, int rightCount, boolean examIsPassed, Category category) {
        this.dateExam = dateExam;
        this.mistakesCount = mistakesCount;
        this.rightCount = rightCount;
        this.examIsPassed = examIsPassed;
        this.category = category.getCategory();
    }

    public Statistic(long dateExam, int mistakesCount, int rightCount, boolean examIsPassed) {
        this.dateExam = dateExam;
        this.mistakesCount = mistakesCount;
        this.rightCount = rightCount;
        this.examIsPassed = examIsPassed;
        if (Session.getInstance().getCurrentCategory() != null)
            this.category = Session.getInstance().getCurrentCategory().getCategory();
    }

}

