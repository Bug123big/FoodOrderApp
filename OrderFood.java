public class OrderFood {
    protected String name;
    protected double price;
    private int amount;
    private String type;
    public OrderFood() {
    }
    public OrderFood(String name, double price, int amount, String type) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.type = type;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public int getAmount() {
        return amount;
    }
    public String getType() {
        return type;
    }
}
