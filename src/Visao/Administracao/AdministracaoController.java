/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Visao.Administracao;

import Visao.ControllerHierarchy.RegionController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Gustavo Freitas
 */
public class AdministracaoController extends RegionController{


    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label infoString;

    /** 
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.infoString.setText("Iniciando...");
        this.progressBar.setProgress(0.0);
    }

    public Double getProgress(){
        return (this.progressBar.getProgress());
    }

    public void setProgress(Double progress) {
        this.progressBar.setProgress(progress);
    }

    public void setInfoString(String infoString) {
        this.infoString.setText(infoString);
    }
}
