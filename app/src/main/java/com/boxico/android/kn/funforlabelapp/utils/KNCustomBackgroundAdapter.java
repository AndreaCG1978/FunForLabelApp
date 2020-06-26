package com.boxico.android.kn.funforlabelapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.boxico.android.kn.funforlabelapp.R;
import com.boxico.android.kn.funforlabelapp.dtos.LabelImage;


public class KNCustomBackgroundAdapter extends ArrayAdapter<LabelImage> {

	Context myContext;


	public KNCustomBackgroundAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull LabelImage[] objects) {
		super(context, resource, textViewResourceId, objects);
		this.myContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final View view = super.getView(position, convertView, parent);
		TextView txt = view.findViewById(R.id.rowValor);
		LabelImage li = (LabelImage) getItem(position);
		Bitmap b =Bitmap.createScaledBitmap(li.getImage(), 100,100, false);
		BitmapDrawable icon = new BitmapDrawable(myContext.getResources(), b);
		txt.setCompoundDrawablesWithIntrinsicBounds(null, null, icon,null);
		return view;
	}
}
