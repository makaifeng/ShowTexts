package com.mkf_test.showtexts;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @ViewInject(R.id.editText)
    private EditText edittext;
    @ViewInject(R.id.button2)
    private Button showLastUrl;
    String lastUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
//        setContentView(R.layout.activity_main);
//        String url="http://m.biquge.com/13_13453/7436940.html";
//        String url="http://m.greattone.net/app/music.php?classid=13&id=666";
//        String url="http://www.greattone.net/test/demo.html";
//        edittext.setText(url);
        lastUrl=getSharedPreferences(getPackageName(),MODE_PRIVATE).getString("url","");
        if(!TextUtils.isEmpty(lastUrl)){
            showLastUrl.setVisibility(View.VISIBLE);
        }
    }
    public void toWeb(View v){
        String url=edittext.getText().toString().trim();
        if (TextUtils.isEmpty(url)) return;
        startActivityForResult(new Intent(this,WebActivity.class).putExtra("url",url),1);
    }
    public void showLastUrl(View v){
        startActivityForResult(new Intent(this,WebActivity.class).putExtra("url",lastUrl),1);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            lastUrl=getSharedPreferences(getPackageName(),MODE_PRIVATE).getString("url","");
            if(!TextUtils.isEmpty(lastUrl)){
                showLastUrl.setVisibility(View.VISIBLE);
            }
    }
}
