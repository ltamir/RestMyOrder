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
import ac.arial.liortamir.restmyorder.fragment.OrderFragment;

public class OrderAdapter extends OrderListAdapter {
    public OrderAdapter(Context ctx, FragmentActivity activity, Fragment instance) {
        super(ctx, activity, instance);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);

        final OrderListRowView row = (OrderListRowView) convertView.getTag();
        final OrderItem orderItem = dataList.get(position);

        row.rowDish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ((OrderFragment)instance).evtOrderFragEditDish(view, orderItem);
            }
        });
        row.rowSit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ((OrderFragment)instance).evtOrderFragEditDish(view, orderItem);
            }
        });
        row.rowComments.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ((OrderFragment)instance).evtOrderFragEditDish(view, orderItem);
            }
        });

        row.isServed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if( !orderItem.isServed() && !orderItem.isReady()){
                    Toast.makeText(view.getContext(), "Cannot be served\nDish is not marked as ready", Toast.LENGTH_SHORT).show();
                    row.isServed.setChecked(false);
                }else{
                    orderItem.setServed(!orderItem.isServed());
                    dataHandler.updateOrderItem(orderItem);
                }
            }
        });

//        row.isServed.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_DOWN){
//                    if(!orderItem.isReady()){
//                        if(!orderItem.isServed())
//                            Toast.makeText(v.getContext(), "Cannot be served\nDish is not marked as ready", Toast.LENGTH_SHORT).show();
//                        return true;
//                    }
//                    orderItem.setServed(!orderItem.isServed());
//                    dataHandler.updateOrderItem(orderItem);
//                }
//                return false;
//            }
//        });
        row.isReady.setEnabled(false);
        return convertView;
    }
}
