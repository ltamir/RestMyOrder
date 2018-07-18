package ac.arial.liortamir.restmyorder.entity;

public class Dish {
    public static final String DB_DISH_ID = "dishId";
    public static final String DB_DISH_NAME = "dishName";
    public static final String DB_PRICE = "dishPrice";
    public static final String DB_DISH_TYPE_ID = "dishTypeId";

    private Integer dishId;
    private String dishName;
    private double dishPrice;
    private DishType dishType;

    public Dish(Integer dishId, String dishName, double dishPrice, DishType dishType) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.dishPrice = dishPrice;
        this.dishType = dishType;
    }

    public Integer getDishId() {
        return dishId;
    }

    public void setDishId(Integer dishId) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public double getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(double dishPrice) {
        this.dishPrice = dishPrice;
    }

    public DishType getDishType() {
        return dishType;
    }

    public void setDishType(DishType dishType) {
        this.dishType = dishType;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Dish))
            return false;

        Dish dish = (Dish)obj;
        return dish.getDishId() == getDishId();
    }

    @Override
    public String toString() {
        return getDishName() + " " + getDishPrice();
    }
}
