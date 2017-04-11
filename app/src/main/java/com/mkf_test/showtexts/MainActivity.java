package com.mkf_test.showtexts;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

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
//        setContentView(R.layout.activity_main);
        String url="http://m.6mao.com/wapbook/4025_9559214.html";
//        String url="http://m.greattone.net/app/music.php?classid=13&id=666";
//        String url="";
        edittext.setText(url);
        lastUrl=getSharedPreferences(getPackageName(),MODE_PRIVATE).getString("url","");
        if(!TextUtils.isEmpty(lastUrl)){
            showLastUrl.setVisibility(View.VISIBLE);
        }
//        setSupportActionBar(toolbar);
    }


    public void toWeb(View v){
        String url=edittext.getText().toString().trim();
        if (TextUtils.isEmpty(url)) return;
        startActivityForResult(new Intent(this,WebActivity.class).putExtra("url",url),1);
    }
    public void tomyview(View v){
        String url=edittext.getText().toString().trim();
        if (TextUtils.isEmpty(url)) return;
        startActivityForResult(new Intent(this,ShowTextActivity.class).putExtra("url",url),1);
    }
    public void showLastUrl(View v){
        startActivityForResult(new Intent(this,WebActivity.class).putExtra("url",lastUrl),1);

    }
    public void viewBookMarks(View v){
        startActivityForResult(new Intent(this,ListActivity.class),2);
    }
//@Override
//public boolean onCreateOptionsMenu(Menu menu) {
//    // Inflate the menu; this adds items to the action bar if it is present.
//    getMenuInflater().inflate(R.menu.menu_main, menu);
//    return true;
//}

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.test) {
//            String url=edittext.getText().toString().trim();
//            startActivityForResult(new Intent(this,ShowTextActivity.class).putExtra("url",url),3);
//            return true;
//        }
//
//
//        return super.onOptionsItemSelected(item);
//    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==RESULT_OK) {
            lastUrl = getSharedPreferences(getPackageName(), MODE_PRIVATE).getString("url", "");
            if (!TextUtils.isEmpty(lastUrl)) {
                showLastUrl.setVisibility(View.VISIBLE);
            }
        }else if (requestCode==2&&resultCode==RESULT_OK){
            String url=data.getStringExtra("url");
            startActivityForResult(new Intent(this,WebActivity.class).putExtra("url",url),1);
        }
    }
}
