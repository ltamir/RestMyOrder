package ac.arial.liortamir.restmyorder.entity;

import java.util.LinkedList;
import java.util.List;

public class Order {
    public static final String DB_ORDER_ID = "orderId";
    public static final String DB_ORDER_ORDER_DATE = "orderDate";
    public static final String DB_DINING_TABLE_ID = "diningTableId";
    public static final String DB_IS_CLOSED = "isClosed";

    private Integer orderId;
    private String orderDate;
    private DiningTable diningTable;
    private boolean isClosed;
    private List<OrderItem> orderItemList;

    public Order(Integer orderId, DiningTable diningTable, boolean isClosed, List<OrderItem> orderItemList) {
        this.orderId = orderId;
        this.diningTable = diningTable;
        this.isClosed = isClosed;
        this.orderItemList = orderItemList;
    }

    public Order(Integer orderId, String orderDate, DiningTable diningTable, boolean isClosed) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.diningTable = diningTable;
        this.isClosed = isClosed;
        this.orderItemList = new LinkedList<>();
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public DiningTable getDiningTable() {
        return diningTable;
    }

    public void setDiningTable(DiningTable diningTable) {
        this.diningTable = diningTable;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Order))
            return false;

        Order order = (Order)obj;
        return order.getOrderId() == getOrderId();
    }

    @Override
    public String toString() {
        final String[] dateTimeArr = orderDate.split(" ");
        final String[] dateArr = dateTimeArr[0].split("-");
        final String date = dateArr[2] + "/" +dateArr[1] + "/" +dateArr[0];
        if(dateTimeArr.length == 2)
            return getDiningTable() + " " + date + " " +  dateTimeArr[1];

        return getDiningTable() + " " + date;
    }
}
