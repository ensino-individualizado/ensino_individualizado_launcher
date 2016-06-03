package control;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import model.LocalApplicationInfo;
import model.RemoteApplicationInfo;
import tools.FileManager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

/**
 * Created by Gustavo Freitas on 26/05/2016.
 */
public class Updater {

    public boolean isUpdateNecessary(HashMap<String, LocalApplicationInfo> localInfo, HashMap<String, RemoteApplicationInfo> remoteInfo){

        boolean is = false;

        for(String key : remoteInfo.keySet()){
            LocalApplicationInfo keyInfo = localInfo.get(key);
            if(keyInfo != null){
                if(this.needsUpdate(keyInfo, remoteInfo.get(key))){
                    keyInfo.setNeed_update(true);
                    is = true;
                }
                else{
                    keyInfo.setNeed_update(false);
                }
            }
            else{
                is = true;
                keyInfo = new LocalApplicationInfo();
                keyInfo.setFile_name(remoteInfo.get(key).getFile_name());
                keyInfo.setNeed_update(true);
                this.needsUpdate(keyInfo, remoteInfo.get(key));
                localInfo.put(key, keyInfo);
            }
        }
        return (is);
    }

    public boolean needsUpdate(LocalApplicationInfo local, RemoteApplicationInfo remote){
        Gson gson = new Gson();
        remote.setVersion(gson.fromJson(this.getRequest(remote.getVersion_url()), String.class));
        if(remote.getVersion() != null) {
            return (remote.getVersion().compareTo(local.getVersion()) == 0 ? false : true);
        }
        else{
            //Remote é nulo quando o servidor não foram lançadas releases
            return (false);
        }
    }

    public void update(LocalApplicationInfo local, RemoteApplicationInfo remote) throws IOException, URISyntaxException {
        String downloadURL = FileManager.getInstance().parseFromJson(this.getRequest(remote.getDownload_url()), new TypeToken<String>(){}.getType());
        FileManager.getInstance().save(downloadURL, local.getFile_name());
        local.setVersion(remote.getVersion());
    }

    private String getRequest(String url) {

        Client client = Client.create();

        WebResource webResource = client
                .resource(url);

        ClientResponse response = webResource.accept("application/json")
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        String serverResult = response.getEntity(String.class);

        client.destroy();

        return (serverResult);
    }

    public void saveLocalInfo(HashMap<String, LocalApplicationInfo> info) throws IOException, URISyntaxException {
        FileManager.getInstance().saveToJson(Controller.getInstance().getConfig().local_data_location, info, new TypeToken<HashMap<String, LocalApplicationInfo>>(){}.getType());
    }
}
