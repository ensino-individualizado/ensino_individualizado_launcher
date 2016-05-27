package model;

/**
 * Created by Gustavo Freitas on 26/05/2016.
 */
public class RemoteApplicationInfo implements Comparable<RemoteApplicationInfo>{
    private String jar_name = "";
    private String name_url = "";
    private String last_url = "";
    private transient String version  = "";

    public String getJar_name() {
        return jar_name;
    }

    public void setJar_name(String jar_name) {
        this.jar_name = jar_name;
    }

    public String getName_url() {
        return name_url;
    }

    public void setName_url(String name_url) {
        this.name_url = name_url;
    }

    public String getLast_url() {
        return last_url;
    }

    public void setLast_url(String info_url) {
        this.last_url = info_url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String toString(){
        return ("jar_name:"+this.jar_name+"\n" +
                "name_url:"+this.name_url+"\n" +
                "last_url:"+this.last_url+"\n");
    }

    @Override
    public int compareTo(RemoteApplicationInfo o) {
        return (this.jar_name.compareTo(o.getJar_name()));
    }
}
