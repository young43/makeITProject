package kr.ac.kookmin.makeit;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ListViewProjectAdapter extends ArrayAdapter {

    private ArrayList<ListItemProject> listViewItemList = new ArrayList<ListItemProject>() ;

    public ListViewProjectAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }


    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listproject_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView titleTextView = (TextView) convertView.findViewById(R.id.text_project_title);
        TextView regionTextView = (TextView) convertView.findViewById(R.id.text_project_region);
        TextView contentTextView = (TextView) convertView.findViewById(R.id.text_content);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.text_date);
        TextView memberCntTextView = (TextView) convertView.findViewById(R.id.text_memberCnt);



        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final ListItemProject listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        titleTextView.setText(listViewItem.getTitle());
        regionTextView.setText(listViewItem.getRegion());
        contentTextView.setText(listViewItem.getContent());
        dateTextView.setText(listViewItem.getTimestamp().toString());
        memberCntTextView.setText(listViewItem.getMemberCnt()+"");  // Integer -> String형변환

//        titleTextView.setText(listViewItem.getTitle());
//        descTextView.setText(listViewItem.getDesc());
//        checkBox.setChecked(listViewItem.isChecked());



        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Drawable icon, String title, String desc) {
        ListItemProject item = new ListItemProject();

//        item.setIcon(icon);
//        item.setTitle(title);
//        item.setDesc(desc);
//        item.setChecked(false);

        listViewItemList.add(item);
    }

    public void addAll(ArrayList<ListItemProject> items) {
        listViewItemList.addAll(items);
        // listViewItemList.addAll(Arrays.asList(items));
        // super.addAll(items);
    }

    public void setAll(ArrayList<ListItemProject> items){
        listViewItemList = (ArrayList<ListItemProject>)items.clone();
    }

    public void removeItem(int position){
        Log.e("testActivity", listViewItemList.get(position).getTitle());
        listViewItemList.remove(position);
    }
}
