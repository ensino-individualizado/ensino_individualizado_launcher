package model;

/**
 * Created by Gustavo Freitas on 26/05/2016.
 */
public class RemoteApplicationInfo implements Comparable<RemoteApplicationInfo>{
    private String file_name = "";
    private String version_url = "";
    private String download_url = "";
    private transient String version  = "";

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getVersion_url() {
        return version_url;
    }

    public void setVersion_url(String version_url) {
        this.version_url = version_url;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String info_url) {
        this.download_url = info_url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String toString(){
        return ("file_name:"+this.file_name+"\n" +
                "version_url:"+this.version_url+"\n" +
                "download_url:"+this.download_url+"\n");
    }

    @Override
    public int compareTo(RemoteApplicationInfo o) {
        return (this.file_name.compareTo(o.getFile_name()));
    }
}
