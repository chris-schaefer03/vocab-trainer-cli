package trainer;

public class Vocab {
    private String deutsch;
    private String albanisch;

    public Vocab(String deutsch, String albanisch) {
        this.deutsch = deutsch;
        this.albanisch = albanisch;
    }

    public String getDeutsch() {
        return deutsch;
    }

    public String getAlbanisch() {
        return albanisch;
    }

    @Override
    public String toString() {
        return deutsch + " → " + albanisch;
    }
}
