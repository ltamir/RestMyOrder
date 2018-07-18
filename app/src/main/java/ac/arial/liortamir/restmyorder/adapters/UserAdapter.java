package ac.arial.liortamir.restmyorder.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ac.arial.liortamir.restmyorder.R;
import ac.arial.liortamir.restmyorder.entity.User;
import ac.arial.liortamir.restmyorder.fragment.UserFragment;
import ac.arial.liortamir.restmyorder.persistence.DataHandler;

public class UserAdapter extends AdapterDataSet<User> {
    protected Context mContext = null;
    protected LayoutInflater inflater = null;
    protected List<User> dataList = null;
    protected FragmentActivity activity = null;
    protected Fragment instance;

    protected DataHandler dataHandler = DataHandler.getInstance();

    public UserAdapter(Context ctx, FragmentActivity activity, Fragment instance, List<User> dataList) {
        this.mContext = ctx;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
        this.instance = instance;
        this.dataList = dataList;
    }

    @Override
    public void setData(List<User> data) {
        this.dataList = data;
    }

    @Override
    public List<User> getData() {
        return this.dataList;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public Object getItem(int position) {
        return dataList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final UserRowView row;
        if (convertView == null) {
            row = new UserRowView();
            convertView = inflater.inflate(R.layout.user_list_data, null);
            row.fullName = (TextView) convertView.findViewById(R.id.userDataFrag_lbl_fullName);
            row.role = (TextView) convertView.findViewById(R.id.userDataFrag_lbl_role);

            convertView.setTag(row);
        } else row = (UserRowView) convertView.getTag();

        row.fullName.setId(position);
        row.role.setId(position);

        final User user = dataList.get(position);
//        row.fullName.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                ((UserFragment)instance).evtUserFragEditUser(view, user);
//            }
//        });

        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ((UserFragment)instance).evtUserFragEditUser(view, user);
            }
        });

        row.setData(user.getFullName(),
                user.getRole().getRoleName(),
                position);

        return convertView;
    }

    protected class UserRowView {
        int position;
        TextView fullName;
        TextView role;


        public void setData(String fullName, String role, int position) {
            this.fullName.setText(fullName);
            this.role.setText(role);
            this.position = position;
        }
    }
}
