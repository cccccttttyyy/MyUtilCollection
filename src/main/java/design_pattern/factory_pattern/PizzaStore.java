package design_pattern.factory_pattern;

public abstract class PizzaStore {
    SimplePizzaFactory factory;
    public PizzaStore( SimplePizzaFactory factory){
        this.factory = factory;
    }

    /**
     * 不动的东西放在抽象类里声明为final 可变的东西抽象出去
     * @param type
     * @return
     */
    final Pizza orderPizza(String type){
        Pizza pizza = null;
        pizza = createPizza(type);
        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();
        return pizza;
    }

    /**
     * 这个方法相当于为上面方法提供实例的工厂
     * @param type
     * @return
     */
   abstract Pizza createPizza(String type);
}
