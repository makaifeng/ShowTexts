package com.mkf_test.showtexts.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewHolder
{
  private View mConvertView;
  private final SparseArray<View> mViews = new SparseArray<View>();

  private ViewHolder(Context context, ViewGroup group, int resId, int position)
  {
    this.mConvertView = LayoutInflater.from(context).inflate(resId, group, false);
//    System.out.println(this.mConvertView + "----");
    this.mConvertView.setTag(this);
  }

  public static ViewHolder get(Context context, View v, ViewGroup group, int resId, int position)
  {
    if (v == null)
      return new ViewHolder(context, group, resId, position);
    return (ViewHolder)v.getTag();
  }

  public View getConvertView()
  {
    return this.mConvertView;
  }

  public <T extends View> View getView(int resId)
  {
    View view = this.mViews.get(resId);
    if (view == null)
    {
    	view = this.mConvertView.findViewById(resId);
      this.mViews.put(resId, view);
    }
		return view;
  }
}
