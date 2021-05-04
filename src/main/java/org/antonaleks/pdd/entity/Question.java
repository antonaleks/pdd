package org.antonaleks.pdd.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.antonaleks.pdd.serialization.ImageDeserializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Question implements JsonSerializable {
    private int number;

    private int id;

    private int blockNumber;

    private int ticketNumber;

    private int cat;

    private String text;

    public String getImage() {
        return image;
    }

    @JsonDeserialize(using = ImageDeserializer.class)
    private String image;
    private String comment;
    private int rightOption;

    private ArrayList<Option> options;

    private ArrayList<Topic> topics;


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<Option> getOptions() {
        return options;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public int getCat() {
        return cat;
    }

    public void setCat(int cat) {
        this.cat = cat;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRightOption() {
        return rightOption;
    }

    public void setRightOption(int rightOption) {
        this.rightOption = rightOption;
    }

    public ArrayList<Topic> getTopics() {
        return topics;
    }

    private String toJson(List list) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(out, list);

        return out.toString();
    }

    public String getTopicsJson() throws IOException {
        return toJson(topics);
    }

    public String getOptionsJson() throws IOException {
        return toJson(options);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return number == question.number && id == question.id && blockNumber == question.blockNumber && ticketNumber == question.ticketNumber && cat == question.cat && rightOption == question.rightOption && Objects.equals(text, question.text) && Objects.equals(image, question.image) && Objects.equals(comment, question.comment) && Objects.equals(options, question.options) && Objects.equals(topics, question.topics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, id, blockNumber, ticketNumber, cat, text, image, comment, rightOption, options, topics);
    }

    @Override
    public String toString() {
            return getText();
    }
}
