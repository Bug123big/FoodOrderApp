public class FoodStorage {
    private String name;
    private double price;
    private String imagePath;
    private String type;
    private boolean special;

    public FoodStorage(String name, double price, String imagePath, String type, boolean special) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.type = type;
        this.special = special;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getType() {
        return type;
    }

    public boolean isSpecial() {
        return special;
    }
}
