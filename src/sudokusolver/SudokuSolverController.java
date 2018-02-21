package src.sudokusolver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

//*************************************************************
//Alexander Turner
//CS 4242: Assignment 0: Sudoku Solver
//Jan. 20th, 2017
//Program that attemps to solve Sudoku puzzles.
//*************************************************************

public class SudokuSolverController implements Initializable {
    
    @FXML
    public GridPane gPane;
    public ProgressIndicator pindicator;
    public Slider slider;
    public Spinner spinner;
    public Button button;
    public Button startButton;
    public Button nextButton;
    public Label label;
    public ProgressBar pbar;
    public Label statusLabel;
    public double slideValue;
    public Label zero_zero;
    public List<String> sudokuNumbers;
    public List<String> sudokuNumbers2;
    //public int[][] theGrid = new int[9][9];
    public GridNode[][] theGrid = new GridNode[9][9];
    public GridNode[][] solvedGrid = new GridNode[9][9];
    public ArrayList<Integer> visitedNonetList = new ArrayList<>();
    public ArrayList<Integer> visitedRowList = new ArrayList<>();
    public ArrayList<Integer> visitedColumnList = new ArrayList<>();
    public ArrayList<Integer> tempPlacementList = new ArrayList<>();
    public boolean repeat = true;
    public boolean numberAdded = false;
    public boolean firstRun = true;
    public int currentNonet = 0;
    public TextField textField;
    public int correctCount = 0;
    public Label correctLabel;
    public Button solveButton;
    public Button saveFileButton;
    public Button checkButton;
    
    
    
    //First part of the file to run. Here I set up what buttons are active
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("Starting");
        //handleButtonAction(new ActionEvent());
      
       startButton.setDisable(true);
       nextButton.setDisable(true);
       solveButton.setDisable(true);
       saveFileButton.setDisable(true);
       checkButton.setDisable(true);
    }
   
    //This method controls the exiting of the app when you press File -> Close
    public void exitApp(){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Warning! Please read the information below!");
        alert.setContentText("Are you sure you want to close this application?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
 
            System.exit(0);
        } 
    }
 
    //Creates a new filechooser window
    public File openTheFile(FileChooser fileChooser, Stage stage){
        return fileChooser.showOpenDialog(stage);
        
    }
    
    //This method has the user choose a file to solve, and then copies the contents to a string to be used later
    public void openFile(){
        firstRun = true;
        theGrid = new GridNode[9][9];
        startButton.setText("Start");
        nextButton.setDisable(true);
        startButton.setDisable(true);
        
        String str = null;
        StringBuilder sb = new StringBuilder();
        
        
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setTitle("Pick a Text File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir"))    //"user.dir"
        );                 
        fileChooser.getExtensionFilters().addAll(
        //new FileChooser.ExtensionFilter("Text Files", "*.*"),
        new FileChooser.ExtensionFilter(".txt file", "*.txt"),
        new FileChooser.ExtensionFilter(".csv file", "*.csv")
        //new FileChooser.ExtensionFilter(".doc", "*.doc"),
        //new FileChooser.ExtensionFilter(".docx", "*.docx")        
        );
        File file = openTheFile(fileChooser, stage);
        
        
        while (((file == null))){
            file = openTheFile(fileChooser, stage);
            if (!(file == null)){
                startButton.setDisable(false);
                
                break;
            }
        }
        if (!(file == null)){
                startButton.setDisable(false);
                
            }
        
        try {
            
                
            BufferedReader in = new BufferedReader(new FileReader(file));
            
            while ((str = in.readLine()) != null) {
                //System.out.println(str);
                sb.append(str);
               
            }

            in.close();
        } catch (IOException e) {
            System.out.println("File Read Error");
           
        }
        String result = sb.toString();
        result = result.replaceAll("\\s+","");
        sudokuNumbers = Arrays.asList(result.split(","));
        //System.out.println(result);
        //System.out.println(sudokuNumbers);
    }
    
    //This method has the user choose a file to verify solver, creates a string of the files contents to be used later
     public void checkBoard(){    
        sudokuNumbers = new ArrayList<String>(); 
        firstRun = true;
        solvedGrid = new GridNode[9][9];
        //startButton.setText("Start");
        //nextButton.setDisable(true);
        //startButton.setDisable(true);
        
        String str = null;
        StringBuilder sb = new StringBuilder();
        
        
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setTitle("Pick a Text File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir"))
        );                 
        fileChooser.getExtensionFilters().addAll(
        //new FileChooser.ExtensionFilter("Text Files", "*.*"),
        new FileChooser.ExtensionFilter(".csv file", "*.csv"),
        new FileChooser.ExtensionFilter(".txt file", "*.txt")
        
        //new FileChooser.ExtensionFilter(".doc", "*.doc"),
        //new FileChooser.ExtensionFilter(".docx", "*.docx")        
        );
        File file = openTheFile(fileChooser, stage);
        
        
        while (((file == null))){
            file = openTheFile(fileChooser, stage);
            if (!(file == null)){
                startButton.setDisable(false);
                
                break;
            }
        }
        if (!(file == null)){
                startButton.setDisable(false);
                
            }
        
        try {
            
                
            BufferedReader in = new BufferedReader(new FileReader(file));
            
            while ((str = in.readLine()) != null) {
                //System.out.println(str);
                sb.append(str);
               
            }

            in.close();
        } catch (IOException e) {
            System.out.println("File Read Error");
           
        }
        String result = sb.toString();
        result = result.replaceAll("\\s+","");
        sudokuNumbers = Arrays.asList(result.split(","));
        //System.out.println(result);
        //System.out.println(sudokuNumbers);
        checking();
        compareGrid();
        startButton.setDisable(true);
    }
    
    //Method responsible for comparing the solver with its solution, and shows what was right and wrong 
    public void compareGrid(){
        correctCount = 0;
        for (int j = 0; j < 9; j++){
            for (int i = 0; i < 9; i++){
                if (!((theGrid[j][i].getNum()) == (solvedGrid[j][i].getNum()))){
                    theGrid[j][i].setNum(solvedGrid[j][i].getNum());
                    theGrid[j][i].color = 1;
                }
                else{
                    correctCount++;
                    correctLabel.setText(Integer.toString(correctCount));
                    theGrid[j][i].color = 2;
                }
            }
        }
        setGrid();
    } 

    
    //Allows the option of opening to the Main Menu screen. It is not utilized.
    public void openNewWindow(ActionEvent e) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("FXML_MainMenu.fxml"));
        stage.setTitle("Sodoku Solver: Main Menu");
        //stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
       
        stage.show();
    }
    
    //Opens the SudokuSolver window. Is it not utilized.
     public void openSolveWindow() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("FXML_SudokuSolver.fxml"));
        stage.setTitle("Sudoku Solver: Solver");
        //stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
       
        stage.show();
    }
     
    
    //Button controller for the start button, it takes the chosen file and through various methods, prints values to the grid
    @FXML
    private void startAction(){
        startButton.setText("Reset");
        int resetCount = 0;
        int k = 0;
        int nCount = 0;
        int tempVar = 0;
        for (int j = 0; j < 9; j++){
            switch (j) {
            case 3:  nCount = 3;
                     break;
            case 4:  nCount = 3;
                     break;       
            case 5:  nCount = 3;
                     break;
            case 6:  nCount = 6;
                     break;
            case 7:  nCount = 6;
                     break;
            case 8:  nCount = 6;
                     break;
            default: nCount = 0;
                     break;
            }
            
            if (((j % 3 ) == 0) && (j>0)){
                nCount = j;
            }
           
            for (int i = 0; i< 9; i++, k++){
                if (((i % 3 ) == 0) && (i>0)){
                    nCount++;
                }
                GridNode gp = new GridNode(j, i, nCount, Integer.parseInt(sudokuNumbers.get(k)));
                theGrid[j][i]= gp;
                //System.out.println("j: "+j+" i: "+i+ " Nonet: " + nCount); 
            }
        }
        setGrid();
        nextButton.setDisable(false);
        startButton.setDisable(false);
        solveButton.setDisable(false);
        saveFileButton.setDisable(false);
        checkButton.setDisable(false);
        
    }
    
    //Takes the string created earlier and puts it into the solvedGrid 
    public void checking(){
        int resetCount = 0;
        int k = 0;
        int nCount = 0;
        int tempVar = 0;
        for (int j = 0; j < 9; j++){
            switch (j) {
            case 3:  nCount = 3;
                     break;
            case 4:  nCount = 3;
                     break;       
            case 5:  nCount = 3;
                     break;
            case 6:  nCount = 6;
                     break;
            case 7:  nCount = 6;
                     break;
            case 8:  nCount = 6;
                     break;
            default: nCount = 0;
                     break;
            }
            
            if (((j % 3 ) == 0) && (j>0)){
                nCount = j;
            }
           
            for (int i = 0; i< 9; i++, k++){
                if (((i % 3 ) == 0) && (i>0)){
                    nCount++;
                }
                GridNode gp = new GridNode(j, i, nCount, Integer.parseInt(sudokuNumbers.get(k)));
                solvedGrid[j][i]= gp;
                //System.out.println("j: "+j+" i: "+i+ " Nonet: " + nCount); 
            }
        }
    }
    
    
    //Button handler for the next button. This solves one number at a time.
    @FXML
    private void nextAction(ActionEvent event){
        //Reset the visitedLists here???
        visitedNonetList = new ArrayList<Integer>();
        visitedRowList = new ArrayList<Integer>();
        visitedColumnList = new ArrayList<Integer>();
        clearGoodList();
        repeat = true;
        numberAdded = false;
        //Here is where I will add the code to allow me to implement the step by step AI system
        //checkVal(2,4,1);
        currentNonet = 0;
        for (int j = 0; j < 4; j++){
            for (int i = 0; i < 9; i++){
               newNextNumber(0);
               addBadNonetNumber(currentNonet);
               clearGoodList();

               newNextNumber(1);
               addBadRowNumber(currentNonet);
               clearGoodList();
               
               newNextNumber(2);
               addBadRowNumber(currentNonet);
               clearGoodList();
               
               nextNumber(0);
               addNumber();
               clearGoodList();

               nextNumber(1);
               addNumber();
               clearGoodList();

               nextNumber(2);
               addNumber();
               clearGoodList();

               currentNonet++;

            }

            setGrid(); //move more periodically not here
            }
        }
    
    //Button handler for the Solve button. This solves as many numbers as possible
     @FXML
    private void solveAction(ActionEvent event){
       for (int j = 0; j < 15; j++){ 
            //Reset the visitedLists here???
            visitedNonetList = new ArrayList<Integer>();
            visitedRowList = new ArrayList<Integer>();
            visitedColumnList = new ArrayList<Integer>();
            clearGoodList();
            repeat = true;
            numberAdded = false;
            currentNonet = 0;
            //Here is where I will add the code to allow me to implement the step by step AI system
            //checkVal(2,4,1);
            for (int k = 0; k < 4; k++){
                for (int i = 0; i < 9; i++){
                    newNextNumber(0);
                    addBadNonetNumber(currentNonet);
                    //currentNonet++;
                    clearGoodList();
                    numberAdded = false;

                    newNextNumber(1);
                    addBadRowNumber(currentNonet);
                    clearGoodList();
                    numberAdded = false;

                    newNextNumber(2);
                    addBadRowNumber(currentNonet);
                    clearGoodList();
                    numberAdded = false;

                    nextNumber(0);
                    addNumber();
                    clearGoodList();
                    numberAdded = false;

                    nextNumber(1);
                    addNumber();
                    clearGoodList();
                    numberAdded = false;

                    nextNumber(2);
                    addNumber();
                    clearGoodList();
                    numberAdded = false;

                    currentNonet++;
                }
            }
            setGrid(); //move more periodically not here
       }
    }
    
    //Saves a file based on what is on the board and what name is inserted into the textField
    public void write() throws IOException{
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
              new FileOutputStream("src/sudokusolver/puzzles/solutions/" + textField.getText()+".csv"), "utf-8"))) {
            for (int j = 0; j < 9; j++){  
                for (int i = 0; i < 9; i++){
                
                    writer.write(Integer.toString(theGrid[j][i].getNum()));
                    writer.write(",");
            
                }
                //writer.write("/n");
            }    
        }
    }

    //Chooses the next best row to evaluate.
    public int chooseRow(){
        ArrayList<Integer> rowList = new ArrayList<Integer>();
        for (int k = 0; k < 9; k++){
            rowList.add(0);
        }
        
        for (int j = 0; j < 9; j++){
            for (int i = 0; i < 9; i++){
                if (theGrid[j][i].getNum() == 0 ){
                    rowList.set(theGrid[j][i].getRow() , (rowList.get(theGrid[j][i].getRow()) + 1));
                }
            }        
        }
        //System.out.println(rowList);
        int temp = 8;
        int row = 0;
        for (int l = 0; l < 9; l++){
            if ((rowList.get(l) <= temp) && (rowList.get(l) > 0)){
                if (visitedRowList.contains(l)){
                    
                }
                else{
                    temp = rowList.get(l);
                    row = l;
                }
            }    
        }
        //System.out.println("Returning Row: " + row + " with the value: " + temp);
        visitedRowList.add(row);
        return row; ///Change this from 0
    }
    
    //Chooses the next best column to evaluate.
    public int chooseColumn(){
        ArrayList<Integer> columnList = new ArrayList<Integer>();
        for (int k = 0; k < 9; k++){
            columnList.add(0);
        }
        
        for (int j = 0; j < 9; j++){
            for (int i = 0; i < 9; i++){
                if (theGrid[j][i].getNum() == 0 ){
                    columnList.set(theGrid[j][i].getColumn() , (columnList.get(theGrid[j][i].getColumn()) + 1));
                }
            }        
        }
        //System.out.println(columnList);
        int temp = 8;
        int column = 0;
        for (int l = 0; l < 9; l++){
            if ((columnList.get(l) <= temp) && (columnList.get(l) > 0)){
                if (visitedColumnList.contains(l)){
                    
                }
                else{
                    temp = columnList.get(l);
                    column = l;
                }
            }    
        }
        //System.out.println("Returning Column: " + column + " with the value: " + temp);
        visitedColumnList.add(column);
        return column; ///Change this from 0
    }
    
    //Chooses the next best nonet to evaluate.
    public int chooseNonet(){
        ArrayList<Integer> nonetList = new ArrayList<Integer>();
        for (int k = 0; k < 9; k++){
            nonetList.add(0);
        }
        
        for (int j = 0; j < 9; j++){
            for (int i = 0; i < 9; i++){
                if (theGrid[j][i].getNum() == 0 ){
                    nonetList.set(theGrid[j][i].getNonet() , (nonetList.get(theGrid[j][i].getNonet()) + 1));
                }
            }        
        }
        //System.out.println(nonetList);
        int temp = 8;
        int nonet = 0;
       for (int l = 0; l < 9; l++){
            if ((nonetList.get(l) <= temp) && (nonetList.get(l) > 0)){
                if (visitedNonetList.contains(l)){
                    
                }
                else{
                    temp = nonetList.get(l);
                    nonet = l;
                }
            }    
        }
        //System.out.println("Returning Nonet: " + nonet + " with the value: " + temp);
        visitedNonetList.add(nonet);
        return nonet; 
    }
    
    
    //Returns if the nonet contains a value or not
    private boolean checkNonet(int placement, int val){
        
        boolean present = false;
        int resetCount = 0;
        int k = 0;
        int nCount = 0;
        int tempVar = 0;
        int newJ = 0;
        int newI = 0;
        
        switch (placement){
            case 0:
                    newJ = 0;
                    newI = 0;
                    break;
            case 1:
                    newJ = 0;
                    newI = 3;
                    break;
            case 2:
                    newJ = 0;
                    newI = 6;
                    break;
            case 3:
                    newJ = 3;
                    newI = 0;
                    break;
            case 4:
                    newJ = 3;
                    newI = 3;
                    break;
            case 5:
                    newJ = 3;
                    newI = 6;
                    break;
            case 6:
                    newJ = 6;
                    newI = 0;
                    break;
            case 7:
                    newJ = 6;
                    newI = 3;
                    break;
            case 8:
                    newJ = 6;
                    newI = 6;
                    break;
            default:
                    break;
        }

        int jBound = newJ + 3;
        int iBound = newI + 3;
        
        //System.out.println(" j is: " + newJ + " with bounds " + jBound + " and i is: " + newI + " with bounds " + iBound);
        
        for (int j = newJ; j < jBound; j++){
            switch (j) {
            case 3:  nCount = 3;
                     break;
            case 4:  nCount = 3;
                     break;       
            case 5:  nCount = 3;
                     break;
            case 6:  nCount = 6;
                     break;
            case 7:  nCount = 6;
                     break;
            case 8:  nCount = 6;
                     break;
            default: nCount = 0;
                     break;
            }
            
            if (((j % 3 ) == 0) && (j>0)){
                nCount = j;
            }
           
            for (int i = newI; i< iBound; i++, k++){
                if (((i % 3 ) == 0) && (i>0)){
                    nCount++;
                }
                
                if (theGrid[j][i].getNum() == val){
                    present = true;
                }
                
                //System.out.println("j: "+j+" i: "+i+ " Nonet: " + nCount +" (" + theGrid[j][i].getRow() + ", " + theGrid[j][i].getColumn() + ")"); 
            }
        }
        
        return present;
    }
    
    //Returns if the nonet/row/column contains a value or not
    public boolean checkVal(int type, int placement, int val){
        boolean present = false;
        switch (type) {
            case 0:  //nonet                
                int tempType = type;
                    present = checkNonet(placement, val);
                     break;
            case 1:  //row
                    for (int i = 0; i < 9; i++){
                        if (theGrid[placement][i].getNum() == val){
                            present = true;
                        }
                    }
                     break;       
            default: //column
                    for (int i = 0; i < 9; i++){
                        if (theGrid[i][placement].getNum() == val){
                            present = true;
                        }
                    }
                     break;
        }
        //System.out.println(" " + val + " is " + present);
        return present;//present;
    }
    
    //Method that will add a number to the grid if it is the only value that appears in the nodes goodList 
    public void addNumber(){
        for (int j = 0; j < 9; j++){
            for (int i = 0; i < 9; i++){
                if (theGrid[j][i].goodList.size() == 1){
                   if (!(numberAdded)){
                        theGrid[j][i].setNum(theGrid[j][i].goodList.get(0));
                        //theGrid[j][i].setTextFill(Color.web("#008000"));
                        theGrid[j][i].goodList.remove(0);
                        setGrid();
                        numberAdded = true;
                   }
                }
            }
        }
    }
    
    //Method that will add a number to the grid if it is the only value that doesn't appears in the nodes badList when it appears in the rest of the nonets badLists
    public void addBadNonetNumber(int nonet){
        int potentialNumber = 0;
        int oneCount = 0;
        int twoCount = 0;
        int threeCount = 0;
        int fourCount = 0;
        int fiveCount = 0;
        int sixCount = 0;
        int sevenCount = 0;
        int eightCount = 0;
        int nineCount = 0;
        
        int badNodeCount = 0;
        for (int k = 0; k < 9; k++){
            potentialNumber++;
            
            for (int j = 0; j < 9; j++){
                for (int i = 0; i < 9; i++){
                    if (theGrid[j][i].getNonet() == nonet){
                        if ((theGrid[j][i].badList.contains(potentialNumber))){
                            switch(potentialNumber){
                                case 1:
                                    oneCount++;
                                        break;
                                case 2:
                                    twoCount++;
                                        break;
                                case 3:
                                    threeCount++;
                                        break;
                                case 4:
                                    fourCount++;
                                        break;
                                case 5:
                                    fiveCount++;
                                        break;
                                case 6:
                                    sixCount++;
                                        break;
                                case 7:
                                    sevenCount++;
                                        break;
                                case 8:
                                    eightCount++;
                                        break;
                                case 9:
                                    nineCount++;
                                        break;
                                default:
                                        break;
                            }
                        }
                        else{
                           
                        }
                    }
                }
            }  
        }
        potentialNumber = 0;
        for (int k = 0; k < 9; k++){
            potentialNumber++;
            
            for (int j = 0; j < 9; j++){
                for (int i = 0; i < 9; i++){
                    //System.out.println("BadList: " + j + ", " + i + ": " + theGrid[j][i].badList);
                                    
                    if (theGrid[j][i].getNonet() == nonet){
                        if (!(theGrid[j][i].badList.contains(potentialNumber))){
                             switch(potentialNumber){
                                case 1:
                                    if (oneCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            } 
                                        }
                                    }
                                        break;
                                case 2:
                                    if (twoCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 3:
                                    if (threeCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 4:
                                    if (fourCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 5:
                                    if (fiveCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 6:
                                    if (sixCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 7:
                                    if (sevenCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 8:
                                    if (eightCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 9:
                                    if (nineCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                default:
                                        break;
                            }
                        }
                        
                    }
                }
            }
        }    
    }
    
    //Method that will add a number to the grid if it is the only value that doesn't appears in the nodes badList when it appears in the rest of the rows badLists
    public void addBadRowNumber(int row){
        int potentialNumber = 0;
        int oneCount = 0;
        int twoCount = 0;
        int threeCount = 0;
        int fourCount = 0;
        int fiveCount = 0;
        int sixCount = 0;
        int sevenCount = 0;
        int eightCount = 0;
        int nineCount = 0;
        
        for (int k = 0; k < 9; k++){
            potentialNumber++;
            
            for (int j = 0; j < 9; j++){
                for (int i = 0; i < 9; i++){
                    if (theGrid[j][i].getRow() == row){
                        if ((theGrid[j][i].badList.contains(potentialNumber))){
                            switch(potentialNumber){
                                case 1:
                                    oneCount++;
                                        break;
                                case 2:
                                    twoCount++;
                                        break;
                                case 3:
                                    threeCount++;
                                        break;
                                case 4:
                                    fourCount++;
                                        break;
                                case 5:
                                    fiveCount++;
                                        break;
                                case 6:
                                    sixCount++;
                                        break;
                                case 7:
                                    sevenCount++;
                                        break;
                                case 8:
                                    eightCount++;
                                        break;
                                case 9:
                                    nineCount++;
                                        break;
                                default:
                                        break;
                            }
                        }
                        else{
                           
                        }
                    }
                }
            }  
        }
        potentialNumber = 0;
        for (int k = 0; k < 9; k++){
            potentialNumber++;
            
            for (int j = 0; j < 9; j++){
                for (int i = 0; i < 9; i++){
                    //System.out.println("BadList: " + j + ", " + i + ": " + theGrid[j][i].badList);
                                    
                    if (theGrid[j][i].getRow() == row){
                        if (!(theGrid[j][i].badList.contains(potentialNumber))){
                             switch(potentialNumber){
                                case 1:
                                    if (oneCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 2:
                                    if (twoCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 3:
                                    if (threeCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 4:
                                    if (fourCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 5:
                                    if (fiveCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 6:
                                    if (sixCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 7:
                                    if (sevenCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 8:
                                    if (eightCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 9:
                                    if (nineCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                default:
                                        break;
                            }
                        }
                        
                    }
                }
            }
        }    
    }
    
    //Method that will add a number to the grid if it is the only value that doesn't appears in the nodes badList when it appears in the rest of the columns badLists
    public void addBadColumnNumber(int column){
        int potentialNumber = 0;
        int oneCount = 0;
        int twoCount = 0;
        int threeCount = 0;
        int fourCount = 0;
        int fiveCount = 0;
        int sixCount = 0;
        int sevenCount = 0;
        int eightCount = 0;
        int nineCount = 0;

        for (int k = 0; k < 9; k++){
            potentialNumber++;
            
            for (int j = 0; j < 9; j++){
                for (int i = 0; i < 9; i++){
                    if (theGrid[j][i].getColumn() == column){
                        if ((theGrid[j][i].badList.contains(potentialNumber))){
                            switch(potentialNumber){
                                case 1:
                                    oneCount++;
                                        break;
                                case 2:
                                    twoCount++;
                                        break;
                                case 3:
                                    threeCount++;
                                        break;
                                case 4:
                                    fourCount++;
                                        break;
                                case 5:
                                    fiveCount++;
                                        break;
                                case 6:
                                    sixCount++;
                                        break;
                                case 7:
                                    sevenCount++;
                                        break;
                                case 8:
                                    eightCount++;
                                        break;
                                case 9:
                                    nineCount++;
                                        break;
                                default:
                                        break;
                            }
                        }
                        else{
                           
                        }
                    }
                }
            }  
        }
        potentialNumber = 0;
        for (int k = 0; k < 9; k++){
            potentialNumber++;
            
            for (int j = 0; j < 9; j++){
                for (int i = 0; i < 9; i++){
                    //System.out.println("BadList: " + j + ", " + i + ": " + theGrid[j][i].badList);
                                    
                    if (theGrid[j][i].getColumn() == column){
                        if (!(theGrid[j][i].badList.contains(potentialNumber))){
                             switch(potentialNumber){
                                case 1:
                                    if (oneCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 2:
                                    if (twoCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 3:
                                    if (threeCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 4:
                                    if (fourCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 5:
                                    if (fiveCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 6:
                                    if (sixCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 7:
                                    if (sevenCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 8:
                                    if (eightCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                case 9:
                                    if (nineCount == 8){
                                        if (!((checkVal(0, theGrid[j][i].getNonet(), potentialNumber)) || (checkVal(1, theGrid[j][i].getRow(), potentialNumber)) || (checkVal(2, theGrid[j][i].getColumn(), potentialNumber)))){
                                            if (!(numberAdded)){
                                                theGrid[j][i].setNum(potentialNumber);
                                                theGrid[j][i].badList.add(potentialNumber);
                                                setGrid();
                                                numberAdded = true;
                                            }
                                        }
                                    }
                                        break;
                                default:
                                        break;
                            }
                        }
                        
                    }
                }
            }
        }    
    }
    
    //Clears every nodes goodList
    public void clearGoodList(){
        for (int j = 0; j < 9; j++){
            
            for (int i = 0; i < 9; i++){
                theGrid[j][i].goodList = new ArrayList<>();
                //theGrid[j][i].badList = new ArrayList<Integer>();
            }
        }
    }
    
    //Would sort out what the next accurate number would be for the sudoku problem, Old Algorithm, based on goodLists
    public void nextNumber(int type){
        int potentialNumber = 0;
        int nonet = 0;
        int row = 0;
        int column = 0;
        switch(type){
            case 0:
               nonet = chooseNonet(); // must loop after a node has been checked completely
            
                for (int k = 0; k < 9; k++){
                    potentialNumber++;
                    
                    if (!(checkVal(0, nonet, potentialNumber))){
                        for (int j = 0; j < 9; j++){
                            for (int i = 0; i < 9; i++){
                                if (theGrid[j][i].getNonet() == nonet){
                                    
                                    if (theGrid[j][i].getNum() == 0){
                                        if (!(checkVal(1,theGrid[j][i].getRow(), potentialNumber))){
                                            if (!(checkVal(2,theGrid[j][i].getColumn(), potentialNumber))){
                                                //System.out.println("Inserting # " + potentialNumber + " into [" + j + ", " + i + "]");
                                                
                                                if (theGrid[j][i].goodList.contains(potentialNumber)){
                                                    
                                                }
                                                else{
                                                    theGrid[j][i].goodList.add(potentialNumber);
                                                    //System.out.println("goodList: " + j + ", " + i + " -- " + theGrid[j][i].goodList);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } 
                }
               
               
                break;
            case 1:
                row = chooseRow(); // must loop after a node has been checked completely
            
               for (int k = 0; k < 9; k++){
                    potentialNumber++;
                    
                   if (!(checkVal(1, row, potentialNumber))){
                        for (int j = 0; j < 9; j++){
                            for (int i = 0; i < 9; i++){
                                if (theGrid[j][i].getRow() == row){
                                    
                                    if (theGrid[j][i].getNum() == 0){
                                        if (!(checkVal(0,theGrid[j][i].getNonet(), potentialNumber))){
                                            if (!(checkVal(2,theGrid[j][i].getColumn(), potentialNumber))){
                                                //System.out.println("Inserting # " + potentialNumber + " into [" + j + ", " + i + "]");
                                                
                                                if (theGrid[j][i].goodList.contains(potentialNumber)){
                                                    
                                                }
                                                else{
                                                    theGrid[j][i].goodList.add(potentialNumber);
                                                    //System.out.println("goodList: " + j + ", " + i + " -- " + theGrid[j][i].goodList);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            case 2:
                column = chooseColumn(); // must loop after a node has been checked completely
            
               for (int k = 0; k < 9; k++){
                    potentialNumber++;
                    
                   if (!(checkVal(2, column, potentialNumber))){
                        for (int j = 0; j < 9; j++){
                            for (int i = 0; i < 9; i++){
                                if (theGrid[j][i].getColumn() == column){
                                    
                                    if (theGrid[j][i].getNum() == 0){
                                        if (!(checkVal(0,theGrid[j][i].getNonet(), potentialNumber))){
                                            if (!(checkVal(1,theGrid[j][i].getRow(), potentialNumber))){
                                                //System.out.println("Inserting # " + potentialNumber + " into [" + j + ", " + i + "]");
                                                
                                                if (theGrid[j][i].goodList.contains(potentialNumber)){
                                                    
                                                }
                                                else{
                                                    theGrid[j][i].goodList.add(potentialNumber);
                                                    //System.out.println("goodList: " + j + ", " + i + " -- " + theGrid[j][i].goodList);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
    
    //Would sort out what the next accurate number would be for the sudoku problem, Newer Algorithm, based on badLists
    public void newNextNumber(int type){
        int potentialNumber = 0;
        int nonet = 0;
        int row = 0;
        int column = 0;
        switch(type){
            case 0:
               nonet = chooseNonet(); // must loop after a node has been checked completely
            
               for (int k = 0; k < 9; k++){
                    potentialNumber++;
                    
                   if (!(checkVal(0, nonet, potentialNumber))){
                        for (int j = 0; j < 9; j++){
                            for (int i = 0; i < 9; i++){
                                if (theGrid[j][i].getNonet() == nonet){
                                    
                                    if (theGrid[j][i].getNum() == 0){
                                        if (!(checkVal(1,theGrid[j][i].getRow(), potentialNumber))){
                                            if (!(checkVal(2,theGrid[j][i].getColumn(), potentialNumber))){
                                                //System.out.println("Inserting # " + potentialNumber + " into [" + j + ", " + i + "]");
                                                
                                                if (theGrid[j][i].goodList.contains(potentialNumber)){
                                                    
                                                }
                                                else{
                                                    theGrid[j][i].goodList.add(potentialNumber);
                                                    //System.out.println("goodList: " + j + ", " + i + " -- " + theGrid[j][i].goodList);
                                                }
                                            }
                                            else{
                                                if (!(theGrid[j][i].badList.contains(potentialNumber))){
                                                    theGrid[j][i].badList.add(potentialNumber);
                                                }
                                            }
                                        }
                                        else{
                                            if (!(theGrid[j][i].badList.contains(potentialNumber))){
                                                theGrid[j][i].badList.add(potentialNumber);
                                            }
                                        }
                                    }
                                    else{
                                        for (int w = 1; w < 10; w++){
                                            if (!(theGrid[j][i].badList.contains(w))){
                                                theGrid[j][i].badList.add(w);
                                            }
                                        }
                                    }
                                    
                                }
                            }
                        }
                    }
                    else{
                        for (int j = 0; j < 9; j++){
                            for (int i = 0; i < 9; i++){
                                if (theGrid[j][i].getNonet() == nonet){
                                    if (!(theGrid[j][i].badList.contains(potentialNumber))){
                                        theGrid[j][i].badList.add(potentialNumber);
                                    }
                                }
                            }
                        }
                   }
                }
                
                
                break;
            case 1:
                row = chooseRow(); // must loop after a node has been checked completely
            
               for (int k = 0; k < 9; k++){
                    potentialNumber++;
                    
                   if (!(checkVal(1, row, potentialNumber))){
                        for (int j = 0; j < 9; j++){
                            for (int i = 0; i < 9; i++){
                                if (theGrid[j][i].getRow() == row){
                                    
                                    if (theGrid[j][i].getNum() == 0){
                                        if (!(checkVal(0,theGrid[j][i].getNonet(), potentialNumber))){
                                            if (!(checkVal(2,theGrid[j][i].getColumn(), potentialNumber))){
                                                //System.out.println("Inserting # " + potentialNumber + " into [" + j + ", " + i + "]");
                                                
                                                if (theGrid[j][i].goodList.contains(potentialNumber)){
                                                    
                                                }
                                                else{
                                                    theGrid[j][i].goodList.add(potentialNumber);
                                                    //System.out.println("goodList: " + j + ", " + i + " -- " + theGrid[j][i].goodList);
                                                }
                                            }
                                            else{
                                                if (!(theGrid[j][i].badList.contains(potentialNumber))){
                                                    theGrid[j][i].badList.add(potentialNumber);
                                                }
                                            }
                                        }
                                        else{
                                            if (!(theGrid[j][i].badList.contains(potentialNumber))){
                                                theGrid[j][i].badList.add(potentialNumber);
                                            }
                                        }
                                    }
                                    else{
                                        for (int w = 1; w < 10; w++){
                                            if (!(theGrid[j][i].badList.contains(w))){
                                                theGrid[j][i].badList.add(w);
                                            }
                                        }
                                    }
                                    
                                }
                            }
                        }
                    }
                    else{
                        for (int j = 0; j < 9; j++){
                            for (int i = 0; i < 9; i++){
                                if (theGrid[j][i].getRow() == row){
                                    if (!(theGrid[j][i].badList.contains(potentialNumber))){
                                        theGrid[j][i].badList.add(potentialNumber);
                                    }
                                }
                            }
                        }
                   }
                }
                addBadRowNumber(row);
                
                break;
            case 2:
                column = chooseColumn(); // must loop after a node has been checked completely
            
               for (int k = 0; k < 9; k++){
                    potentialNumber++;
                    
                   if (!(checkVal(2, column, potentialNumber))){
                        for (int j = 0; j < 9; j++){
                            for (int i = 0; i < 9; i++){
                                if (theGrid[j][i].getColumn() == column){
                                    
                                    if (theGrid[j][i].getNum() == 0){
                                        if (!(checkVal(0,theGrid[j][i].getNonet(), potentialNumber))){
                                            if (!(checkVal(1,theGrid[j][i].getRow(), potentialNumber))){
                                                //System.out.println("Inserting # " + potentialNumber + " into [" + j + ", " + i + "]");
                                                
                                                if (theGrid[j][i].goodList.contains(potentialNumber)){
                                                    
                                                }
                                                else{
                                                    theGrid[j][i].goodList.add(potentialNumber);
                                                    //System.out.println("goodList: " + j + ", " + i + " -- " + theGrid[j][i].goodList);
                                                }
                                            }
                                            else{
                                                if (!(theGrid[j][i].badList.contains(potentialNumber))){
                                                    theGrid[j][i].badList.add(potentialNumber);
                                                }
                                            }
                                        }
                                        else{
                                            if (!(theGrid[j][i].badList.contains(potentialNumber))){
                                                theGrid[j][i].badList.add(potentialNumber);
                                            }
                                        }
                                    }
                                    else{
                                        for (int w = 1; w < 10; w++){
                                            if (!(theGrid[j][i].badList.contains(w))){
                                                theGrid[j][i].badList.add(w);
                                            }
                                        }
                                    }
                                    
                                }
                            }
                        }
                    }
                    else{
                        for (int j = 0; j < 9; j++){
                            for (int i = 0; i < 9; i++){
                                if (theGrid[j][i].getColumn() == column){
                                    if (!(theGrid[j][i].badList.contains(potentialNumber))){
                                        theGrid[j][i].badList.add(potentialNumber);
                                    }
                                }
                            }
                        }
                   }
                }
                addBadColumnNumber(column);
                
                break;
            default:
                break;
        }
    }
    
    //Sets theGrid to the JavaFx GridPane Labels
    private void setGrid() {
        int i = 0;
        int j = 0;
        for (Node node : gPane.getChildren()) {
            //System.out.println("Id: " + node.getId());
            if (node instanceof Label) {
                // clear
                if (!((theGrid[j][i].getNum()) == 0)){
                    ((Label)node).setText(Integer.toString(theGrid[j][i].getNum()));
                    if (firstRun){
                        
                        switch(theGrid[j][i].color){
                            case 0:
                                ((Label)node).setTextFill(Color.web("#000000"));
                                break;
                            case 1: //red
                                ((Label)node).setTextFill(Color.web("#e80b0b"));
                                break;
                            case 2: //green
                                ((Label)node).setTextFill(Color.web("#2c721b"));
                                break;
                        }
                    }
                }
                else{
                    ((Label)node).setText(" ");
                    //((Label)node).setTextFill(Color.web("#008000"));
                    ((Label)node).setTextFill(Color.web("#0f8484"));
                    
                    
                }
                //System.out.println("("+j+", "+i+")");
                //return ((Label)node);
                if (i < 8){
                    i++;
                }
                else{
                    i = 0;
                    j++;
                } 
            }
        }
        firstRun = false;
    }
    
    //This class holds each individual nodes num, row, column, goodList, badList, color
    public class GridNode{
        private int num; // or starting number
        private int row; //0 to 8
        private int column;// 0 to 8
        private int nonet; // 0 to 8
        public ArrayList<Integer> goodList = new ArrayList<>();
        public ArrayList<Integer> badList = new ArrayList<>();
        public int color = 0;
     
        public GridNode(int row, int column, int nonet, int num){
            this.row = row;
            this.column = column;
            this.nonet = nonet;
            this.num = num;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }

        public int getNonet() {
            return nonet;
        }

        public void setNonet(int nonet) {
            this.nonet = nonet;
        }

        public ArrayList<Integer> getGoodList() {
            return goodList;
        }

        public void setGoodList(ArrayList<Integer> goodList) {
            this.goodList = goodList;
        }


        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }    
    }  
}

        
    

