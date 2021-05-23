package org.antonaleks.pdd.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.model.Statistic;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements JsonSerializable {
    @JsonProperty("login")
    private String login;

    @Override
    public String toString() {
        return "Пользователь " + login;
    }

    @JsonProperty("password")
    private String password;

    @JsonIgnoreProperties
    public List<Statistic> getStatisticFromDB() {
        List<User> userList = MongoHelper.getInstance().getUserList(and(eq("login", login), eq("password", password)));

        User currentUser = userList.stream().filter(user -> user.equals(this)).findFirst().orElse(null);
        if (!currentUser.statistic.isEmpty())
            statistic = currentUser.statistic;
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
        return PasswordUtils.verifyUserPassword(password, this.password);
    }


    @JsonProperty("role")
    private Role role;

    public User() {

    }

    public User(String login, String password) {
        this.login = login;
        this.password = PasswordUtils.generateSecurePassword(password);
    }

    public User(String login, String password, String role) {
        this(login, password);

        this.role = Role.valueOf(role);
    }


    public void addStatistic(Statistic newStatistic) throws IOException {

        statistic.add(newStatistic);
        MongoHelper.getInstance().updateUser(this);

    }
}

enum Role {

    ADMINS,
    USERS,
    ;
//
//    Role(String role) {
//        this.role = role;
//    }
//
//    private final String role;
//
//    public String getRole() {
//        return this.role;
//    }
}


class PasswordUtils {

    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static final String DEFAULT_SALT = "uR7vFgIyBzELQMoQzroUfYQ7lHIaZX";

    public static String getSalt(int length) {
        StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }

    public static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    public static String generateSecurePassword(String password, String salt) {
        String returnValue = null;
        byte[] securePassword = hash(password.toCharArray(), salt.getBytes());

        returnValue = Base64.getEncoder().encodeToString(securePassword);

        return returnValue;
    }

    public static String generateSecurePassword(String password) {
        String returnValue = null;
        byte[] securePassword = hash(password.toCharArray(), DEFAULT_SALT.getBytes());

        returnValue = Base64.getEncoder().encodeToString(securePassword);

        return returnValue;
    }

    public static boolean verifyUserPassword(String providedPassword,
                                             String securedPassword, String salt) {
        boolean returnValue = false;

        // Generate New secure password with the same salt
        String newSecurePassword = generateSecurePassword(providedPassword, salt);

        // Check if two passwords are equal
        returnValue = newSecurePassword.equalsIgnoreCase(securedPassword);

        return returnValue;
    }

    public static boolean verifyUserPassword(String providedPassword,
                                             String securedPassword) {
        boolean returnValue = false;

        // Generate New secure password with the same salt
        String newSecurePassword = generateSecurePassword(providedPassword, DEFAULT_SALT);

        // Check if two passwords are equal
        returnValue = newSecurePassword.equalsIgnoreCase(securedPassword);

        return returnValue;
    }
}
