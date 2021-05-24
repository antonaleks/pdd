package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Stage;
import org.antonaleks.pdd.entity.User;
import org.antonaleks.pdd.model.Statistic;

import java.io.IOException;
import java.util.Date;
import java.util.function.Function;

//@ViewController(value = "/fxml/Main.fxml", title = "PDD")
public class StatisticController extends BaseController {


    public Label timerLabel;
    public Label userLabel;
    public Label examPassedLabel;
    public Label examNoFailLabel;
    public Label examFailLabel;
    public JFXTreeTableView<Statistic> treeTableView;
    public JFXTreeTableColumn<Statistic, Date> dateColumn;
    public JFXTreeTableColumn<Statistic, Integer> countMistakesColumn;
    public JFXTreeTableColumn<Statistic, Integer> rightCountColumn;
    public JFXTreeTableColumn<Statistic, String> resultColumn;
    public JFXButton closeButton;


    @FXML
    public void initialize(User user) throws IOException {
        userLabel.setText(user.toString());
        int succeed = (int) user.getStatistic().stream().filter(i -> i.isExamIsPassed()).count();
        int fails = user.getStatistic().size() - succeed;
        examPassedLabel.setText("Экзаменов пройдено: " + user.getStatistic().size());
        examFailLabel.setText("Экзаменов провалено: " + fails);
        examNoFailLabel.setText("Экзаменов сдано: " + succeed);


        setupCellValueFactory(dateColumn, Statistic::getDateExamProperty);
        setupCellValueFactory(countMistakesColumn, Statistic::getMistakesCountProperty);
        setupCellValueFactory(rightCountColumn, Statistic::getRightCountProperty);
        setupCellValueFactory(resultColumn, Statistic::isExamIsPassedProperty);


        final ObservableList<Statistic> dummyData = FXCollections.observableArrayList();

        dummyData.addAll(user.getStatisticFromDB());

        treeTableView.setRoot(new RecursiveTreeItem<>(dummyData, RecursiveTreeObject::getChildren));

        treeTableView.setShowRoot(false);

    }

    private <T> void setupCellValueFactory(JFXTreeTableColumn<Statistic, T> column, Function<Statistic, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<Statistic, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }

    public void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

    }


}
