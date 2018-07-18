package ac.arial.liortamir.restmyorder.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ac.arial.liortamir.restmyorder.R;
import ac.arial.liortamir.restmyorder.adapters.AdapterDataSet;
import ac.arial.liortamir.restmyorder.adapters.UserAdapter;
import ac.arial.liortamir.restmyorder.entity.Role;
import ac.arial.liortamir.restmyorder.entity.User;
import ac.arial.liortamir.restmyorder.persistence.DataHandler;

public class UserFragment extends BaseFragment {
    private final static int USER_ACTION_INSERT = 0;
    private final static int USER_ACTION_UPDATE = 1;
    private GridView grd;
    private Spinner spnRole;
    private EditText txtFullName;
    private EditText txtEmail;
    private EditText txtPassword;

    private User user;
    private int userAction = USER_ACTION_INSERT;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user, container, false);
        dataHandler = DataHandler.getInstance();
        createUserFragment();
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

    private void createUserFragment(){
        List<Role> roleList = dataHandler.getRoles();
        spnRole = createSpinner(R.id.userFrag_cmb_role, roleList, rootView);
        txtFullName = rootView.findViewById(R.id.userFrag_txt_fulName);
        txtEmail = rootView.findViewById(R.id.userFrag_txt_email);
        txtPassword = rootView.findViewById(R.id.userFrag_txt_password);

        grd = rootView.findViewById(R.id.userFrag_grdDishList);
        grd.setAdapter(new UserAdapter(rootView.getContext(),getActivity(), this, dataHandler.getUserList()));

        Button button = rootView.findViewById(R.id.userFrag_cmd_newUser);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                evtUserFragNewUser();
            }
        });

        button = rootView.findViewById(R.id.userFrag_cmd_handleUser);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                evtUserFragSaveUser();
            }
        });
    }

    private void refreshGrid(){
        AdapterDataSet adapter = (AdapterDataSet)grd.getAdapter();
        adapter.setData(dataHandler.getUserList());
        adapter.notifyDataSetChanged();
        grd.invalidateViews();
    }

    // ***** Events ***** //

    public void evtUserFragEditUser(View view, User user){
        txtFullName.setText(user.getFullName(),TextView.BufferType.EDITABLE );
        txtEmail.setText(user.getEmail(), TextView.BufferType.EDITABLE);
        txtPassword.setText(user.getPassword(), TextView.BufferType.EDITABLE);

        ArrayAdapter<Role> roleAdapter = (ArrayAdapter<Role>)spnRole.getAdapter();
        int roleItemPos = roleAdapter.getPosition(user.getRole());
        spnRole.setSelection(roleItemPos);
        this.user = user;
        userAction = USER_ACTION_UPDATE;
    }

    private void evtUserFragNewUser(){
        this.user = null;
        txtFullName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        userAction = USER_ACTION_INSERT;
    }

    private void evtUserFragSaveUser(){
        if(txtFullName.getText().length() < 4){
            makeToastLong(getString(R.string.user_invalid_name));
            return;
        }
        if(txtEmail.getText().length() < 8){
            makeToastLong(getString(R.string.user_invalid_email));
            return;
        }
        if(txtPassword.getText().length() < 3){
            makeToastLong(getString(R.string.user_invalid_password));
            return;
        }
        switch(userAction) {
            case USER_ACTION_INSERT:
                User newUser = new User(null,
                        txtFullName.getText().toString(),
                        txtEmail.getText().toString(),
                        txtPassword.getText().toString(),
                        (Role)spnRole.getSelectedItem());
                dataHandler.insertUser(user);
                Toast.makeText(getActivity(), getResources().getString(R.string.userFrag_handleUserResult), Toast.LENGTH_LONG).show();
                break;
            case USER_ACTION_UPDATE:
                user.setFullName(txtFullName.getText().toString());
                user.setEmail(txtEmail.getText().toString());
                user.setPassword(txtPassword.getText().toString());
                user.setRole((Role)spnRole.getSelectedItem());

                String result = dataHandler.updateUser(user);
                if(result.equals("OK"))
                    Toast.makeText(getActivity(), getResources().getString(R.string.userFrag_handleUserResult), Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                break;
        }

        refreshGrid();  //TODO consider update Adapter's list
    }

}

