package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.StackPane;
import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.User;
import org.antonaleks.pdd.utils.PropertiesManager;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@ViewController(value = "/fxml/UserList.fxml")
public class UserListController extends BaseController {

    private static final String PREFIX = "( ";
    private static final String POSTFIX = " )";
    public JFXPasswordField passwordField;
    public JFXTextField loginField;
    public JFXTextField surnameField;
    public JFXTextField nameField;
    public JFXTextField patronymicField;

    // readonly table view
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXTreeTableView<User> treeTableView;
    @FXML
    private JFXTreeTableColumn<User, String> firstNameColumn;
    @FXML
    private JFXTreeTableColumn<User, String> lastNameColumn;
    @FXML
    private JFXTreeTableColumn<User, String> patronymicColumn;
    @FXML
    private JFXTextField searchField;
    @FXML
    private Label treeTableViewCount;
    @FXML
    private JFXButton treeTableViewAdd;
    @FXML
    private JFXButton treeTableViewRemove;
    @FXML
    private JFXButton statisticButton;
    @FXML
    private JFXButton acceptButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private StackPane root;
    @FXML
    private JFXDialog dialog;
    private ObservableList<User> dummyData;

//    private final String[] names = {"Morley", "Scott", "Kruger", "Lain",
//        "Kennedy", "Gawron", "Han", "Hall", "Aydogdu", "Grace",
//        "Spiers", "Perera", "Smith", "Connoly",
//        "Sokolowski", "Chaow", "James", "June",};
//    private final Random random = new SecureRandom();

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {
        root.getChildren().remove(dialog);
        cancelButton.setOnAction(action -> dialog.close());
        setupReadOnlyTableView();
        root.setOnMouseClicked(i -> treeTableView.getSelectionModel().clearSelection());
    }

    private <T> void setupCellValueFactory(JFXTreeTableColumn<User, T> column, Function<User, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<User, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }

    private void setupReadOnlyTableView() {
        setupCellValueFactory(firstNameColumn, User::getNameProperty);
        setupCellValueFactory(lastNameColumn, User::getSurnameProperty);
        setupCellValueFactory(patronymicColumn, User::getPatronymicProperty);

        dummyData = fillUserData();

        treeTableView.setRoot(new RecursiveTreeItem<>(dummyData, RecursiveTreeObject::getChildren));

        treeTableView.setShowRoot(false);
        treeTableViewCount.textProperty()
                .bind(Bindings.createStringBinding(() -> PREFIX + treeTableView.getCurrentItemsCount() + POSTFIX,
                        treeTableView.currentItemsCountProperty()));
        treeTableViewAdd.disableProperty()
                .bind(Bindings.notEqual(-1, treeTableView.getSelectionModel().selectedIndexProperty()));
        treeTableViewRemove.disableProperty()
                .bind(Bindings.equal(-1, treeTableView.getSelectionModel().selectedIndexProperty()));
        statisticButton.disableProperty()
                .bind(Bindings.equal(-1, treeTableView.getSelectionModel().selectedIndexProperty()));
        treeTableViewAdd.setOnMouseClicked((e) -> {
            System.out.println("add");
            showDialog();
        });
        treeTableViewRemove.setOnMouseClicked((e) -> {
            System.out.println("remove");
            try {
                removeUser();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        searchField.textProperty().addListener(setupSearchField(treeTableView));
    }

    private void showDialog() {
        dialog.setTransitionType(JFXDialog.DialogTransition.RIGHT);
        dialog.show((StackPane) context.getRegisteredObject("ContentPane"));
    }


    private void removeUser() throws IOException {
        User selectedUser = treeTableView.getSelectionModel().getSelectedItem().getValue();
        MongoHelper.getInstance().remove(PropertiesManager.getDbCollectionUser(), and(eq("login", selectedUser.getLogin()), eq("password", selectedUser.getPassword())));
        dummyData.remove(selectedUser);

        final IntegerProperty currCountProp = treeTableView.currentItemsCountProperty();
        currCountProp.set(currCountProp.get() - 1);
    }

    @FXML
    private void addUser() throws IOException {
        User user = new User(loginField.getText(), passwordField.getText(),
                nameField.getText(), surnameField.getText(), patronymicField.getText(), "USERS");
        List<User> users = new ArrayList();
        users.add(user);
        MongoHelper.getInstance().<User>insertJsonMany(users, PropertiesManager.getDbCollectionUser());

        dummyData.addAll(user);
        final IntegerProperty currCountProp = treeTableView.currentItemsCountProperty();
        currCountProp.set(currCountProp.get() + 1);
        dialog.close();

    }

    private ChangeListener<String> setupSearchField(final JFXTreeTableView<User> tableView) {
        return (o, oldVal, newVal) ->
                tableView.setPredicate(UserProp -> {
                    final User user = UserProp.getValue();
                    return user.getSurname().contains(newVal)
                            || user.getName().contains(newVal)
                            || user.getPatronymic().contains(newVal);
                });
    }

    private ObservableList<User> fillUserData() {
        final ObservableList<User> dummyData = FXCollections.observableArrayList();
        List<User> userList = MongoHelper.getInstance().getUserList(eq("role", "USERS"), null);//MongoHelper.getInstance().getDocumentList(User.class, PropertiesManager.getDbCollectionUser());
        dummyData.addAll(userList);

        return dummyData;
    }

    @FXML
    private void openStatistic() {
        User selectedUser = treeTableView.getSelectionModel().getSelectedItem().getValue();
        if (selectedUser != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/fxml/Statistic.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            StatisticController dataController = loader.getController();

            try {
                dataController.initialize(selectedUser);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            loadModalWindow("Тренировка", root);
        }
    }


}
