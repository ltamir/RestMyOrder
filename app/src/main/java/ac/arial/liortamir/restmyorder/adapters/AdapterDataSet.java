package ac.arial.liortamir.restmyorder.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class AdapterDataSet <E>extends BaseAdapter {

    public AdapterDataSet(){}
    public AdapterDataSet(Context ctx, FragmentActivity activity, Fragment instance){

    }

    public AdapterDataSet(Context ctx, FragmentActivity activity, Fragment instance, List<E> dataList){

    }
    public abstract void setData(List<E> data);
    public abstract List<E> getData( );
}
