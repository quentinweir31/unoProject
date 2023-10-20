public class Card {
    private String colour;
    private int value;
    private String type;

    public Card(String colour, int value, String type) {
        this.value = value;
        this.type = type;
    }

    public String toString() {
        return colour + " " + value + " with type " + type;
    }
}
