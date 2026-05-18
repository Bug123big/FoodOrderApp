import java.util.ArrayList;

public class Menu {
    private ArrayList<FoodStorage> storageFood = new ArrayList<>();

    public Menu() {
        storageFood.add(new FoodStorage("Braised beef noodles", 12.99, "Image/BraisedBeefNoodlesImages.png", "entree"));
        storageFood.add(new FoodStorage("Fried rice", 9.99, "Image/FriedRice.png", "entree"));
        storageFood.add(new FoodStorage("Paomo", 11.99, "Image/Paomo.png", "entree"));
        storageFood.add(new FoodStorage("Rou jia mo", 6.99, "Image/Roujiamo.png", "entree"));
        storageFood.add(new FoodStorage("Tianjin-style jianbing", 6.99, "Image/Jianbing.png", "entree"));
        storageFood.add(new FoodStorage("Malatang", 10.99, "Image/Malatang.png", "entree"));
        
        

        storageFood.add(new FoodStorage("Fried chop rice cake", 1.99, "Image/FriedChopRiceCake.png", "dessert"));
        storageFood.add(new FoodStorage("Mooncake", 2.99, "Image/Mooncake.png", "dessert"));
        storageFood.add(new FoodStorage("Misandao", 0.99, "Image/Misandao.png", "dessert"));
        storageFood.add(new FoodStorage("Tanghulu", 5.99, "Image/Tanghulu.png", "dessert"));
        storageFood.add(new FoodStorage("Zongzi", 6.99, "Image/Zongzi.png", "dessert"));
        storageFood.add(new FoodStorage("Sesame Ball", 2.99, "Image/Matuan.png", "dessert"));
        storageFood.add(new FoodStorage("Osmanthus cake", 2.99, "Image/OsmanthusCake.png", "dessert"));
        storageFood.add(new FoodStorage("Chinese flower cake", 2.99, "Image/FlowerCake.png", "dessert"));
        storageFood.add(new FoodStorage("Aiwowo", 1.99, "Image/Aiwowo.png", "dessert"));

        storageFood.add(new FoodStorage("Soy milk ", 2.59, "Image/Doujiang.png", "drink"));
        storageFood.add(new FoodStorage("Sweet Pear", 5.99, "Image/SweetPear.png", "drink"));
        storageFood.add(new FoodStorage("Black tea", 3.49, "Image/BlakcTea.png", "drink"));
        storageFood.add(new FoodStorage("Suanmeitang", 3.49, "Image/Suanmeitang.png", "drink"));
        storageFood.add(new FoodStorage("milk tea", 3.49, "Image/Milktea.png", "drink"));
        storageFood.add(new FoodStorage("Coke", 1.99, "Image/cola.png", "drink"));
        

    }

    public ArrayList<FoodStorage> getItems() {
        return storageFood;
    }

    public ArrayList<FoodStorage> getItemsByType(String type) {
        ArrayList<FoodStorage> result = new ArrayList<>();

        for (FoodStorage item : storageFood) {
            if (item.getType().equals(type)) {
                result.add(item);
            }
        }

        return result;
    }
}
