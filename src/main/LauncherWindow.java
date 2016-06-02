/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import Visao.Administracao.AdministracaoController;
import control.LauncherMainController;
import javafx.application.Platform;
import javafx.stage.StageStyle;
import Visao.ControllerHierarchy.WindowController;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 * @author Gustavo Freitas
 */
public class LauncherWindow extends WindowController{

    private static String launchAtEnd;
    LauncherMainController launcherMainController = null;

    public static void main(String[] args) throws Exception {

        if(args.length == 0){
            throw new Exception("Aplicação a ser lançada não foi indicada.");
        }

        launchAtEnd = args[0];
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        super.start(primaryStage, "/Visao/Administracao/AdministracaoFXML.fxml", "Administração");

        getStage().getIcons().clear();
        getStage().setResizable(false);
        getStage().initStyle(StageStyle.UNDECORATED);
        super.show();
        new Thread(() -> {
            try {
                LauncherMainController.getInstance().startApplication((AdministracaoController)this.getRegionController());
            } catch (Exception e) {
                e.printStackTrace();
                Platform.exit();
            }
            finally {
                try {
                    Runtime.getRuntime().exec("java -jar " + LauncherWindow.launchAtEnd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Platform.exit();
            }
        }).start();
    }

    @Override
    public void stop(){
        System.out.println("Aplicação finalizada.");
    }
}
