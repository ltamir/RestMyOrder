package ac.arial.liortamir.restmyorder.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.util.SparseArray;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ac.arial.liortamir.restmyorder.entity.DiningTable;
import ac.arial.liortamir.restmyorder.entity.Dish;
import ac.arial.liortamir.restmyorder.entity.DishType;
import ac.arial.liortamir.restmyorder.entity.Order;
import ac.arial.liortamir.restmyorder.entity.OrderItem;
import ac.arial.liortamir.restmyorder.entity.Role;
import ac.arial.liortamir.restmyorder.entity.User;

public class DataHandler {
    private static DataHandler dataHandler = new DataHandler();
    private DBHandler dbHandler ;

    private List<String> queryList = new LinkedList<>();
    private OrderItem selectedOrderItem;
    private User activeUser;

    // ***** Cache ***** //
//    HashMap<Integer, Order> orderMap = new HashMap<>();
    SparseArray<Order> orderMap = new SparseArray<>();
//    Map<Integer, DishType> dishTypeMap = new HashMap<>();
    SparseArray<DishType> dishTypeMap = new SparseArray<>();
//    Map<Integer, Dish> dishMap = new HashMap<>();
    SparseArray<Dish> dishMap = new SparseArray<>();
    SparseArray<User> userMap = new SparseArray<>();


    // ***** singletone implementation ***** //

    private DataHandler(){
        queryList.add("Select a query");
        queryList.add("select * from [order]");
        queryList.add("select * from orderItem");
        queryList.add("select * from diningTable");
        queryList.add("select * from dish");
        queryList.add("select * from dishType");
        queryList.add("select * from role");
        queryList.add("select * from user");

    }

    public static DataHandler getInstance(Context ctx){
        dataHandler.dbHandler = new DBHandler(ctx);
        return dataHandler;
    }

    public static DataHandler getInstance(){
        return dataHandler;
    }

    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }

    // ***** Cache ***** //

    private Order getOrderCache(Integer orderId){
        Order order = orderMap.get(orderId);
        if(order == null) {
            order = getOrderById(orderId);
            orderMap.put(orderId, order);
        }
        return order;
    }

    private DishType getDishTypeCache(Integer dishTypeId){
        DishType dishType = dishTypeMap.get(dishTypeId);
        if(dishType == null) {
            dishType = getDishTypeById(dishTypeId);
            dishTypeMap.put(dishTypeId, dishType);
        }
        return dishType;
    }

    private Dish getDishCache(Integer dishId){
        Dish dish = dishMap.get(dishId);
        if(dish == null) {
            dish = getDishById(dishId);
            dishMap.put(dishId, dish);
        }
        return dish;
    }

    private User getUserCache(Integer userId){
        User user = userMap.get(userId);
        if(user == null) {
            user = getUserById(userId);
            userMap.put(userId, user);
        }
        return user;
    }
// ***** Business logic related ***** //


    public OrderItem getSelectedOrderItem() {
        return selectedOrderItem;
    }

    public void setSelectedOrderItem(OrderItem selectedOrderItem) {
        this.selectedOrderItem = selectedOrderItem;
    }

    public List<String> getQueriesList(){
        return this.queryList;
    }

    public void handleCloseOrder(OrderItem orderItem){
        ContentValues cv = dbHandler.getSingle("select count(isServed) as openCnt from orderItem where orderId = ? and isServed = 0", new String[]{orderItem.getOrder().getOrderId().toString()});
        int openOrderItems = cv.getAsInteger("openCnt");
        if(openOrderItems == 0) {
            orderItem.getOrder().setClosed(true);
            ContentValues cvOrder = mapOrder(orderItem.getOrder());
            dbHandler.update("[order]", cvOrder, "orderId = ?", new String[]{orderItem.getOrder().getOrderId().toString()});
        }
    }

    public List<ContentValues> executeQuery(String query){
        List<ContentValues> cvList = null;
        try {
            cvList = dbHandler.getMultiple(query, new String[]{});
        }catch(SQLiteException e){
            ContentValues cv = new ContentValues();
            cv.put("SQL Error", e.getMessage());
            cvList = new LinkedList<>();
            cvList.add(cv);
            return cvList;
        }
        if(cvList == null){
            cvList = new LinkedList<>();
        }
        if(cvList.size() == 0){
            ContentValues cv = new ContentValues();
            cv.put("No Results ", "");
            cvList.add(cv);
        }
        return cvList;
    }

    public List<DiningTable> getTableList(){
        List<DiningTable> diningTableList = new LinkedList<>();
        List<ContentValues> cvList = dbHandler.getMultiple("select * from diningTable", new String[]{});

        for(ContentValues cv : cvList){
            diningTableList.add(mapDiningTable(cv));
        }
        return diningTableList;
    }

    public List<Order> getOrderList(String date){
        List<Order> orderList = new LinkedList<>();
        List<ContentValues> cvList = dbHandler.getMultiple("select * from [order] where date(orderDate) = date(?)", new String[]{date});

        for(ContentValues cv : cvList){
            orderList.add(mapOrder(cv));
        }
        return orderList;
    }

    public List<DishType> getDishTypeList(){
        List<DishType> dishTypeList = new LinkedList<>();
        List<ContentValues> cvList = dbHandler.getMultiple("select * from dishType", new String[]{});

        for(ContentValues cv : cvList){
            dishTypeList.add(mapDishType(cv));
        }
        return dishTypeList;
    }

    public List<Dish> getDishList(){
        List<Dish> dishList = new LinkedList<>();
        List<ContentValues> cvList = dbHandler.getMultiple("select * from dish", new String[]{});

        for(ContentValues cv : cvList){
            dishList.add(mapDish(cv));
        }
        return dishList;
    }

    public User authenticateUser(String email, String password){
        User user = null;
        ContentValues cv = dbHandler.getSingle("select * from user where email = ? and password = ?", new String[]{email, password});
        if(cv.size()> 0)
            user = mapUser(cv);
        return user;
    }

    public List<OrderItem> getAllOrderItemList(Order order){
        List<OrderItem> orderItemList = new LinkedList<>();

        List<ContentValues> cvList = dbHandler.getMultiple("select * from orderItem inner join dish on dish.dishId = orderItem.dishId inner join dishType on dishType.dishTypeId = dish.dishTypeId where orderId in (select orderId from [order] where orderId = ?) order by sitNum, dishType.dishTypeId", new String[]{String.valueOf(order.getOrderId())});
        for(ContentValues cv : cvList){
            orderItemList.add(mapOrderItem(cv));
        }
        return orderItemList;
    }

    public List<User> getUserList(){
        List<User> userList = new LinkedList<>();
        List<ContentValues> cvList = dbHandler.getMultiple("select * from user order by fullName", new String[]{});
        for(ContentValues cv : cvList){
            userList.add(mapUser(cv));
        }
        return userList;
    }

    public List<Role> getRoles(){
        List<Role> roleList = new LinkedList<>();
        List<ContentValues> cvList = dbHandler.getMultiple("select * from role order by roleName", new String[]{});
        for(ContentValues cv : cvList){
            roleList.add(mapRole(cv));
        }
        return roleList;
    }

    public List<OrderItem> getOpenOrderItemList(DiningTable table){
        List<OrderItem> orderItemList = new LinkedList<>();

        List<ContentValues> cvList = dbHandler.getMultiple("select * from orderItem inner join dish on dish.dishId = orderItem.dishId inner join dishType on dishType.dishTypeId = dish.dishTypeId where orderId in (select orderId from [order] where diningTableId = ? and isClosed = 0) order by sitNum, dishType.dishTypeId", new String[]{String.valueOf(table.getDiningTableId())});
        for(ContentValues cv : cvList){
            orderItemList.add(mapOrderItem(cv));
        }
        return orderItemList;
    }

    public List<Dish> getDishByDishType(DishType dt){
        List<Dish> dishList = new LinkedList<>();
        List<ContentValues> cvList = dbHandler.getMultiple("select * from dish where dishTypeId = ?", new String[]{String.valueOf(dt.getDishTypeId())});
        for(ContentValues cv : cvList){
            dishList.add(mapDish(cv));
        }
        return dishList;
    }


    // ***** Data update ***** //

    public void updateOrderItem(OrderItem orderItem){
        ContentValues cv = mapOrderItem(orderItem);
        dbHandler.update("orderItem", cv, "orderItemId = ?", new String[]{cv.getAsString(OrderItem.DB_ORDER_ITEM_ID)});
        handleCloseOrder(orderItem);
    }

    public void updateDish(Dish dish){
        ContentValues cv = mapDish(dish);
        dbHandler.update("dish", cv, "dishId = ?", new String[]{cv.getAsString(Dish.DB_DISH_ID)});
        dishMap.put(dish.getDishId(), dish);
    }

    public void updateOrder(Order order){
        ContentValues cv = mapOrder(order);
        dbHandler.update("order", cv, "orderId = ?", new String[]{cv.getAsString(OrderItem.DB_ORDER_ID)});
    }

    public void updateDiningTable(DiningTable table){
        ContentValues cv = mapDiningTable(table);
        dbHandler.update("diningTable", cv, "diningTableId = ?", new String[]{table.getDiningTableId().toString()});
    }

    public String updateUser(User user){
        ContentValues cv = mapUser(user);
        String result = dbHandler.update("[user]", cv, User.DB_USER_ID + " = ?", new String[]{user.getUserId().toString()});
        return result;
    }

    // ***** Data insert ***** //

    public void insertOrderItem(Order order, Dish dish, int sit, String comments){
        OrderItem orderItem = new OrderItem(null, order, getActiveUser(), null,sit, dish, comments,false,false);
        ContentValues cv = mapOrderItem(orderItem);
        dbHandler.insert("orderItem", cv);
    }

    public Order insertOrder(DiningTable table){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        String date = String.format("%d-%02d-%02d %d:%d",year, month+1, day,hour,minute);
        Order order = new Order(null,date,table,false);
        ContentValues cv = mapOrder(order);
        long orderId = dbHandler.insert("[order]", cv);
        order.setOrderId((int)orderId);
        return order;
    }

    public Dish insertDish(Dish dish){
        ContentValues cv = mapDish(dish);
        long newId = dbHandler.insert("dish", cv);
        dish.setDishId((int)newId);
        return dish;
    }

    public DiningTable insertDiningTable(DiningTable diningTable){
        ContentValues cv = mapDiningTable(diningTable);
        long newId = dbHandler.insert("diningTable", cv);
        diningTable.setDiningTableId((int)newId);
        return diningTable;
    }

    public User insertUser(User user){
        ContentValues cv = mapUser(user);
        long newId = dbHandler.insert("[user]", cv);
        user.setUserId((int)newId);
        return user;
    }

    public void deleteOrderItem(int orderItemId){
        dbHandler.delete("orderItem", "orderItemId = ?", new String[]{String.valueOf(orderItemId)});
    }

    // ***** single entity by ID ***** //

    private DishType getDishTypeById(int id){
        ContentValues cv = dbHandler.getSingle("select * from dishType where dishTypeId = ?", new String[]{String.valueOf(id)});
        return mapDishType(cv);
    }

    private Dish getDishById(int id){
        ContentValues cv = dbHandler.getSingle("select * from dish where dishId = ?", new String[]{String.valueOf(id)});
        return mapDish(cv);
    }

    private Order getOrderById(int id){
        ContentValues cv = dbHandler.getSingle("select * from [order] where orderId = ?", new String[]{String.valueOf(id)});
        return mapOrder(cv);
    }

    private DiningTable getDiningTableById(int id){
        ContentValues cv = dbHandler.getSingle("select * from diningTable where diningTableId = ?", new String[]{String.valueOf(id)});
        return mapDiningTable(cv);
    }

    public OrderItem getOrderItemById(int id){
        ContentValues cv = dbHandler.getSingle("select * from orderItem where orderItemId = ?", new String[]{String.valueOf(id)});
        return mapOrderItem(cv);
    }

    public Role getRoleById(int id){
        ContentValues cv = dbHandler.getSingle("select * from role where roleId = ?", new String[]{String.valueOf(id)});
        return mapRole(cv);
    }

    public User getUserById(int id){
        ContentValues cv = dbHandler.getSingle("select * from user where userId = ?", new String[]{String.valueOf(id)});
        return mapUser(cv);
    }

    // ***** ContentValues to Entity mapping ***** //

    private Dish mapDish(ContentValues cv){
        Dish dish = new Dish(cv.getAsInteger(Dish.DB_DISH_ID),
                cv.getAsString(Dish.DB_DISH_NAME),
                cv.getAsDouble(Dish.DB_PRICE),
                getDishTypeCache(cv.getAsInteger(Dish.DB_DISH_TYPE_ID))
                );

        return dish;
    }
    private DishType mapDishType(ContentValues cv){
        DishType dt = new DishType(cv.getAsInteger(DishType.DB_DISH_TYPE_ID),
                cv.getAsString(DishType.DB_DISH_TYPE_NAME)
        );
        return dt;
    }

    private Order mapOrder(ContentValues cv){
        Order order = new Order(
                cv.getAsInteger(Order.DB_ORDER_ID),
                cv.getAsString(Order.DB_ORDER_ORDER_DATE),
                getDiningTableById(cv.getAsInteger(Order.DB_DINING_TABLE_ID)),
                cv.getAsBoolean(Order.DB_IS_CLOSED)
        );
        return order;
    }

    private DiningTable mapDiningTable(ContentValues cv){
        DiningTable dt = new DiningTable(
                cv.getAsInteger(DiningTable.DB_DINING_TABLE_ID),
                cv.getAsString(DiningTable.DB_DINING_TABLE_NAME)
        );
        return dt;
    }

    private OrderItem mapOrderItem(ContentValues cv){
        OrderItem orderItem = null;
        Order order = getOrderCache(cv.getAsInteger(OrderItem.DB_ORDER_ID));
        Dish dish = getDishCache(cv.getAsInteger(OrderItem.DB_DISH_ID));
        User waiter = getUserCache(cv.getAsInteger(OrderItem.DB_WAITER_USER_ID));
        User chef = cv.getAsInteger(OrderItem.DB_CHEF_USER_ID) == null?null:getUserCache(cv.getAsInteger(OrderItem.DB_CHEF_USER_ID));

        orderItem = new OrderItem(
                cv.getAsInteger(OrderItem.DB_ORDER_ITEM_ID),
                order,
                waiter,
                chef,
                cv.getAsInteger(OrderItem.DB_SIT_NUM),
                dish,
                cv.getAsString(OrderItem.DB_COMMENTS),
                cv.getAsBoolean(OrderItem.DB_IS_READY),
                cv.getAsBoolean(OrderItem.DB_IS_SERVED)
        );
        return orderItem;
    }

    private Role mapRole(ContentValues cv){
        Role dt = new Role(
                cv.getAsInteger(Role.DB_ROLE_ID),
                cv.getAsString(Role.DB_ROLE_NAME)
        );
        return dt;
    }

    private User mapUser(ContentValues cv){
        User dt = new User(
                cv.getAsInteger(User.DB_USER_ID),
                cv.getAsString(User.DB_USER_FULL_NAME),
                cv.getAsString(User.DB_USER_EMAIL),
                cv.getAsString(User.DB_USER_PASSWORD),
                getRoleById(cv.getAsInteger(User.DB_USER_ROLE_ID))
        );
        return dt;
    }


    // ***** Entity to ContentValues mapping ***** //

    private ContentValues mapDish(Dish dish){
        ContentValues cv = new ContentValues();

        cv.put(Dish.DB_DISH_ID, dish.getDishId());
        cv.put(Dish.DB_DISH_NAME, dish.getDishName());
        cv.put(Dish.DB_PRICE, dish.getDishPrice());
        cv.put(Dish.DB_DISH_TYPE_ID, dish.getDishType().getDishTypeId());

        return cv;
    }
    private ContentValues mapDishType(DishType dishType){
        ContentValues cv = new ContentValues();
        cv.put(DishType.DB_DISH_TYPE_ID, dishType.getDishTypeId());
        cv.put(DishType.DB_DISH_TYPE_NAME, dishType.getDishTypeName());

        return cv;
    }

    private ContentValues mapOrder(Order order){
        ContentValues cv = new ContentValues();

        cv.put(Order.DB_ORDER_ID, order.getOrderId());
        cv.put(Order.DB_ORDER_ORDER_DATE, order.getOrderDate());
        cv.put(Order.DB_DINING_TABLE_ID, order.getDiningTable().getDiningTableId());
        cv.put(Order.DB_IS_CLOSED, order.isClosed());

        return cv;
    }

    private ContentValues mapDiningTable(DiningTable diningTable){
        ContentValues cv = new ContentValues();
        cv.put(DiningTable.DB_DINING_TABLE_ID, diningTable.getDiningTableId());
        cv.put(DiningTable.DB_DINING_TABLE_NAME, diningTable.getDiningTableName());

        return cv;
    }

    private ContentValues mapOrderItem(OrderItem orderItem){
        ContentValues cv = new ContentValues();

        cv.put(OrderItem.DB_ORDER_ITEM_ID, orderItem.getOrderItemId());
        cv.put(OrderItem.DB_ORDER_ID, orderItem.getOrder().getOrderId());
        cv.put(OrderItem.DB_WAITER_USER_ID, orderItem.getWaiter().getUserId());
        cv.put(OrderItem.DB_CHEF_USER_ID, (orderItem.getChef() == null?null:orderItem.getChef().getUserId()));
        cv.put(OrderItem.DB_SIT_NUM, orderItem.getSitNum());
        cv.put(OrderItem.DB_DISH_ID, orderItem.getDish().getDishId());
        cv.put(OrderItem.DB_COMMENTS, orderItem.getComments());
        cv.put(OrderItem.DB_IS_READY, orderItem.isReady());
        cv.put(OrderItem.DB_IS_SERVED, orderItem.isServed());

        return cv;
    }

    private ContentValues mapRole(Role role){
        ContentValues cv = new ContentValues();
        cv.put(Role.DB_ROLE_ID, role.getRoleId());
        cv.put(Role.DB_ROLE_NAME, role.getRoleName());

        return cv;
    }

    private ContentValues mapUser(User user){
        ContentValues cv = new ContentValues();
        cv.put(User.DB_USER_ID, user.getUserId());
        cv.put(User.DB_USER_FULL_NAME, user.getFullName());
        cv.put(User.DB_USER_EMAIL, user.getEmail());
        cv.put(User.DB_USER_PASSWORD, user.getPassword());
        cv.put(User.DB_USER_ROLE_ID, user.getRole().getRoleId());

        return cv;
    }
}
