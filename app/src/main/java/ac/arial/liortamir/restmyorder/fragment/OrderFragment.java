package ac.arial.liortamir.restmyorder.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import ac.arial.liortamir.restmyorder.R;
import ac.arial.liortamir.restmyorder.adapters.AdapterDataSet;
import ac.arial.liortamir.restmyorder.adapters.OrderAdapter;
import ac.arial.liortamir.restmyorder.entity.DiningTable;
import ac.arial.liortamir.restmyorder.entity.Dish;
import ac.arial.liortamir.restmyorder.entity.DishType;
import ac.arial.liortamir.restmyorder.entity.Order;
import ac.arial.liortamir.restmyorder.entity.OrderItem;
import ac.arial.liortamir.restmyorder.persistence.DataHandler;

public class OrderFragment extends BaseFragment {

    private static final int ORDER_ITEM_ACTION_INSERT = 0;
    private static final int ORDER_ITEM_ACTION_UPDATE = 1;

    final static String TAG = "OrderFragment";
    public static int kindaMutexOrder = 0;
    private int orderItemActionType = ORDER_ITEM_ACTION_INSERT;


    private Spinner spnDiningTable;
    private Spinner spnDish;
    private Spinner spnDishType;
    private EditText txtSit;
    private EditText txtComments;
    private GridView grdOrderList;
    private TextView txtTotal;
    private TextView txtOrderDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order, container, false);
        dataHandler = DataHandler.getInstance();
        createOrderFragment(rootView);
        hideSoftKeyboard(getActivity());
        return rootView;
    }

    // ***** State ***** //

    @Override
    public void saveFragmentState(@NonNull Bundle bundle) {
        saveSpinnerState(R.id.orderFrag_cmbDiningTable, bundle);
        saveSpinnerState(R.id.orderFrag_cmbDish, bundle);
        saveSpinnerState(R.id.orderFrag_cmbDishType, bundle);
        saveEditTextState(R.id.orderFrag_txtSitNum, bundle);
        saveEditTextState(R.id.orderFrag_txtComments, bundle);
    }

    @Override
    public void loadFragmentState(@Nullable Bundle bundle) {
        loadSpinnerState(R.id.orderFrag_cmbDiningTable, bundle);
        loadSpinnerState(R.id.orderFrag_cmbDish, bundle);
        loadSpinnerState(R.id.orderFrag_cmbDishType, bundle);
        loadEditTextState(R.id.orderFrag_txtSitNum, bundle);
        loadEditTextState(R.id.orderFrag_txtComments, bundle);
        evtOrderFragDiningTableChange();
    }

    @Override
    protected void updateFragmentState() {
        evtOrderFragDiningTableChange();
    }

// ***** GUI setup ***** //

    private void createOrderFragment(final View rootView){
        Context ctx = getActivity();
//        Spinner spinner;
//        GridView grd;
        Button btn;
        txtSit = rootView.findViewById(R.id.orderFrag_txtSitNum);
        txtComments = rootView.findViewById(R.id.orderFrag_txtComments);
        grdOrderList = rootView.findViewById(R.id.orderFrag_grdDishList);
        grdOrderList.setAdapter(new OrderAdapter(rootView.getContext(),this.getActivity(), this));

        spnDiningTable = createSpinner(R.id.orderFrag_cmbDiningTable, dataHandler.getTableList(),rootView);
        spnDiningTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                evtOrderFragDiningTableChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spnDishType = createSpinner(R.id.orderFrag_cmbDishType, dataHandler.getDishTypeList(),rootView);
        spnDishType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                evtOrderFragDishTypeChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spnDish = createSpinner(R.id.orderFrag_cmbDish, dataHandler.getDishList(),rootView);

        btn = rootView.findViewById(R.id.orderFrag_cmdAddDish);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evtOrderFragHandleOrderItem(rootView);
            }
        });

        btn = rootView.findViewById(R.id.orderFrag_cmdRemoveDish);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evtOrderFragRemoveDish(rootView);
            }
        });

        btn = rootView.findViewById(R.id.orderFrag_cmdNewDish);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evtOrderFragNewOrderItem();
            }
        });

        btn = rootView.findViewById(R.id.orderFrag_cmdRefresh);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evtOrderFragDiningTableChange();
            }
        });

        txtOrderDate = rootView.findViewById((R.id.orderFrag_lbl_date));


    }

    private void setOrderSum(List<OrderItem> orderItemList){
        txtTotal = rootView.findViewById(R.id.orderFrag_lblSum);
        if(txtTotal == null)
            return;
        Double sum = 0.00;
        for(OrderItem oi : orderItemList){
            sum += oi.getDish().getDishPrice();
        }
        txtTotal.setText(getResources().getString(R.string.orderFrag_total, sum));
    }


    // ***** EVENT HANDLERS ***** //

    public void evtOrderFragEditDish(View view, OrderItem orderItem){
//        EditText txtSit = rootView.findViewById(R.id.orderFrag_txtSitNum);
//        Spinner spnDish = rootView.findViewById(R.id.orderFrag_cmbDish);
//        Spinner spnDishType = rootView.findViewById(R.id.orderFrag_cmbDishType);
//        EditText txtComments = rootView.findViewById(R.id.orderFrag_txtComments);
        Button btn = rootView.findViewById(R.id.orderFrag_cmdAddDish);
        btn.setText(getResources().getString(R.string.orderFrag_cmd_update_dish));

        dataHandler.setSelectedOrderItem(orderItem);    //if we later change the order, we take the data from this member

        String comment = " " + new String(orderItem.getComments());
        txtComments.setText(comment);
        txtSit.setText(String.valueOf(orderItem.getSitNum()));

        ArrayAdapter<DishType> dishTypeadapter = (ArrayAdapter<DishType>)spnDishType.getAdapter();
        int pos = dishTypeadapter.getPosition(orderItem.getDish().getDishType());
        kindaMutexOrder = 1;
        spnDishType.setSelection(pos);

        evtOrderFragDishTypeChange();

        ArrayAdapter dishAdapter = (ArrayAdapter)spnDish.getAdapter();
        pos = dishAdapter.getPosition(orderItem.getDish());
        spnDish.setSelection(pos);
        kindaMutexOrder = 0;
        orderItemActionType = ORDER_ITEM_ACTION_UPDATE;
    }

    public void evtOrderFragDishTypeChange(){
//        Spinner spnDishType = rootView.findViewById(R.id.orderFrag_cmbDishType);
//        Spinner spnDish = rootView.findViewById(R.id.orderFrag_cmbDish);

        if(spnDishType == null || spnDishType.getCount() < 1 || kindaMutexOrder == 1)
            return;
        DishType dishType = (DishType)spnDishType.getSelectedItem();
        ArrayAdapter arr = (ArrayAdapter)spnDish.getAdapter();
        arr.clear();
        arr.addAll(dataHandler.getDishByDishType(dishType));
        spnDish.invalidate();
    }

    public void evtOrderFragDiningTableChange(/*View rootView*/){
//        GridView grd = rootView.findViewById(R.id.orderFrag_grdDishList);
//        Spinner spn = rootView.findViewById(R.id.orderFrag_cmbDiningTable);

        if(spnDiningTable == null || spnDiningTable.getCount() < 1)
            return;
        DiningTable table = (DiningTable)spnDiningTable.getSelectedItem();
        AdapterDataSet adapter = (AdapterDataSet)grdOrderList.getAdapter();
        List<OrderItem> orderItemsList = dataHandler.getOpenOrderItemList(table);
        if(orderItemsList.size() == 0) // new order
            txtOrderDate.setText(getCurrentDate());
        else
            txtOrderDate.setText(dateSqltoDisplay(orderItemsList.get(0).getOrder().getOrderDate()));
        setOrderSum(orderItemsList);
        adapter.setData(orderItemsList);
        grdOrderList.invalidateViews();
        evtOrderFragNewOrderItem();
    }

    public void evtOrderFragNewOrderItem(){
//        EditText txtSit = rootView.findViewById(R.id.orderFrag_txtSitNum);
//        EditText txtComments = rootView.findViewById(R.id.orderFrag_txtComments);
        dataHandler.setSelectedOrderItem(null);

        txtSit.setText("" );
        txtComments.setText("" );
        orderItemActionType = ORDER_ITEM_ACTION_INSERT;
    }

    public void evtOrderFragHandleOrderItem(View rootView){
//        EditText txtSit = rootView.findViewById(R.id.orderFrag_txtSitNum);
//        Spinner spnDish = rootView.findViewById(R.id.orderFrag_cmbDish);
//        Spinner spnTable = rootView.findViewById(R.id.orderFrag_cmbDiningTable);
//        GridView grd = rootView.findViewById(R.id.orderFrag_grdDishList);
//        EditText txtComments = rootView.findViewById(R.id.orderFrag_txtComments);
        AdapterDataSet adapter = (AdapterDataSet)grdOrderList.getAdapter();
        Dish dish = (Dish)spnDish.getSelectedItem();
        int sit = 0;

        try {
            sit = Integer.valueOf(txtSit.getText().toString());
        }catch(NumberFormatException e){ sit = 0; }
        if(sit == 0){
            Toast.makeText(rootView.getContext(), "Invalid sit number", Toast.LENGTH_SHORT).show();
            return;
        }

        switch(orderItemActionType){
            case ORDER_ITEM_ACTION_INSERT:
                Order order = null;
                if(adapter.getCount() == 0){ //orderItem list is empty - New Order
                    DiningTable table = (DiningTable)spnDiningTable.getSelectedItem();
                    order = dataHandler.insertOrder(table);
                }else{
                    order = ((OrderItem)adapter.getItem(0)).getOrder();
                }
                dataHandler.insertOrderItem(order, dish, sit, txtComments.getText().toString());
                Toast.makeText(rootView.getContext(), getResources().getString(R.string.orderFrag_toast_added), Toast.LENGTH_SHORT).show();
                break;
            case ORDER_ITEM_ACTION_UPDATE:
                OrderItem orderItem = dataHandler.getSelectedOrderItem();
                orderItem.setSitNum(sit);
                orderItem.setComments(txtComments.getText().toString());
                orderItem.setDish(dish);
                dataHandler.updateOrderItem(orderItem);
                Toast.makeText(rootView.getContext(), getResources().getString(R.string.orderFrag_toast_Updated), Toast.LENGTH_SHORT).show();
                Button btn = rootView.findViewById(R.id.orderFrag_cmdAddDish);
                btn.setText(getResources().getString(R.string.orderFrag_cmd_add_dish));
                break;
        }

        evtOrderFragDiningTableChange();
        grdOrderList.invalidateViews();
    }

    public void evtOrderFragRemoveDish(View rootView){
//        EditText txtSit = rootView.findViewById(R.id.orderFrag_txtSitNum);
//        EditText txtComments = rootView.findViewById(R.id.orderFrag_txtComments);
//        GridView grd = rootView.findViewById(R.id.orderFrag_grdDishList);
        Button btn = rootView.findViewById(R.id.orderFrag_cmdAddDish);
        btn.setText(getResources().getString(R.string.orderFrag_cmd_add_dish));

        if(dataHandler.getSelectedOrderItem() == null){
            Toast.makeText(rootView.getContext(), "No dish selected", Toast.LENGTH_SHORT).show();
            return;
        }

        dataHandler.deleteOrderItem(dataHandler.getSelectedOrderItem().getOrderItemId());
        dataHandler.setSelectedOrderItem(null);
        Toast.makeText(rootView.getContext(), "Dish removed", Toast.LENGTH_SHORT).show();
        txtSit.setText("" );
        txtComments.setText("" );
        evtOrderFragDiningTableChange();
        grdOrderList.invalidateViews();
    }

}
