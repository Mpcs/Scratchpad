package com.mpcs.scratchpad.editor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class ShaderEditorController implements Initializable {
    @FXML
    private EngineCanvas mainEngineCanvas;

    @FXML
    private Pane wrapperPane;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainEngineCanvas.widthProperty().bind(wrapperPane.widthProperty());
        mainEngineCanvas.heightProperty().bind(wrapperPane.heightProperty());
    }
}