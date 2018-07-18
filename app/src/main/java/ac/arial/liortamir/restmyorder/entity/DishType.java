package ac.arial.liortamir.restmyorder.entity;

public class DishType {

    public static final String DB_DISH_TYPE_ID = "dishTypeId";
    public static final String DB_DISH_TYPE_NAME = "dishTypeName";

    private int dishTypeId;
    private String dishTypeName;

    public DishType(int dishTypeId, String dishTypeName) {

        this.dishTypeId = dishTypeId;
        this.dishTypeName = dishTypeName;
    }

    public int getDishTypeId() {
        return dishTypeId;
    }

    public void setDishTypeId(int dishTypeId) {
        this.dishTypeId = dishTypeId;
    }

    public String getDishTypeName() {
        return dishTypeName;
    }

    public void setDishTypeName(String dishTypeName) {
        this.dishTypeName = dishTypeName;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof DishType))
            return false;

        DishType dt = (DishType)obj;
        return dt.getDishTypeId() == getDishTypeId();
    }

    @Override
    public String toString() {
        return getDishTypeName();
    }
}
