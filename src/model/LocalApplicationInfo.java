package model;

/**
 * Created by Gustavo Freitas on 26/05/2016.
 */
public class LocalApplicationInfo implements Comparable<LocalApplicationInfo>{

    private String jar_name = "";
    private String jar_version = "";
    private transient boolean need_update = false;

    public String getJar_name() {
        return jar_name;
    }

    public void setJar_name(String jar_name) {
        this.jar_name = jar_name;
    }

    public String getJar_version() {
        return jar_version;
    }

    public void setJar_version(String jar_version) {
        this.jar_version = jar_version;
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
        return jar_name.equals(that.jar_name);
    }

    @Override
    public int hashCode() {
        return jar_name.hashCode();
    }

    @Override
    public int compareTo(LocalApplicationInfo o) {
        return (this.jar_name.compareTo(o.getJar_name()));
    }

    public String toString(){
        return ("jar_name:"+this.jar_name+",\n" +
                "jar_version:"+this.jar_version+"\n");
    }
}
