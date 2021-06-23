package at.moritz.datamodel;

public abstract class Setting {
    public String key;

    public Setting(String key) {
        this.key = key;
    }

    public abstract String getKey();
}
