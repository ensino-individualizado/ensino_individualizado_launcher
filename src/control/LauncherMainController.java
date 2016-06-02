/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import Visao.Administracao.AdministracaoController;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import model.LocalApplicationInfo;
import model.RemoteApplicationInfo;
import tools.FileManager;

import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Gustavo Freitas
 */
public class LauncherMainController {

    private final Updater updater = new Updater();
    private static LauncherMainController instance = new LauncherMainController();

    public void startApplication(AdministracaoController aplicacao) throws Exception {
        Platform.runLater(() ->{
            aplicacao.setInfoString("Carregando arquivos de configuração...");
        });

        HashMap<String, LocalApplicationInfo> localInfo =
                FileManager.getInstance().loadJsonAndParse(FileManager.localApplicationInfoFile, new TypeToken<HashMap<String, LocalApplicationInfo>>(){}.getType());

        HashMap<String, RemoteApplicationInfo> remoteInfo =
                FileManager.getInstance().loadJsonAndParse(FileManager.remoteApplicationRessources, new TypeToken<HashMap<String, RemoteApplicationInfo>>(){}.getType());

        if(remoteInfo == null){
            throw new Exception("Arquivo de configurações não encontrado.");
        }

        if(localInfo == null){
            localInfo = new HashMap<>();
        }

        Platform.runLater(() ->{
            aplicacao.setProgress(0.1);
            aplicacao.setInfoString("Verificando a necessidade de atualização...");
        });

        if(this.updater.isUpdateNecessary(localInfo, remoteInfo) == true){
            this.updateApplication(aplicacao, localInfo, remoteInfo);
            this.updater.saveLocalInfo(localInfo);
        }
        else{
            System.out.println("Aplicação atualizada. Iniciando...");
        }
    }

    public void updateApplication(AdministracaoController aplicacao, HashMap<String, LocalApplicationInfo> localInfo, HashMap<String, RemoteApplicationInfo> remoteInfo){

        Platform.runLater(() ->{
            aplicacao.setProgress(0.3);
            aplicacao.setInfoString("Baixando atualizações...");
        });

        Double step = new Double(0.7 / localInfo.keySet().size());

        for(LocalApplicationInfo info : localInfo.values()){
            Platform.runLater(() -> {
                aplicacao.setInfoString("Baixando " + info.getJar_name() + "...");
            });

            if(info.isNeed_update()){
                try {
                    this.updater.update(info, remoteInfo.get(info.getJar_name()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Platform.runLater(() ->{
                aplicacao.setProgress(aplicacao.getProgress() + step);
            });
        }
    }

    public static LauncherMainController getInstance() {
        return instance;
    }
}
