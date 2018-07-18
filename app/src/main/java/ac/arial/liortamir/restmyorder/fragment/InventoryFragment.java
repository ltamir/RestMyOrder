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
import android.widget.Spinner;
import android.widget.Toast;

import ac.arial.liortamir.restmyorder.R;
import ac.arial.liortamir.restmyorder.entity.DiningTable;
import ac.arial.liortamir.restmyorder.entity.Dish;
import ac.arial.liortamir.restmyorder.entity.DishType;
import ac.arial.liortamir.restmyorder.persistence.DataHandler;


public class InventoryFragment extends BaseFragment {

    private static final int INVENTORY_ACTION_INSERT = 0;
    private static final int INVENTORY_ACTION_UPDATE = 1;
    private int diningTableActionType = INVENTORY_ACTION_INSERT;
    private int dishActionType = INVENTORY_ACTION_INSERT;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_inventory, container, false);
        dataHandler = DataHandler.getInstance();
        createDishFragment(rootView);
        hideSoftKeyboard(getActivity());

        return rootView;
    }

    // ***** State ***** //

    @Override
    public void saveFragmentState(@NonNull Bundle bundle) {
        saveSpinnerState(R.id.dishFrag_cmbDish, bundle);
        saveSpinnerState(R.id.dishFrag_cmbDishType, bundle);
        saveSpinnerState(R.id.dishFrag_cmbEditDishType, bundle);
        saveEditTextState(R.id.dishFrag_txtEditDishName, bundle);
        saveEditTextState(R.id.dishFrag_txtEditDishPrice, bundle);
    }

    public void loadFragmentState(@Nullable Bundle bundle) {
        loadSpinnerState(R.id.dishFrag_cmbDish, bundle);
        loadSpinnerState(R.id.dishFrag_cmbDishType, bundle);
        loadSpinnerState(R.id.dishFrag_cmbEditDishType, bundle);
        loadEditTextState(R.id.dishFrag_txtEditDishName, bundle);
        loadEditTextState(R.id.dishFrag_txtEditDishPrice, bundle);
        loadSpinnerState(R.id.dishFrag_cmbDiningTable, bundle);
        loadEditTextState(R.id.dishFrag_txtEditDiningTable, bundle);

    }

    @Override
    protected void updateFragmentState() {

    }

    // ***** GUI Setup  ***** //

    private void createDishFragment(final View rootView){
        Context ctx = getActivity();
        Spinner spinner = null;

        // ***** Dish ***** //

        spinner = createSpinner(R.id.dishFrag_cmbDishType,dataHandler.getDishTypeList(),rootView);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                evtDishFragDishTypeChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        createSpinner(R.id.dishFrag_cmbDish, dataHandler.getDishList(), rootView);
        createSpinner(R.id.dishFrag_cmbEditDishType, dataHandler.getDishTypeList(), rootView);
        Button btn = rootView.findViewById(R.id.dishFrag_cmdUpdate);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evtDishFragUpdateDish(v);
            }
        });

        btn = rootView.findViewById(R.id.dishFrag_cmdEditDish);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evtDishFragEditDish(v);
            }
        });
        btn = rootView.findViewById(R.id.dishFrag_cmdNewDish);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evtDishFragNewDish(v);
            }
        });


        // ***** Dining Table ***** //

        createSpinner(R.id.dishFrag_cmbDiningTable, dataHandler.getTableList(), rootView);

        btn = rootView.findViewById(R.id.dishFrag_cmdNewDiningTable);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText diningTableName = rootView.findViewById(R.id.dishFrag_txtEditDiningTable);
                diningTableName.setText("");
                diningTableActionType = InventoryFragment.INVENTORY_ACTION_INSERT;
            }
        });
        btn = rootView.findViewById(R.id.dishFrag_cmdEditDiningTable);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinner = rootView.findViewById(R.id.dishFrag_cmbDiningTable);
                DiningTable diningTable = (DiningTable)spinner.getSelectedItem();
                EditText diningTableName = rootView.findViewById(R.id.dishFrag_txtEditDiningTable);
                diningTableName.setText(diningTable.getDiningTableName());
                diningTableActionType = InventoryFragment.INVENTORY_ACTION_UPDATE;
            }
        });

        btn = rootView.findViewById(R.id.dishFrag_cmdSaveDiningTable);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evtUandleDiningTable();
            }
        });
    }


    // ***** EVENT HANDLERS ***** //

    private void evtDishFragDishTypeChange(){
        Spinner spnDishType = rootView.findViewById(R.id.dishFrag_cmbDishType);
        Spinner spnDish = rootView.findViewById(R.id.dishFrag_cmbDish);

        if(spnDishType == null || spnDishType.getCount() < 1)
            return;
        DishType dishType = (DishType)spnDishType.getSelectedItem();
        ArrayAdapter arr = (ArrayAdapter)spnDish.getAdapter();
        arr.clear();
        arr.addAll(dataHandler.getDishByDishType(dishType));
        spnDish.invalidate();
    }

    private void evtDishFragUpdateDish(View view){
        Spinner spnDishType = rootView.findViewById(R.id.dishFrag_cmbEditDishType);
        Spinner spnDish = rootView.findViewById(R.id.dishFrag_cmbDish);
        EditText txtDishName = rootView.findViewById(R.id.dishFrag_txtEditDishName);
        EditText txtDishPrice = rootView.findViewById(R.id.dishFrag_txtEditDishPrice);
        Double dishPrice = 0.00;
        Dish dish = null;

        if(txtDishName.getText().toString().length() == 0){
            Toast.makeText(view.getContext(), "", Toast.LENGTH_SHORT).show();
            return;
        }
        if(txtDishPrice.getText().toString().length() == 0){
            Toast.makeText(view.getContext(), "", Toast.LENGTH_SHORT).show();
            return;
        }

        try{
            dishPrice = Double.valueOf(txtDishPrice.getText().toString());
        }catch(NumberFormatException e){
            Toast.makeText(view.getContext(), "", Toast.LENGTH_SHORT).show();
            return;
        }

        switch(dishActionType){
            case InventoryFragment.INVENTORY_ACTION_UPDATE:
                dish = (Dish)spnDish.getSelectedItem();
                dish.setDishName(txtDishName.getText().toString());
                dish.setDishPrice(dishPrice);
                dish.setDishType((DishType)spnDishType.getSelectedItem());
                dataHandler.updateDish(dish);
                break;
            case InventoryFragment.INVENTORY_ACTION_INSERT:
                dish = new Dish(null, txtDishName.getText().toString(), dishPrice,(DishType)spnDishType.getSelectedItem());
                dataHandler.insertDish(dish);
                break;
        }

        evtDishFragDishTypeChange();
    }

    private void evtDishFragEditDish(View view){
        Spinner spnDish = rootView.findViewById(R.id.dishFrag_cmbDish);
        Spinner spnDishType = rootView.findViewById(R.id.dishFrag_cmbEditDishType);
        EditText txtDishName = rootView.findViewById(R.id.dishFrag_txtEditDishName);
        EditText txtDishPrice = rootView.findViewById(R.id.dishFrag_txtEditDishPrice);
        Button btnAction = rootView.findViewById(R.id.dishFrag_cmdUpdate);

        Dish dish = (Dish)spnDish.getSelectedItem();
        txtDishName.setText(dish.getDishName());
        txtDishPrice.setText(String.valueOf(dish.getDishPrice()) );

        ArrayAdapter dishTypeadapter = (ArrayAdapter)spnDish.getAdapter();
        int pos = dishTypeadapter.getPosition(dish);
        spnDish.setSelection(pos);

        dishTypeadapter = (ArrayAdapter)spnDishType.getAdapter();
        pos = dishTypeadapter.getPosition(dish.getDishType());
        spnDishType.setSelection(pos);
        dishActionType = InventoryFragment.INVENTORY_ACTION_UPDATE;
        btnAction.setText(getResources().getString(R.string.dishFrag_action_update));
    }

    private void evtDishFragNewDish(View view){
        Spinner spnDishType = rootView.findViewById(R.id.dishFrag_cmbEditDishType);
        EditText txtDishName = rootView.findViewById(R.id.dishFrag_txtEditDishName);
        EditText txtDishPrice = rootView.findViewById(R.id.dishFrag_txtEditDishPrice);
        Button btnAction = rootView.findViewById(R.id.dishFrag_cmdUpdate);
        spnDishType.setSelection(0);
        txtDishName.setText("");
        txtDishPrice.setText("");
        dishActionType = InventoryFragment.INVENTORY_ACTION_INSERT;
        btnAction.setText(getResources().getString(R.string.dishFrag_action_insert));
    }

    private void evtUandleDiningTable(){
        DiningTable diningTable = null;
        EditText diningTableName = rootView.findViewById(R.id.dishFrag_txtEditDiningTable);
        Spinner spinner = rootView.findViewById(R.id.dishFrag_cmbDiningTable);
        switch(diningTableActionType){
            case InventoryFragment.INVENTORY_ACTION_INSERT:
                diningTable = new DiningTable(null,diningTableName.getText().toString());
                dataHandler.insertDiningTable(diningTable);
                break;
            case InventoryFragment.INVENTORY_ACTION_UPDATE:
                diningTable = (DiningTable)spinner.getSelectedItem();
                diningTable.setDiningTableName(diningTableName.getText().toString());
                dataHandler.updateDiningTable(diningTable);
                break;
        }
        ArrayAdapter arr = (ArrayAdapter)spinner.getAdapter();
        arr.clear();
        arr.addAll(dataHandler.getTableList());
        spinner.invalidate();
    }
}
