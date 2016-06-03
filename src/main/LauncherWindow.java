/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import Visao.Administracao.AdministracaoController;
import Visao.utils.AlertUtils;
import control.Controller;
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
                Controller.getInstance().startApplication((AdministracaoController)this.getRegionController());
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtils.getInstance().showException("Erro ao obter arquivo.",
                        "Ocorreu um erro ao tentar atualizar a aplicação.\n" +
                        "Informe o Administrador sobre esse problema.", e);
                Platform.exit();
            }
            finally {
                try {
                    Runtime.getRuntime().exec(Controller.getInstance().getConfig().launch_prefix + " " + LauncherWindow.launchAtEnd + " " + Controller.getInstance().getConfig().application_arguments);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Platform.exit();
        }).start();
    }

    @Override
    public void stop(){
        System.out.println("Aplicação finalizada.");
    }
}
