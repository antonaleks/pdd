import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.User;
import org.antonaleks.pdd.utils.PropertiesManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

public class LoginTest {


    @Test
    void testLoginPassword() {
        var newUser = new User("user", "user", "USERS");
        var userList = MongoHelper.getInstance().getDocumentList(User.class, PropertiesManager.getDbCollectionUser());
        var currentUser = userList.stream().filter(user -> user.equals(newUser)).findFirst().orElse(null);
        System.out.println(currentUser);
    }

    @Test
    void testPasswordHash() {
        var user = new User("admin", "admin", "ADMINS");
        var user2 = new User("admin", "admin", "ADMINS");
        System.out.println(user.equals(user2));
        System.out.println(user.verifyPassword("admin"));
        System.out.println();
    }

    @Test
    void testNewUserByAdmin() throws IOException {
        MongoHelper.getInstance().removeAll(PropertiesManager.getDbCollectionUser());

        var userList = new ArrayList<User>();
        userList.add(new User("admin", "admin", "ADMINS"));
        userList.add(new User("user", "user", "USERS"));
        MongoHelper.getInstance().<User>insertJsonMany(userList, PropertiesManager.getDbCollectionUser());

    }
}
