package ac.arial.liortamir.restmyorder.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import ac.arial.liortamir.restmyorder.R;
import ac.arial.liortamir.restmyorder.Restaurant;
import ac.arial.liortamir.restmyorder.entity.OrderItem;
import ac.arial.liortamir.restmyorder.persistence.DataHandler;

public class OrderListAdapter extends AdapterDataSet<OrderItem> {

    protected Context mContext = null;
    protected LayoutInflater inflater = null;
    protected List<OrderItem> dataList = null;
    protected FragmentActivity activity = null;
    protected Fragment instance;

    protected DataHandler dataHandler = DataHandler.getInstance();

    public OrderListAdapter(Context ctx, FragmentActivity activity, Fragment instance) {
        this.mContext = ctx;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
        this.instance = instance;
    }

    @Override
    public void setData(List<OrderItem> data) {
        this.dataList = data;
    }

    @Override
    public List<OrderItem> getData() {
        return this.dataList;
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
        final OrderListRowView row;
        if (convertView == null) {
            if(position >= dataList.size()) return convertView;
            row = new OrderListRowView();
            convertView = inflater.inflate(R.layout.dish_list_data, null);
            row.rowSit = convertView.findViewById(R.id.kitchenFragDataSit);
            row.rowDish = convertView.findViewById(R.id.kitchenFragDataDish);
            row.rowPrice = convertView.findViewById(R.id.kitchenFragDataPrice);
            row.rowComments = convertView.findViewById(R.id.kitchenFragDataComment);
            row.isReady = convertView.findViewById(R.id.kitchenFragDataIsReady);
            row.isServed = convertView.findViewById(R.id.kitchenFragDataIsServed);
            convertView.setTag(row);
        } else row = (OrderListRowView) convertView.getTag();
        row.rowSit.setId(position);
        row.rowDish.setId(position);
        row.rowPrice.setId(position);
        row.isReady.setId(position);
        row.isServed.setId(position);
        row.rowComments.setId(position);
        final OrderItem orderItem = dataList.get(position);
        row.setData(String.valueOf(orderItem.getSitNum()),
                orderItem.getDish().getDishName(),
                String.format("%6.2f", orderItem.getDish().getDishPrice()),
                orderItem.getComments(),
                orderItem.isReady(), orderItem.isServed(), position);

        return convertView;
    }

    protected class OrderListRowView {
        int position;
        TextView rowSit;
        TextView rowDish;
        TextView rowPrice;
        TextView rowComments;
        CheckBox isReady;
        CheckBox isServed;

        public void setData(String rowSit, String rowDish, String rowPrice, String comments, boolean isReady, boolean isServed, int position) {
            this.rowSit.setText(rowSit);
            this.rowDish.setText(rowDish);
            this.rowPrice.setText(rowPrice);
            this.rowComments.setText(comments);
            this.isReady.setChecked(isReady);
            this.isServed.setChecked(isServed);
            this.position = position;
        }
    }
}
