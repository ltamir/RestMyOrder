package ac.arial.liortamir.restmyorder.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;

import ac.arial.liortamir.restmyorder.R;
import ac.arial.liortamir.restmyorder.adapters.AdapterDataSet;
import ac.arial.liortamir.restmyorder.adapters.KitchenAdapter;
import ac.arial.liortamir.restmyorder.entity.DiningTable;
import ac.arial.liortamir.restmyorder.entity.OrderItem;
import ac.arial.liortamir.restmyorder.persistence.DataHandler;

public class KitchenFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_kitchen, container, false);
        dataHandler = DataHandler.getInstance();
        createKitchenFragment(rootView);
        hideSoftKeyboard(getActivity());

        return rootView;
    }


    // ***** State ***** //

    @Override
    public void saveFragmentState(@NonNull Bundle bundle) {

        saveSpinnerState(R.id.kitchenFrag_spnTable, bundle);
    }

    public void loadFragmentState(@Nullable Bundle bundle) {
        loadSpinnerState(R.id.kitchenFrag_spnTable, bundle);
        evtKitchenFragDiningTableChange(rootView);
    }

    @Override
    protected void updateFragmentState() {
        evtKitchenFragDiningTableChange(rootView);
    }

    // ***** Fragment Setup ***** //
    private void createKitchenFragment(final View rootView){
        Spinner spinner = null;
        GridView grd = null;

        grd = rootView.findViewById(R.id.kitchenFrag_grdOrderItems);
        grd.setAdapter(new KitchenAdapter(rootView.getContext(),getActivity(), this));


        spinner = createSpinner(R.id.kitchenFrag_spnTable, dataHandler.getTableList(), rootView);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                evtKitchenFragDiningTableChange(rootView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button btn = (Button)rootView.findViewById(R.id.kitchenFrag_btnShowOrder);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                evtKitchenFragDiningTableChange(rootView);
            }
        });

        btn = (Button)rootView.findViewById(R.id.kitchenFrag_btnRefresh);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                evtKitchenFragDiningTableChange(rootView);
            }
        });

    }


    // ***** EVENT HANDLERS ***** //

    private void evtKitchenFragDiningTableChange(View rootView){
        GridView grd = (GridView)rootView.findViewById(R.id.kitchenFrag_grdOrderItems);
        Spinner spn = (Spinner)rootView.findViewById(R.id.kitchenFrag_spnTable);

        if(spn == null || spn.getCount() < 1)
            return;
        DiningTable table = (DiningTable)spn.getSelectedItem();
        AdapterDataSet adapter = (AdapterDataSet)grd.getAdapter();
        adapter.setData(dataHandler.getOpenOrderItemList(table));
        adapter.notifyDataSetChanged();
        grd.invalidateViews();
    }

    public void showWaiter(OrderItem orderItem){
        makeToastLong(orderItem.getWaiter().getFullName());
    }
}
