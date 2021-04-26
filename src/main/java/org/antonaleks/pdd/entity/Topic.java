package org.antonaleks.pdd.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Topic implements JsonSerializable {
    @JsonProperty("name")
    private String name;
    @JsonProperty("id")
    private int id;
    @JsonProperty("weight")
    private int weight;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return id == topic.id && weight == topic.weight && Objects.equals(name, topic.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, weight);
    }

    @Override
    public String toString() {
        return id + "." + " " + name;
    }

    public String toStringJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
