public class OrderEntree extends OrderFood {
    private boolean childrenFood;
    public OrderEntree(String name, double price, int amount, boolean childrenFood) {
        this(name, price, amount, childrenFood, false);
    }
    public OrderEntree(String name, double price, int amount, boolean childrenFood, boolean limitedSpecial) {
        super(name, price, amount, "entree", limitedSpecial);
        this.childrenFood = childrenFood;
    }
    public void setChildrenFood(boolean childrenFood) {
        this.childrenFood = childrenFood;
    }
    public boolean getChildrenFood() {
        return childrenFood;
    }
    
    @Override
    public double getPrice() {
        if (childrenFood) {
            return price * 0.65;
        }
        return price;
    }
}
