package design_pattern.factory_pattern;

import java.util.ArrayList;

public abstract class Pizza {
    String name;
    String dough;
    String sauce;
    ArrayList toppings = new ArrayList();
   public  void prepare(){
       System.out.printf("Preparing" + name);
       System.out.println("Addtoppings: " );
       for (int i = 0; i <toppings.size() ; i++) {
           System.out.println(" "+toppings.get(i));
       }
   }
    public  void bake(){
        System.out.println("bake for 25 mins");
    }
    public  void cut(){
        System.out.println("Cutting the pizza");
    }
    public  void box(){
        System.out.println("Place pizza in box");
    }
}
