package ac.arial.liortamir.restmyorder.entity;

public class OrderItem {

    public static final String DB_ORDER_ITEM_ID = "orderItemId";
    public static final String DB_ORDER_ID = "orderId";
    public static final String DB_WAITER_USER_ID = "waiterUserId";
    public static final String DB_CHEF_USER_ID = "chefUserId";
    public static final String DB_SIT_NUM = "sitNum";
    public static final String DB_DISH_ID = "dishId";
    public static final String DB_COMMENTS = "comments";
    public static final String DB_IS_READY = "isReady";
    public static final String DB_IS_SERVED = "isServed";

    private Integer orderItemId;
    private Order order;
    private User waiter;
    private User chef;
    private int sitNum = 1;
    private Dish dish;
    private String comments;
    private boolean isReady;
    private boolean isServed;


    public OrderItem(Integer orderItemId, Order order, User waiter, User chef,int sitNum, Dish dish, String comments, boolean isReady, boolean isServed) {
        this.orderItemId = orderItemId;
        this.order = order;
        this.waiter = waiter;
        this.chef = chef;
        this.sitNum = sitNum;
        this.dish = dish;
        this.comments = comments;
        this.isReady = isReady;
        this.isServed = isServed;
    }

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getWaiter() {
        return waiter;
    }

    public void setWaiter(User waiter) {
        this.waiter = waiter;
    }

    public User getChef() {
        return chef;
    }

    public void setChef(User chef) {
        this.chef = chef;
    }

    public int getSitNum() {
        return sitNum;
    }

    public void setSitNum(int sitNum) {
        this.sitNum = sitNum;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public String getComments() { return comments; }

    public void setComments(String comments) { this.comments = comments; }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public boolean isServed() {
        return isServed;
    }

    public void setServed(boolean served) {
        isServed = served;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof OrderItem))
            return false;

        OrderItem orderitem = (OrderItem)obj;
        return orderitem.getOrderItemId() == getOrderItemId();
    }

    @Override
    public String toString() {
        return "Sit: "  + getSitNum() + " " + dish;
    }
}
