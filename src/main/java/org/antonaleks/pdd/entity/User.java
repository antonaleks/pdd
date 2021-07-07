package org.antonaleks.pdd.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleObjectProperty;
import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.model.Role;
import org.antonaleks.pdd.utils.HashUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@JsonIgnoreProperties(value = {"children", "groupedColumn", "groupedValue"}, ignoreUnknown = true)
public class User extends RecursiveTreeObject<User> implements JsonSerializable {

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("name")
    private String name;

    @JsonProperty("surname")
    public String getSurname() {
        return surname;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("patronymic")
    public String getPatronymic() {
        return patronymic;
    }

    @JsonIgnore
    public SimpleObjectProperty getSurnameProperty() {
        return new SimpleObjectProperty(surname);
    }

    @JsonIgnore
    public SimpleObjectProperty getNameProperty() {
        return new SimpleObjectProperty(name);
    }

    @JsonIgnore
    public SimpleObjectProperty getPatronymicProperty() {
        return new SimpleObjectProperty(patronymic);
    }

    @JsonIgnore
    public SimpleObjectProperty getLoginProperty() {
        return new SimpleObjectProperty(login);
    }

    @JsonIgnore
    public SimpleObjectProperty getPasswordProperty() {
        return new SimpleObjectProperty("***");
    }

    @JsonProperty("patronymic")
    private String patronymic;

    @JsonProperty("login")
    private String login;

    @Override
    public String toString() {
        return String.format("Пользователь: %s %s %s", surname, name, patronymic);
    }

    @JsonIgnore
    public void setPassword(String password) {
        this.password = HashUtils.generateSecurePassword(password);
    }

    @JsonProperty("password")
    private String password;


    @JsonIgnore
    public List<Statistic> getStatisticFromDB() {
        List<User> userList = MongoHelper.getInstance().getUserList(and(eq("login", login), eq("password", password)));

        User currentUser = userList.stream().filter(user -> user.equals(this)).findFirst().orElse(null);
        if (!currentUser.statistic.isEmpty())
            statistic = currentUser.statistic;
        else
            statistic = new ArrayList<>();
        return statistic;
    }

    public List<Statistic> getStatistic() {

        return statistic;
    }


    @JsonProperty("statistic")
    private List<Statistic> statistic = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(login, user.login) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public boolean verifyPassword(String password) {
        return HashUtils.verifyHashString(password, this.password);
    }


    @JsonProperty("role")
    private Role role;

    public User() {

    }


    public User(String login, String password, String surname, String name, String patronymic) {
        this.login = login;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.password = HashUtils.generateSecurePassword(password);
    }

    public User(String login, String password) {
        this.login = login;
        this.password = HashUtils.generateSecurePassword(password);
    }

    public User(String login, String password, String role) {
        this(login, password, "", "", "");

        this.role = Role.valueOf(role);
    }

    public User(String login, String password, String surname, String name, String patronymic, String role) {
        this(login, password, surname, name, patronymic);

        this.role = Role.valueOf(role);
    }


    public void addStatistic(Statistic newStatistic) {

        statistic.add(newStatistic);

    }

    public void updateStatistic() throws IOException {
        MongoHelper.getInstance().updateUserStatistic(this);
    }

    public void update() throws IOException {
        MongoHelper.getInstance().updateUser(this);
    }
}



