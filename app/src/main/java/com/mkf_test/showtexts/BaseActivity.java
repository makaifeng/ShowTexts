package com.mkf_test.showtexts;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Created by mkf on 2016/11/14.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected ProgressDialog progressDialog;
    public LinearLayout headlayout;
    public FrameLayout layout_main;
    public RelativeLayout layout_title;
    public ImageView extraRightTv;
    private View.OnClickListener NavigationClickListener;
    ImageView backIv;
    //窗口的宽度
    int screenWidth;
    //窗口高度
    int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContentView();
        setContentView(getLayoutResId());
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //窗口的宽度
        screenWidth = dm.widthPixels;

        //窗口高度
        screenHeight = dm.heightPixels;
    }

    protected void beforeSetContentView() {
    }

    public abstract @LayoutRes
    int getLayoutResId();

    @Override
    public void setContentView(int layoutResID) {
        headlayout = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.head, null);
        layout_main = (FrameLayout) headlayout.findViewById(R.id.layout_main);
        layout_title = (RelativeLayout) headlayout.findViewById(R.id.titleLayout);
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        layout_main.addView(view);
        setContentView(headlayout);

        backIv = (ImageView) findViewById(R.id.backIv);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NavigationClickListener != null) NavigationClickListener.onClick(v);
                finish();
            }
        });
        extraRightTv = (ImageView) findViewById(R.id.extraRightTv);
//        actionmenuview点击事件
//        extraRightTv.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Toast.makeText(MainActivity.this,"actionmenuview的点击事件",Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });

    }

    public void setTitleBarVisibility(int visibility) {
        layout_title.setVisibility(visibility);
    }

    public void setNavigationMenu(boolean isShow, @MenuRes int menuResId) {
        if (isShow) {
            extraRightTv.setVisibility(View.VISIBLE);
            extraRightTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(BaseActivity.this, v);
                    popupMenu.inflate(menuResId);

                    //设置选中
//            popupMenu.menu.findItem(checkedItemId).isChecked = true
                    //菜单项的监听
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            onOptionsItemSelected(item);
                            return true;
                        }
                    });
//            //使用反射，强制显示菜单图标
//            try {
//                val field = popupMenu.getClass().getDeclaredField("mPopup")
//                field.setAccessible(true)
//                val mHelper = field.get(popupMenu) as MenuPopupHelper
//                mHelper.setForceShowIcon(true)
//            } catch (e: IllegalAccessException) {
//                e.printStackTrace()
//            } catch (e: NoSuchFieldException) {
//                e.printStackTrace()
//            }


                    //显示PopupMenu
                    popupMenu.show();
                }
            });
        } else {
            extraRightTv.setVisibility(View.GONE);
        }
    }

    public void setNavigationBackShowed(boolean isNavigationShow) {
        setNavigationBackShowedAndClickable(isNavigationShow, v -> finish());
    }

    public void setNavigationBackShowedAndClickable(boolean isNavigationShow, View.OnClickListener onClickListener) {
        this.NavigationClickListener = onClickListener;
        if (isNavigationShow) {
            backIv.setVisibility(View.VISIBLE);
            backIv.setOnClickListener(NavigationClickListener);
        } else {
            backIv.setVisibility(View.GONE);
        }
    }


    public void showProgressDialog(String loadingTxt) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            if (!TextUtils.isEmpty(loadingTxt)) {
                progressDialog.setMessage(loadingTxt);
            }
            progressDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
//        mAutoHideProgressBar = true;
    }

}

