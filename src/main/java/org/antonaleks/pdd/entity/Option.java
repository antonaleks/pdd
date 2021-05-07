package org.antonaleks.pdd.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Option implements JsonSerializable {
    @JsonProperty("text")
    private String text;
    @JsonProperty("id")
    private int id;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Option option = (Option) o;
        return id == option.id && Objects.equals(text, option.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, id);
    }

    @Override
    public String toString() {
            return id + ". " + text;
    }
}
