package ac.arial.liortamir.restmyorder.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import ac.arial.liortamir.restmyorder.R;
import ac.arial.liortamir.restmyorder.persistence.DataHandler;

public abstract class BaseFragment extends Fragment {

    protected View rootView = null;
    protected DataHandler dataHandler;
    final static String TAG = "BaseFragment";

    protected Spinner createSpinner(int viewId, List dataSource, View rootView){
        Spinner spinner = rootView.findViewById(viewId);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, dataSource);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        return spinner;
    }

    protected void makeToastLong(String message){
        Toast.makeText(this.getContext(), message, Toast.LENGTH_LONG).show();
    }

    protected void makeToastSort(String message){
        Toast.makeText(this.getContext(), message, Toast.LENGTH_LONG).show();
    }

    // ***** date handling ***** //

    protected String getCurrentDate(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String date = String.format("%02d/%02d/%d",day, month+1, year);

        return date;
    }

    protected String dateDisplayToSql(String date){
        final String[] arr = date.split("/");
        final String sqlDate = arr[2] + "-" + arr[1] + "-" + arr[0];
        return sqlDate;
    }
    protected String dateSqltoDisplay(String date){
        final String[] dateTime = date.split(" ");
        StringBuilder result = new StringBuilder();
        switch(dateTime.length){
            case 2:
                result.append(" ");
                result.append(dateTime[1]);
            case 1:
                final String[] arr = dateTime[0].split("-");
                final String sqlDate = arr[2] + "/" + arr[1] + "/" + arr[0];
                result.insert(0,sqlDate);
        }

        return result.toString();
    }

    // ***** save and restore state ***** //

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveFragmentState(outState);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState == null)return;
        loadFragmentState(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
//        getActivity().setTitle(getResources().getString(R.string.app_name) + " - " + dataHandler.getActiveUser().getFullName());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && rootView != null) {
            updateFragmentState();
        } else {

        }
    }
    protected abstract void saveFragmentState(@NonNull Bundle bundle);
    protected abstract void loadFragmentState(@NonNull Bundle bundle);
    protected abstract void updateFragmentState();

    protected void saveSpinnerState(int resourceId, Bundle bundle){
        Spinner spn = rootView.findViewById(resourceId);
        int pos = spn.getSelectedItemPosition();
        bundle.putInt(String.valueOf(resourceId), pos);
    }

    protected void saveEditTextState(int resourceId, Bundle bundle){
        EditText editText = rootView.findViewById(resourceId);

        String data = editText.getText().toString();
        Log.d(TAG, "saveEditTextState: " + String.valueOf(resourceId) + ":" + data);
        bundle.putString(editText.getResources().toString(), data);
    }

    protected void loadSpinnerState(int resourceId, Bundle bundle){
        if(rootView == null) return;
        Spinner spn = rootView.findViewById(resourceId);
        Integer pos = bundle.getInt(String.valueOf(resourceId));
        if(pos == 0)
            return;
        Log.d(TAG, "loadSpinnerState: " + String.valueOf(resourceId) + ":" + pos);
        spn.setSelection(pos);
    }

    protected void loadEditTextState(int resourceId, Bundle bundle) {
        EditText editText = rootView.findViewById(resourceId);
        String data = bundle.getString(editText.getResources().toString());
        Log.d(TAG, "loadEditTextState: " + String.valueOf(resourceId) + ":" + data);
        editText.setText(data);
    }

    public static void hideSoftKeyboard(Activity activity) {
        if(activity.getCurrentFocus() == null)return;
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
