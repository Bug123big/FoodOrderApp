public class FoodStorage {
    private String name;
        private double price;
        private String imagePath;
        private String type;

        public FoodStorage(String name, double price, String imagePath, String type) {
            this.name = name;
            this.price = price;
            this.imagePath = imagePath;
            this.type = type;
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
}

