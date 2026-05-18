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
    public boolean validCombo(ArrayList <OrderFood> comboFood){
        int countEntrees = 0;
        int countDesserts = 0;
        int countDrinks = 0;
        for (OrderFood f: comboFood){
            if(f instanceof OrderEntree){
                countEntrees++;
            }
            else if(f instanceof OrderDrink){
                countDrinks++;
            }
            else{
                countDesserts++;
            }
        }
        return countEntrees == countDesserts && countEntrees == countDrinks;
    }
    public double comboDiscount(){
        int count = 0;
        for (OrderFood f : comboFood) {
            if (f instanceof OrderEntree) {
                count++;
            }
        }
        if (count > 10){
            return 0.65;
        }
        else{
            return (1 - (0.0037037 * (count -1)* (count -1)+0.05));
        }
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
