import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.Statistic;
import org.antonaleks.pdd.entity.User;
import org.antonaleks.pdd.utils.PropertiesManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class LoginTest {


    @Test
    void testLoginPassword() {
        User newUser = new User("user", "user", "Иванов", "Иван", "Иванович", "USERS");
        List<User> userList = MongoHelper.getInstance().getDocumentList(User.class, PropertiesManager.getDbCollectionUser());
        User currentUser = userList.stream().filter(user -> user.equals(newUser)).findFirst().orElse(null);
        System.out.println(currentUser);
    }

    @Test
    void testLoginWithoutStatistic() throws IOException {
        User newUser = new User("test", "test", "USERS");
        List<User> userList = MongoHelper.getInstance().getUserList(eq("login", newUser.getLogin()), "login", "password");
        User currentUser = userList.stream().filter(user -> user.equals(newUser)).findFirst().orElse(null);
        currentUser.getStatisticFromDB();
        Statistic statistic = new Statistic(Timestamp.from(Instant.now()).getTime(), 2, 28, true);
        currentUser.addStatistic(statistic);
        currentUser.updateStatistic();
        currentUser.getStatisticFromDB();

        System.out.println(currentUser);
    }

    @Test
    void testPasswordHash() {
        User user = new User("admin", "admin", "ADMINS");
        User user2 = new User("admin", "admin", "ADMINS");
        System.out.println(user.equals(user2));
        System.out.println(user.verifyPassword("admin"));
        System.out.println();
    }

    @Test
    void testNewUserByAdmin() throws IOException {
        MongoHelper.getInstance().removeAll(PropertiesManager.getDbCollectionUser());

        List<User> userList = new ArrayList<User>();
        userList.add(new User("admin", "admin", "ADMINS"));
//        userList.add(new User("user", "user", "Федоров", "Иван", "Иванович", "USERS"));
        MongoHelper.getInstance().<User>insertJsonMany(userList, PropertiesManager.getDbCollectionUser());

    }

    @Test
    void testNewUserWithStatistic() throws IOException {
        //MongoHelper.getInstance().removeAll(PropertiesManager.getDbCollectionUser());

        List<User> userList = new ArrayList<User>();
        Statistic statistic = new Statistic(Timestamp.from(Instant.now()).getTime(), 2, 28, true);
        User user = new User("test", "test", "Иванов", "Иван", "Иванович", "USERS");
        user.addStatistic(statistic);
        userList.add(user);
        MongoHelper.getInstance().<User>insertJsonMany(userList, PropertiesManager.getDbCollectionUser());

    }
}
