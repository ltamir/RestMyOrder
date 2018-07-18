package ac.arial.liortamir.restmyorder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import ac.arial.liortamir.restmyorder.entity.User;
import ac.arial.liortamir.restmyorder.persistence.DataHandler;


public class MainActivity extends AppCompatActivity {

//    @SuppressLint()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataHandler dataHandler = DataHandler.getInstance(this);
        final EditText userEmail = findViewById(R.id.txtUserName);
        final EditText userPassword = findViewById(R.id.txtPassword);
        userEmail.setText("lior.tamir@gmail.com");
        userPassword.setText("12345");

        ArrayAdapter<User> userAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dataHandler.getUserList());
        final ListView lstUsers = findViewById(R.id.lstUsers);
        lstUsers.setAdapter(userAdapter);
        lstUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<User> userAdapter = (ArrayAdapter<User>)lstUsers.getAdapter();
                User user = userAdapter.getItem(position);
                if(user == null) return;
                userEmail.setText(user.getEmail());
                userPassword.setText(user.getPassword());
            }
        });
    }

    public void login(View view){
        DataHandler dataHandler = DataHandler.getInstance(this);
        EditText userEmail = findViewById(R.id.txtUserName);
        EditText userPassword = findViewById(R.id.txtPassword);
        if(userEmail.getText().length() < 8){
            Toast.makeText(this, getString(R.string.user_invalid_name), Toast.LENGTH_LONG).show();
            return;
        }
        if(userPassword.getText().length() < 3){
            Toast.makeText(this, getString(R.string.user_invalid_password), Toast.LENGTH_LONG).show();
            return;
        }

        User user = dataHandler.authenticateUser(userEmail.getText().toString(),
                userPassword.getText().toString());
        if(user == null){
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(this,Restaurant.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

}
