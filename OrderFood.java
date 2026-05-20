public class OrderFood {
    protected String name;
    protected double price;
    private int amount;
    private String type;
    private boolean limitedSpecial;

    public OrderFood(String name, double price, int amount, String type) {
        this(name, price, amount, type, false);
    }

    public OrderFood(String name, double price, int amount, String type, boolean limitedSpecial) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.type = type;
        this.limitedSpecial = limitedSpecial;
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

    public boolean isLimitedSpecial() {
        return limitedSpecial;
    }
}
