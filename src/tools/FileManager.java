/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;

/**
 *
 * @author Gustavo Freitas
 */
public class FileManager {

    public static final String localApplicationInfoFile = "local_application_info.json";
    public static final String remoteApplicationRessources = "remote_application_ressources.json";

    private final int BUFFER_LENGTH = 2048;
    
    private static FileManager instance = new FileManager();
    
    public static FileManager getInstance(){
        return (FileManager.instance);
    }

    public String load(String filePath) throws IOException {

        if(!this.verifExistence(filePath)){
            return (null);
        }

        BufferedReader br = new BufferedReader(new FileReader(filePath));

        String line = "";
        StringBuilder json = new StringBuilder();

        while( (line = br.readLine()) != null){
            json.append(line);
        }

        br.close();

        return (json.toString());
    }

    public <T> T loadJsonAndParse(String filePath, Type typeOf) throws IOException {
        String json = this.load(filePath);
        if(json != null) {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new StringReader(json));
            reader.setLenient(true);
            return (gson.fromJson(reader, typeOf));
        }
        else{
            return (null);
        }
    }

    public void save(String inputPath, String outputPath) throws IOException {
        BufferedInputStream in = null;

        BufferedInputStream inputStream = new BufferedInputStream(new URL(inputPath).openStream());
        this.save(inputStream, outputPath, BUFFER_LENGTH);
    }

    public void save(InputStream input, String outputPath) throws IOException {
        this.save(input, outputPath, BUFFER_LENGTH);
    }

    public void save(InputStream input, String outputPath, int bufferLength) throws FileNotFoundException, IOException{

        BufferedInputStream is = new BufferedInputStream(input);
        FileOutputStream fos = new FileOutputStream(new File(outputPath));

        byte[] buffer = new byte[bufferLength];
        int r = 0;
        while((r = is.read(buffer))!=-1) {
            fos.write(buffer, 0, r);
        }
        fos.flush();
        fos.close();
        is.close();
    }

    public boolean verifExistence(String filePath) {
        return (new File(filePath).exists());
    }
}
