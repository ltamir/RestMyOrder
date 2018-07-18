package ac.arial.liortamir.restmyorder.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ac.arial.liortamir.restmyorder.Restaurant;
import ac.arial.liortamir.restmyorder.entity.OrderItem;
import ac.arial.liortamir.restmyorder.fragment.KitchenFragment;
import ac.arial.liortamir.restmyorder.fragment.OrderFragment;

public class KitchenAdapter extends OrderListAdapter {
    public KitchenAdapter(Context ctx, FragmentActivity activity, Fragment instance) {
        super(ctx, activity, instance);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= super.getView(position, convertView, parent);

        final OrderListRowView row = (OrderListRowView) convertView.getTag();
        final OrderItem orderItem = dataList.get(position);

        row.rowDish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ((KitchenFragment)instance).showWaiter(orderItem);
            }
        });
        row.rowSit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ((KitchenFragment)instance).showWaiter(orderItem);
            }
        });
        row.rowComments.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ((KitchenFragment)instance).showWaiter(orderItem);
            }
        });

//        row.isReady.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                orderItem.setReady(row.isReady.isChecked());
//                dataHandler.updateOrderItem(orderItem);
//            }
//        });
        row.isReady.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(orderItem.isServed()){
                        Toast.makeText(v.getContext(), "Cannot change since dish was served\nRemove the Served indicator first", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    orderItem.setReady(!orderItem.isReady());
                    orderItem.setChef(dataHandler.getActiveUser());
                    dataHandler.updateOrderItem(orderItem);
                }
                return false;
            }
        });
        row.isServed.setEnabled(false);
        return convertView;
    }
}
