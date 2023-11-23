package com.mpcs.scratchpad.editor;

import com.mpcs.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class ShaderEditorController implements Initializable {
    @FXML
    private EngineCanvas mainEngineCanvas;

    @FXML
    private Pane wrapperPane;

    private double x;
    private double y;
    public void up(ActionEvent e) {
        moveCircle(0, 1);
    }

    public void left(ActionEvent e) {
        moveCircle(-1, 0);
    }
    public void right(ActionEvent e) {
        moveCircle(1, 0);
    }
    public void down(MouseEvent e) {
        //textArea.requestFocus();
    }

    private void moveCircle(double dx, double dy) {
        //myCircle.setCenterX(x+=dx);
        //myCircle.setCenterY(y+=dy);
        Logger.log("X: " + x + " Y: " + y);
    }


    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Logger.log("Controller init!");
        mainEngineCanvas.widthProperty().bind(wrapperPane.widthProperty());
        mainEngineCanvas.heightProperty().bind(wrapperPane.heightProperty());
    }
}