package com.mkf_test.showtexts;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mkf_test.showtexts.db.WebUrl;
import com.mkf_test.showtexts.utils.Dbutils;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

@ContentView(R.layout.activity_list)
public class ListActivity extends BaseActivity {
    @ViewInject(R.id.list)
    private ListView listView;
    List<WebUrl> webUrllist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        setTitle("书签");

        try {
            webUrllist = Dbutils.getInstance().dbGetList();
            if (webUrllist!=null)
            listView.setAdapter(new MyAdapter(this,webUrllist));
        } catch (DbException e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openUrl(i);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, long id) {
                new AlertDialog.Builder(ListActivity.this)
                        .setItems(R.array.longclickmsg,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        switch (which){
                                            case 0://打开
                                                openUrl(position);
                                                break;
                                            case 1://复制网址
                                                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                                cm. setPrimaryClip(ClipData.newPlainText(null, webUrllist.get(position).getUrl()));
                                                break;
                                            case 2://删除
                                                delectUrl(position);
                                                parent.removeView(view);
                                                break;
                                        }

                                    }
                                })
                        .show();
                return true;
            }
        });

    }
    private  void openUrl(int position){
        if (webUrllist!=null) {
            setResult(RESULT_OK, getIntent().putExtra("url", webUrllist.get(position).getUrl()));
            finish();
        }
    }
    private  void delectUrl(int position){
        if (webUrllist!=null) {
            try {
                Dbutils.getInstance().dbDelete(webUrllist.get(position));
                webUrllist.remove(position);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    class MyAdapter extends BaseAdapter {
        Context context; List<WebUrl> webUrllist;
        private MyAdapter(Context context, List<WebUrl> webUrllist){
            this.context=context;
            this.webUrllist=webUrllist;
        }
        @Override

        public int getCount() {
            return webUrllist.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup group) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                       R.layout.adapter_item_text, group, false);
                holder.name= (TextView) convertView.findViewById(R.id.text1);
                holder.name.setTextColor(getResources().getColor(R.color.black));
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.setPosition(position);
            return convertView;
        }

        class ViewHolder {
            TextView name;

            public void setPosition(final int position) {
                name.setText(webUrllist.get(position).getName());
            }
        }
    }
}
