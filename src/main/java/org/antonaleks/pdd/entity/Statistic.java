package org.antonaleks.pdd.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleObjectProperty;
import org.antonaleks.pdd.model.Category;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

class MongoTimestampConverter extends JsonDeserializer<Long> {

    @Override
    public Long deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.readValueAsTree();
        return Long.valueOf(node.get("$numberLong").asText());
    }
}

@JsonIgnoreProperties(value = {"children", "groupedColumn", "groupedValue"}, ignoreUnknown = true)
public class Statistic extends RecursiveTreeObject<Statistic> {
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

    @JsonIgnore
    public SimpleObjectProperty getDateExamProperty() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date c = new Date(dateExam);
        String date = sdf.format(c);
        return new SimpleObjectProperty(date);
    }

    @JsonIgnore
    public SimpleObjectProperty getMistakesCountProperty() {
        return new SimpleObjectProperty(mistakesCount);
    }

    @JsonIgnore
    public SimpleObjectProperty getRightCountProperty() {
        return new SimpleObjectProperty(rightCount);
    }

    @JsonIgnore
    public SimpleObjectProperty isExamIsPassedProperty() {
        return new SimpleObjectProperty(examIsPassed ? "Сдан" : " Не сдан");
    }

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

