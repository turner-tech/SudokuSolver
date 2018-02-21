package sudokusolver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//*************************************************************
//Alexander Turner
//CS 4242: Assignment 0: Sudoku Solver
//Jan. 20th, 2017
//Program that attemps to solve Sudoku puzzles.
//*************************************************************


//This paticular file is responsible for launching the whole program/application. 
public class TheLauncher extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXML_SudokuSolver.fxml"));
        stage.setTitle("Sodoku Solver: The Solver");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
