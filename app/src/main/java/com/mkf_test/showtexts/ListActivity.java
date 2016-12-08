package com.mkf_test.showtexts;

import android.content.Context;
import android.os.Bundle;
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
                if (webUrllist!=null) {
                    setResult(RESULT_OK, getIntent().putExtra("url", webUrllist.get(i).getUrl()));
                    finish();
                }
            }
        });
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
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.setPosition(position);
            return convertView;
        }

        class ViewHolder {
            TextView name;

            public void setPosition(int position) {
                name.setText(webUrllist.get(position).getName());
            }
        }
    }
}
