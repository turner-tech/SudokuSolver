
package sudokusolver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 *
 * @author Alex
 */
public class MainMenu implements Initializable {
    
    @FXML
    public GridPane gPane;
    @FXML
    public ChoiceBox choicebox;
    @FXML
    public ProgressIndicator pindicator;
    @FXML
    public Slider slider;
    @FXML
    public Spinner spinner;
    @FXML
    public PasswordField pfield;
    @FXML
    public ComboBox combox;
    @FXML
    public DatePicker dpicker;
    //public TableView tableView;
    @FXML
    public Button button;
    @FXML
    public Label label;
    public ProgressBar pbar;
    public Label percentLabel;
    public Label statusLabel;
    public String address;
    public String password;
    public double slideValue;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
       
        //setSliding();
       
        slideValue = slider.getValue();
        String slideValueString = Double.toString(slideValue);
        SpinnerValueFactory<Integer> valueFactory = spinner.getValueFactory();
        if (valueFactory != null) {
            StringConverter<Integer> converter = valueFactory.getConverter();
            if (converter != null) {
                try{
                    Integer value = converter.fromString(slideValueString);
                    valueFactory.setValue(value);
                } catch(NumberFormatException nfe){
                    spinner.getEditor().setText(converter.toString(valueFactory.getValue()));
                }
            }
        }
    }
    
     @FXML
    private void handleSlider(ActionEvent event) {
        slideValue = slider.getValue();
        button.setText(Double.toString(slideValue));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("Starting");
       
    }
    
   
    
    private void setSliding(){
        slider.setMin(0);
        slider.setMax(50);
         
        
        //final ProgressIndicator pi = new ProgressIndicator(0);
 
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                pbar.setProgress(new_val.doubleValue()/50);
                pindicator.setProgress(new_val.doubleValue()/50);
                percentLabel.setText(Double.toString((double)Math.round(new_val.doubleValue()*2 * 100d) / 100d) + "%");
                
            }
        });
    }
    
    
    public void exitApp(){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Warning! Please read the information below!");
        alert.setContentText("Are you sure you want to close this application?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
            System.exit(0); //or Platform.exit();
        } else {
             // ... user chose CANCEL or closed the dialog
            
        }
        
    }
    public void openNewWindow(ActionEvent e) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("FXML_Level.fxml"));
        stage.setTitle("Sodoku Solver: Solver");
        //stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        //stage.show();
       // Scene scene2 = new Scene(root);
        
        //stage.setScene(scene2);
        ((Node)(e.getSource())).getScene().getWindow().hide();
       
        stage.show();
        
    }
}
        
    

