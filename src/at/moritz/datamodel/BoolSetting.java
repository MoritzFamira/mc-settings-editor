package at.moritz.datamodel;

public class BoolSetting extends Setting{
    public boolean value;

    public BoolSetting(String key,boolean value) {
        super(key);
        setValue(value);
    }

    @Override
    public String getKey() {
        return super.key;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
    public boolean getValue() {return this.value;}
}
