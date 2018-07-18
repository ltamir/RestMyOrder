package ac.arial.liortamir.restmyorder.fragment;

import android.content.ContentValues;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ac.arial.liortamir.restmyorder.R;
import ac.arial.liortamir.restmyorder.persistence.DataHandler;

public class DBAFragment extends BaseFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dba, container, false);
        dataHandler = DataHandler.getInstance();
        createSqliteFragment(rootView);
        hideSoftKeyboard(getActivity());

        return rootView;
    }

    // ***** State ***** //

    @Override
    public void saveFragmentState(@NonNull Bundle bundle) {
    }

    public void loadFragmentState(@Nullable Bundle bundle) {

    }

    @Override
    protected void updateFragmentState() {

    }

    // ***** Fragment setup ***** //
    private void createSqliteFragment(final View rootView){
        Spinner spinner;
        Button btn;
        final GridView grdResult = rootView.findViewById(R.id.sqliteFrag_grdResult);
        final GridView grdHeader = rootView.findViewById(R.id.sqliteFrag_grdHeader);

        List<String> lstResult = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(rootView.getContext(),
                android.R.layout.simple_list_item_1, lstResult);
        grdResult.setAdapter(adapter);

        List<String> lstHeader = new ArrayList<>();
        ArrayAdapter<String> headerAdapter = new ArrayAdapter<>(rootView.getContext(),
                android.R.layout.simple_list_item_1,lstHeader);
        grdHeader.setAdapter(headerAdapter);

        spinner = createSpinner(R.id.cmbSqliteQueries, dataHandler.getQueriesList(), rootView);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                evtExecuteSql(rootView, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn = rootView.findViewById(R.id.cmdSubmitQuery);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evtExecuteSql(rootView, false);
            }
        });
    }


    // ***** EVENT HANDLERS ***** //

    private void evtExecuteSql(View view, boolean readFromSpinner){
        EditText txtQuery = view.findViewById(R.id.txtSqliteQuery);
        GridView grdHeader = view.findViewById(R.id.sqliteFrag_grdHeader);
        GridView grdResult = view.findViewById(R.id.sqliteFrag_grdResult);
        Spinner spn = view.findViewById(R.id.cmbSqliteQueries);

        if(readFromSpinner)
            txtQuery.setText(spn.getSelectedItem().toString());

        if(spn == null || txtQuery.length() == 0|| spn.getSelectedItemId() == 0)
            return;

        List<ContentValues> result = dataHandler.executeQuery(txtQuery.getText().toString());
        ContentValues cv = result.get(0);

        grdHeader.setNumColumns(cv.keySet().size());
        ArrayAdapter<String> headerAdapter = (ArrayAdapter<String>)grdHeader.getAdapter();
        headerAdapter.clear();
        headerAdapter.addAll(cv.keySet());  //TODO not if sure order is correct since it is a set
        headerAdapter.notifyDataSetChanged();
        grdHeader.invalidateViews();

        ArrayAdapter<String> resultAdapter = (ArrayAdapter<String>)grdResult.getAdapter();
        grdResult.setNumColumns(cv.keySet().size());
        List<String> lst = new LinkedList<>();
        for(ContentValues cvEntry : result)
            for(Map.Entry<String, Object> e : cvEntry.valueSet())
                lst.add(e.getValue().toString());
        resultAdapter.clear();
        resultAdapter.addAll(lst);
        resultAdapter.notifyDataSetChanged();
        grdResult.invalidateViews();
    }
}
