/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.LocalApplicationInfo;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 *
 * @author Gustavo Freitas
 */
public class FileManager {

    public static final String configurationFile = "ensino_individualizado_launcher_config.json";

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
            return (this.parseJson(json, typeOf));
        }
        else{
            return (null);
        }
    }

    public void saveToJson(String filePath, Object obj, Type typeOfObj) throws IOException {
        Gson gson = new Gson();
        String str = gson.toJson(obj, typeOfObj);
        InputStream is = new ByteArrayInputStream(Charset.forName("UTF-8").encode(str).array());
        this.save(is, filePath);
    }

    public void save(String inputPath, String outputPath) throws IOException {
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

    public <T> T parseJson(String json, Type typeOf){
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);
        return (gson.fromJson(reader, typeOf));
    }

    public boolean verifExistence(String filePath) {
        return (new File(filePath).exists());
    }
}
