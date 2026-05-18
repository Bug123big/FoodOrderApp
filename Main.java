public class Main {
    public static void main(String[] args){
        Order order = new Order();
        PanelTester.testPanel(new CoverPanel(order),"Welcome to the Food Order App");
    }
}
