import java.util.ArrayList;

public class Order {
    private ArrayList<OrderFood> singleFoodList = new ArrayList<>();
    private ArrayList<Combo> comboList = new ArrayList<>();

    public Order() {}
    public void addSingleFood(OrderFood food) {
        singleFoodList.add(food);
    }
    public void addCombo(Combo combo) {
        comboList.add(combo);
    }
    public void deleteCombo(Combo combo) {
        comboList.remove(combo);
    }
    public void deleteSingleFood(OrderFood singleFood) {
        singleFoodList.remove(singleFood);
    }
    public ArrayList<OrderFood> getSingleFoods() {
        return singleFoodList;
    }
    public ArrayList<Combo> getCombos() {
        return comboList;
    }
    public double calculateOriginal() {
        double total = 0.0;
        for (OrderFood f : singleFoodList) {
            total += f.getPrice();
        }
        for (Combo c : comboList) {
            total += c.getOriginalComboPrice();
        }
        return total;
    }
    public double calculateFinal() {
        double total = 0.0;
        for (OrderFood f : singleFoodList) {
            total += f.getPrice();
        }
        for (Combo c : comboList) {
            total += c.getFinalComboPrice();
        }
        return total;
    }
    public void clearOrder() {
        singleFoodList.clear();
        comboList.clear();
    }
}