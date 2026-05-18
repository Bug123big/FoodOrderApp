import java.util.ArrayList;

public class Combo{
    private ArrayList<OrderFood> comboFood = new ArrayList<>();

    public Combo(ArrayList <OrderFood> comboFood) {
        if (validCombo(comboFood)) {
            this.comboFood = comboFood;
        } else {
            throw new IllegalArgumentException("Invalid combo: entrees, desserts, and drinks must have the same amount.");
        }
    }
    public boolean validCombo(ArrayList<OrderFood> comboFood) {
    int countEntrees = 0;
    int countDesserts = 0;
    int countDrinks = 0;

    for (OrderFood f : comboFood) {
        if (
            f instanceof OrderEntree
            || f.getType().equals("adult entree")
            || f.getType().equals("child entree")
            || f.getType().equals("entree")
        ) {
            countEntrees++;
        } else if (
            f instanceof OrderDrink
            || f.getType().equals("drink")
        ) {
            countDrinks++;
        } else if (
            f.getType().equals("dessert")
        ) {
            countDesserts++;
        }
    }

    return countEntrees == countDesserts
            && countEntrees == countDrinks
            && countEntrees > 0;
}
    public double comboDiscount() {
    int count = 0;

    for (OrderFood f : comboFood) {
        if (f instanceof OrderEntree|| f.getType().equals("adult entree")|| f.getType().equals("child entree")|| f.getType().equals("entree")) {
            count++;
        }
    }
    if (count <= 0) {
        return 1.0;
    }
    if (count > 10) {
        return 0.65;
    }
    return 1 - (0.0037037 * (count - 1) * (count - 1) + 0.05);
}
    public ArrayList<OrderFood> getFoods() {
        return comboFood;
    }
    public double getOriginalComboPrice(){
        double originalPrice = 0.0;
        for (OrderFood f: comboFood){
            originalPrice += f.getPrice();
        }
        return originalPrice;
    }
    public double getFinalComboPrice(){
        return getOriginalComboPrice() * comboDiscount();
    }
}
