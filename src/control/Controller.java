/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import Visao.Administracao.AdministracaoController;
import Visao.utils.AlertUtils;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import model.Config;
import model.LocalApplicationInfo;
import model.RemoteApplicationInfo;
import tools.FileManager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

/**
 *
 * @author Gustavo Freitas
 */
public class Controller {

    private Config config;
    private final Updater updater = new Updater();
    private static Controller instance = new Controller();
    private HashMap<String, LocalApplicationInfo> localInfo = null;
    private HashMap<String, RemoteApplicationInfo> remoteInfo = null;

    public void startApplication(AdministracaoController aplicacao) throws Exception {

        Platform.runLater(() ->{
            aplicacao.setInfoString("Carregando arquivos de configuração...");
        });

        this.config = FileManager.getInstance().loadJsonAndParse(FileManager.configurationFile, new TypeToken<Config>(){}.getType());

        this.loadLocalInfo();

        remoteInfo =
                FileManager.getInstance().loadJsonAndParse(config.remote_data_location, new TypeToken<HashMap<String, RemoteApplicationInfo>>(){}.getType());

        if(remoteInfo == null){
            throw new IOException("Arquivo de configurações não encontrado.");
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
                aplicacao.setInfoString("Baixando " + info.getFile_name() + "...");
            });

            if(info.isNeed_update()){
                try {
                    this.updater.update(info, remoteInfo.get(info.getFile_name()));
                } catch (IOException | URISyntaxException e) {
                    Platform.runLater(() -> {
                        AlertUtils.getInstance().showException("Erro ao obter arquivo.",
                                "Ocorreu um erro ao tentar baixar a última versão da aplicação \"" + info.getFile_name() + "\".\n" +
                                "A atualização irá continuar, mas um artefato desatualizado permanecerá localmente.", e);
                    });
                    e.printStackTrace();
                }
            }

            Platform.runLater(() ->{
                aplicacao.setProgress(aplicacao.getProgress() + step);
            });
        }
    }

    public void loadLocalInfo(){
        try {
            localInfo =
                    FileManager.getInstance().loadJsonAndParse(config.local_data_location, new TypeToken<HashMap<String, LocalApplicationInfo>>() {}.getType());
        }
        catch (IOException e){
            System.out.println("Não existem dados locais: " + e.getMessage());
        }
    }

    public Config getConfig() {
        return config;
    }

    public static Controller getInstance() {
        return instance;
    }
}
