package com.mkf_test.showtexts.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mkf_test.showtexts.R;

import java.lang.reflect.Field;
import java.util.List;

public abstract class Adapter<T> extends BaseAdapter {
	protected Context context;
	private int mlayout;
	protected List<T> mlist;

	public Adapter(Context context, List<T> list, int resId) {
		this.context = context;
		this.mlist = list;
		this.mlayout = resId;
	}
	public void setList(List<T> mlist){
		this.mlist=mlist;
		notifyDataSetChanged();
	}
	public int getCount() {
		if (this.mlist != null)
			return this.mlist.size();
		return 0;
	}

	public Object getItem(int resId) {
		if (this.mlist != null)
			return this.mlist.get(resId);
		return null;
	}

	public long getItemId(int resId) {
		return resId;
	}

	public List<T> getList() {
		return this.mlist;
	}

	public View getView(int position, View v, ViewGroup group) {
		ViewHolder holder = ViewHolder.get(context, v, group, this.mlayout,
				position);
		getview(holder, position, this.mlist.get(position));
		return holder.getConvertView();
	}

	public abstract void getview(ViewHolder holder, int position, T T);

	protected void initViewholder(View v, Object object) {
		Field[] arrayOfField = object.getClass().getDeclaredFields();
		int i = arrayOfField.length;
		int j = 0;
		while (true) {
			if (j >= i)
				return;
			Field localField = arrayOfField[j];
			localField.setAccessible(true);
			try {
				localField.set(object, v.findViewById(R.id.class.getField(
						localField.getName()).getInt(null)));
				j++;
			} catch (IllegalAccessException localIllegalAccessException) {
				while (true)
					localIllegalAccessException.printStackTrace();
			} catch (IllegalArgumentException localIllegalArgumentException) {
				while (true)
					localIllegalArgumentException.printStackTrace();
			} catch (NoSuchFieldException localNoSuchFieldException) {
				while (true)
					localNoSuchFieldException.printStackTrace();
			}
		}
	}

	static class a {
	}
}
