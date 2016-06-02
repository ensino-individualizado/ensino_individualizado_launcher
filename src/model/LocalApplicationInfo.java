package model;

/**
 * Created by Gustavo Freitas on 26/05/2016.
 */
public class LocalApplicationInfo implements Comparable<LocalApplicationInfo>{

    private String file_name = "";
    private String version = "";
    private transient boolean need_update = false;

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isNeed_update() {
        return need_update;
    }

    public void setNeed_update(boolean need_update) {
        this.need_update = need_update;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalApplicationInfo that = (LocalApplicationInfo) o;
        return file_name.equals(that.file_name);
    }

    @Override
    public int hashCode() {
        return file_name.hashCode();
    }

    @Override
    public int compareTo(LocalApplicationInfo o) {
        return (this.file_name.compareTo(o.getFile_name()));
    }

    public String toString(){
        return ("file_name: "+this.file_name+",\n" +
                "version: "+this.version+"\n");
    }
}
