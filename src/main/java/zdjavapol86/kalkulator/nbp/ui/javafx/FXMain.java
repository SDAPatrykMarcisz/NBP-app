package zdjavapol86.kalkulator.nbp.ui.javafx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import zdjavapol86.kalkulator.nbp.controller.Controller;
import zdjavapol86.kalkulator.nbp.model.dto.OperationSummary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FXMain extends Application {

    @FXML
    private ComboBox<String> sourceCurrencyComboBox;

    @FXML
    private ComboBox<String> destinationCurrencyComboBox;

    @FXML
    private DatePicker exchangeRateDatePicker;

    @FXML
    private TextField currencyAmountLabel;

    @FXML
    private TextField resultAmountLabel;

    private final Controller controller = new Controller();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("template.fxml"));
        stage.setTitle("Registration Form FXML Application");
        stage.setScene(new Scene(root, 400, 250));
        stage.show();

    }

    @FXML
    public void initialize() {
        resultAmountLabel.setEditable(false);
        sourceCurrencyComboBox.setItems(FXCollections.observableList(new ArrayList<>(Arrays.asList("PLN"))));
        sourceCurrencyComboBox.setValue("PLN");
        destinationCurrencyComboBox.setItems(FXCollections.observableList(new ArrayList<>(Arrays.asList("EUR"))));
        destinationCurrencyComboBox.setValue("EUR");

        currencyAmountLabel.textProperty().addListener(
                ((observableValue, oldVal, newVal) -> {
                        CalculationTask task = CalculationTask.builder()
                                .currencyFrom(sourceCurrencyComboBox.getValue())
                                .currencyTo(destinationCurrencyComboBox.getValue())
                                .amount(newVal)
                                .date(exchangeRateDatePicker.getValue().format(DateTimeFormatter.ISO_DATE))
                                .build();

                        task.setOnRunning(event -> currencyAmountLabel.setDisable(true));
                        task.setOnSucceeded(event -> {
                            updateControlls(task.getValue());
                            currencyAmountLabel.setDisable(false);
                            currencyAmountLabel.requestFocus();
                        });
                        ExecutorService executorService = Executors.newFixedThreadPool(1);
                        executorService.execute(task);
                        executorService.shutdown();
                })
        );

//        currencyAmountLabel.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                System.out.println(actionEvent);
//                CalculationTask task = CalculationTask.builder()
//                        .currencyFrom(sourceCurrencyComboBox.getValue())
//                        .currencyTo(destinationCurrencyComboBox.getValue())
//                        .amount(currencyAmountLabel.getText())
//                        .date(exchangeRateDatePicker.getValue().format(DateTimeFormatter.ISO_DATE))
//                        .build();
//
//                task.setOnRunning(event -> currencyAmountLabel.setDisable(true));
//                task.setOnSucceeded(event -> currencyAmountLabel.setDisable(false));
//                ExecutorService executorService = Executors.newFixedThreadPool(1);
//                executorService.execute(task);
//                executorService.shutdown();
//            }
//        });

        OperationSummary calculate = controller.calculate("PLN", "EUR", "20.0", "2021-07-29");
        updateControlls(calculate);
    }

    private boolean timeFromLastEventGreaterThan(LocalDateTime now, LocalDateTime lastTypingTime, int seconds) {
        return ChronoUnit.SECONDS.between(lastTypingTime, now) >= seconds;
    }

    private void updateControlls(OperationSummary calculate) {
        sourceCurrencyComboBox.setPromptText(calculate.getCurrencyFromName());
        destinationCurrencyComboBox.setPromptText(calculate.getCurrencyToName());
        exchangeRateDatePicker.setValue(LocalDate.parse(calculate.getExchangeDate()));
        currencyAmountLabel.setText(calculate.getAmountToConvert());
        resultAmountLabel.setText(calculate.getOperationResult());
    }
}
