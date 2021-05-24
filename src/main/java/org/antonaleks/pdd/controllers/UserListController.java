package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.datafx.controller.ViewController;
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
import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.User;
import org.antonaleks.pdd.utils.PropertiesManager;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@ViewController(value = "/fxml/UserList.fxml")
public class UserListController extends BaseController {

    private static final String PREFIX = "( ";
    private static final String POSTFIX = " )";

    // readonly table view
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
        setupReadOnlyTableView();
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

        ObservableList<User> dummyData = fillUserData();

        treeTableView.setRoot(new RecursiveTreeItem<>(dummyData, RecursiveTreeObject::getChildren));

        treeTableView.setShowRoot(false);
        treeTableViewCount.textProperty()
                .bind(Bindings.createStringBinding(() -> PREFIX + treeTableView.getCurrentItemsCount() + POSTFIX,
                        treeTableView.currentItemsCountProperty()));
        treeTableViewAdd.disableProperty()
                .bind(Bindings.notEqual(-1, treeTableView.getSelectionModel().selectedIndexProperty()));
        treeTableViewRemove.disableProperty()
                .bind(Bindings.equal(-1, treeTableView.getSelectionModel().selectedIndexProperty()));
        treeTableViewAdd.setOnMouseClicked((e) -> {
            System.out.println("add");

            final IntegerProperty currCountProp = treeTableView.currentItemsCountProperty();
            currCountProp.set(currCountProp.get() + 1);
        });
        treeTableViewRemove.setOnMouseClicked((e) -> {
            System.out.println("remove");

            final IntegerProperty currCountProp = treeTableView.currentItemsCountProperty();
            currCountProp.set(currCountProp.get() - 1);
        });
        searchField.textProperty().addListener(setupSearchField(treeTableView));
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
        List<User> userList = MongoHelper.getInstance().getDocumentList(User.class, PropertiesManager.getDbCollectionUser());

        dummyData.addAll(userList);

        return dummyData;
    }

    @FXML
    private void openStatistic() {
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
            dataController.initialize(treeTableView.getSelectionModel().getSelectedItem().getValue());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        loadModalWindow("Тренировка", root);
    }


}
