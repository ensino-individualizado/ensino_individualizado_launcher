package control;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import model.LocalApplicationInfo;
import model.RemoteApplicationInfo;
import tools.FileManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
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
                keyInfo.setJar_name(remoteInfo.get(key).getJar_name());
                keyInfo.setNeed_update(true);
                this.needsUpdate(keyInfo, remoteInfo.get(key));
                localInfo.put(key, keyInfo);
            }
        }
        return (is);
    }

    public boolean needsUpdate(LocalApplicationInfo local, RemoteApplicationInfo remote){
        Gson gson = new Gson();
        remote.setVersion(gson.fromJson(this.getRequest(remote.getName_url()), String.class));
        if(remote.getVersion() != null) {
            return (remote.getVersion().compareTo(local.getJar_version()) == 0 ? false : true);
        }
        else{
            //Remote é nulo quando o servidor não foram lançadas releases
            return (false);
        }
    }

    public void update(LocalApplicationInfo local, RemoteApplicationInfo remote) throws IOException {
        Gson gson = new Gson();
        String downloadURL = gson.fromJson(this.getRequest(remote.getLast_url()), String.class);
        FileManager.getInstance().save(downloadURL, local.getJar_name());
        local.setJar_version(remote.getVersion());
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

    public void saveLocalInfo(HashMap<String, LocalApplicationInfo> info) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, LocalApplicationInfo>>(){}.getType();
        String str = gson.toJson(info, type);
        InputStream is = new ByteArrayInputStream(Charset.forName("UTF-8").encode(str).array());
        FileManager.getInstance().save(is, FileManager.localApplicationInfoFile);
    }
}
