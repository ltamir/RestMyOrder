package ac.arial.liortamir.restmyorder.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SQLiteAdapter extends AdapterDataSet<ContentValues> {
    private Context mContext = null;
    private List<String> headerList = new ArrayList<>();
    private List<ContentValues> dataList = null;
    private LayoutInflater inflater = null;

    public SQLiteAdapter(Context ctx){
        this.mContext = ctx;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void setData(List<ContentValues> data){
        this.dataList = data;
        if(getCount() == 0)
            return;
        ContentValues cv = dataList.get(0); //create ordered list from the set
        headerList.clear();
        for(String s : cv.keySet())
            headerList.add(s);
    }
    @Override
    public List<ContentValues> getData(){return this.dataList;}
    public int getCount() { return dataList == null?0:dataList.size(); }
    public Object getItem(int position) {
        int recPos = position / headerList.size();
        ContentValues cv = dataList.get(recPos);
        int fieldPos = position % dataList.get(0).size();
        return cv.get(headerList.get(fieldPos));
    }
    public long getItemId(int position) { return dataList == null?0:dataList.size()*headerList.size();}
    public View getView(int position, View convertView, ViewGroup parent) {
        GridLayout row = null;
        TextView cell = null;
        StringBuilder rowData = new StringBuilder();

        cell = new TextView(mContext);
        cell.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT, GridView.LayoutParams.WRAP_CONTENT));
        cell.setId(position);
        cell.setBackgroundColor(Color.LTGRAY);

        int recPos = position / headerList.size();
        ContentValues cv = dataList.get(recPos);
        int fieldPos = position % dataList.get(0).size();

        int idGenerator = 100*position;
        cell.setText(cv.getAsString(headerList.get(fieldPos)));
        cell.setTag(cv);
        cell.setId(idGenerator++);
        cell.setPadding(5, 0, 5, 0);

        return cell;
    }
}
