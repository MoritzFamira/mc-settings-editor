package at.moritz.datamodel;

public class DoubleSetting extends Setting{
    public double value;
    //public double min;
    //public double max;

    public DoubleSetting(String key, double value) {
        super(key);
        setValue(value);
    }

    @Override
    public String getKey() {
        return super.key;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public double getValue() {
        return value;
    }
}
