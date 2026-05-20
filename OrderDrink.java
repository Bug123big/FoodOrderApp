public class OrderDrink extends OrderFood {
    private int size;
    public OrderDrink(String name, double price, int amount, int size) {
        this(name, price, amount, size, false);
    }
    public OrderDrink(String name, double price, int amount, int size, boolean limitedSpecial) {
        super(name, price, amount, "drink", limitedSpecial);
        setSize(size);
    }
    public void setSize(int size) {
        if (size == 0 || size == 1 || size == 2) {
            this.size = size;
        } else {
            System.err.println("Invalid drink size");
        }
    }
    public int getSize() {
        return size;
    }
    
    @Override
    public double getPrice() {
        switch (size) {
            case 0:
                return price;
            case 1:
                return price + 1.5;
            case 2:
                return price + 3.0;
            default:
                return price;
        }
    }
}