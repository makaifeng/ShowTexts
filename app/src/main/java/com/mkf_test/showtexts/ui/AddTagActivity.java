package com.mkf_test.showtexts.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mkf_test.showtexts.BaseActivity;
import com.mkf_test.showtexts.R;
import com.mkf_test.showtexts.db.GreenDaoManager;
import com.mkf_test.showtexts.db.query.SearchColumnTableQuery;
import com.mkf_test.showtexts.db.table.SearchColumnTable;
import com.mkf_test.showtexts.utils.HttpUtil;

import java.util.List;

import butterknife.BindView;


public class AddTagActivity extends BaseActivity {
    @BindView(R.id.text)
    TextView textView;
    @BindView(R.id.et_title)
    EditText et_title;
    @BindView(R.id.et_prev)
    TextView et_prev;
    @BindView(R.id.et_mulu)
    TextView et_mulu;
    @BindView(R.id.et_next)
    TextView et_next;
    @BindView(R.id.et_content)
    TextView et_content;
    @BindView(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_add_tag;
    }

    private void initView() {
        String urlPath = getIntent().getStringExtra("url");
        HttpUtil.sendHttpForBack(urlPath, "gbk", new HttpUtil.httpBack() {
            @Override
            public void back(String data, int responseCode) {
                textView.setText(data.toString());
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_title.getText().toString().trim();
                String prev = et_prev.getText().toString().trim();
                String mulu = et_mulu.getText().toString().trim();
                String next = et_next.getText().toString().trim();
                String content = et_content.getText().toString().trim();
                if (title.equals("")) return;
                if (prev.equals("")) return;
                if (mulu.equals("")) return;
                if (next.equals("")) return;
                if (content.equals("")) return;
                try {
                    SearchColumnTable searchColumnTable = new SearchColumnTable();
                    searchColumnTable.setCodingFormat("gbk");
                    searchColumnTable.setNameColumn(title);
                    searchColumnTable.setPrevColumn(prev);
                    searchColumnTable.setNextColumn(next);
                    searchColumnTable.setMuluColumn(mulu);
                    searchColumnTable.setContentCoulmn(content);
                    SearchColumnTableQuery searchColumnTableQuery = new SearchColumnTableQuery();
                    searchColumnTableQuery.insert(searchColumnTable);

                    List<SearchColumnTable> searchColumnTables = searchColumnTableQuery.findAll();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(GreenDaoManager.KEY_SEARCH_COLUMN, JSON.toJSON(searchColumnTables));
                    Log.e("ssss", "toWeb: " + jsonObject.toJSONString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
    }
}
