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
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 *
 * @author Gustavo Freitas
 */
public class FileManager {

    public static final String configurationFile = "file:///" + System.getProperty("user.dir") + "/ensino_individualizado_launcher_config.json";

    private final int BUFFER_LENGTH = 2048;
    
    private static FileManager instance = new FileManager();
    
    public static FileManager getInstance(){
        return (FileManager.instance);
    }

    public String load(String location) throws IOException {

        String line;
        StringBuilder json = new StringBuilder();

        BufferedReader br;

        try {
            URL url = new URL(location);
            br = new BufferedReader(new InputStreamReader(url.openStream()));
        }
        catch(MalformedURLException e){
            br = new BufferedReader(new FileReader(location));
        }

        while( (line = br.readLine()) != null){
            json.append(line);
        }

        br.close();

        return (json.toString());
    }

    public <T> T loadJsonAndParse(String filePath, Type typeOf) throws IOException {
        String json = this.load(filePath);
        return (this.parseFromJson(json, typeOf));
    }

    public void saveToJson(String filePath, Object obj, Type typeOfObj) throws IOException, URISyntaxException {
        String str = this.parseToJson(obj, typeOfObj);
        InputStream is = new ByteArrayInputStream(Charset.forName("UTF-8").encode(str).array());
        this.save(is, filePath);
    }

    public void save(String inputPath, String outputPath) throws IOException, URISyntaxException {
        BufferedInputStream inputStream = new BufferedInputStream(new URL(inputPath).openStream());
        this.save(inputStream, outputPath, BUFFER_LENGTH);
    }

    public void save(InputStream input, String outputPath) throws IOException, URISyntaxException {
        this.save(input, outputPath, BUFFER_LENGTH);
    }

    public void save(InputStream input, String outputPath, int bufferLength) throws IOException, URISyntaxException {
        FileOutputStream fos = null;
        BufferedInputStream is = new BufferedInputStream(input);

        try {
            URL url = new URL(outputPath);
            fos = new FileOutputStream(new File(url.toURI()));
        }
        catch (URISyntaxException | MalformedURLException e){
            fos = new FileOutputStream(new File(outputPath));
        }

        int r;
        byte[] buffer = new byte[bufferLength];
        while((r = is.read(buffer))!=-1) {
            fos.write(buffer, 0, r);
        }

        fos.flush();
        fos.close();
        is.close();
    }

    public <T> T parseFromJson(String json, Type typeOf){

        T obj = null;

        try{
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new StringReader(json));
            reader.setLenient(true);
            obj = gson.fromJson(reader, typeOf);
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        return (obj);
    }

    public String parseToJson(Object obj, Type typeOf){
        Gson gson = new Gson();
        return (gson.toJson(obj, typeOf));
    }
}
