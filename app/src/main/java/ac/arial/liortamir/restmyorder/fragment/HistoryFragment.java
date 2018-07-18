package ac.arial.liortamir.restmyorder.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import ac.arial.liortamir.restmyorder.R;
import ac.arial.liortamir.restmyorder.adapters.AdapterDataSet;
import ac.arial.liortamir.restmyorder.adapters.HistoryAdapter;
import ac.arial.liortamir.restmyorder.entity.Order;
import ac.arial.liortamir.restmyorder.entity.OrderItem;
import ac.arial.liortamir.restmyorder.persistence.DataHandler;


public class HistoryFragment extends BaseFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history, container, false);
        dataHandler = DataHandler.getInstance();
        createHistoryFragment(rootView);
        hideSoftKeyboard(getActivity());

        return rootView;
    }

    // ***** State ***** //
    @Override
    public void saveFragmentState(@NonNull Bundle bundle) {
        saveSpinnerState(R.id.historyFrag_spnDiningTable, bundle);
    }

    public void loadFragmentState(@Nullable Bundle bundle) {

        loadSpinnerState(R.id.historyFrag_spnDiningTable, bundle);
    }

    @Override
    protected void updateFragmentState() {

    }

    private void createHistoryFragment(final View rootView){
        Spinner spinner = null;
        GridView grd = null;

        grd = (GridView)rootView.findViewById(R.id.historyFrag_grdOrderItems);
        grd.setAdapter(new HistoryAdapter(rootView.getContext(),getActivity(), this));

        TextView lblDate = (TextView)rootView.findViewById(R.id.historyFrag_lblSelectedDate);
        String date = getCurrentDate();
        lblDate.setText(date);
        // date in format YYYY-MM-DD HH:MM
        date = dateDisplayToSql(date);
        spinner = createSpinner(R.id.historyFrag_spnDiningTable, dataHandler.getOrderList(date), rootView);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                evtHistoryFragDiningTableChange(rootView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button btn = (Button)rootView.findViewById(R.id.historyFrag_btnShowOrder);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                evtHistoryFragDiningTableChange(v);
            }
        });

        btn = (Button)rootView.findViewById(R.id.historyFrag_cmdShowTables);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                evtHistoryFragDateSelected();
            }
        });
        lblDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                evtHistoryShowDatePickerDialog(rootView);
            }
        });

    }


    // ***** EVENT HANDLERS ***** //

    public void evtHistoryShowDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private void evtHistoryFragDateSelected(){
        Spinner spn = (Spinner)getActivity().findViewById(R.id.historyFrag_spnDiningTable);
        TextView lblDate = (TextView)getActivity().findViewById(R.id.historyFrag_lblSelectedDate);

        String date = dateDisplayToSql(lblDate.getText().toString());
        List<Order> orderList = dataHandler.getOrderList(date);
        if(orderList.size() == 0)
            return;
        ArrayAdapter arr = (ArrayAdapter)spn.getAdapter();
        arr.clear();
        arr.addAll(orderList);
        spn.invalidate();
    }

    private void evtHistoryFragDiningTableChange(View rootView){
        GridView grd = (GridView)getActivity().findViewById(R.id.historyFrag_grdOrderItems);
        Spinner spn = (Spinner)getActivity().findViewById(R.id.historyFrag_spnDiningTable);
        if(spn == null || spn.getCount() < 1)
            return;

        Order order = (Order)spn.getSelectedItem();
        AdapterDataSet adapter = (AdapterDataSet)grd.getAdapter();
        adapter.setData( dataHandler.getAllOrderItemList(order));
        grd.invalidateViews();
    }

    public void showWorkers(OrderItem orderItem){
        makeToastLong(getString(R.string.waiter) + ": " +
                orderItem.getWaiter().getFullName() + "\n" +
                getString(R.string.chef) + ": " +
                (orderItem.getChef()==null?"":orderItem.getChef().getFullName()));
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of TimePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month,day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            TextView lblDate = (TextView)getActivity().findViewById(R.id.historyFrag_lblSelectedDate);
            String date = String.format("%02d/%02d/%d",dayOfMonth , month+1, year);
            lblDate.setText(date);
        }
    }
}
