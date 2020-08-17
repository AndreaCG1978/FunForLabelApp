package com.boxico.android.kn.funforlabelapp.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.boxico.android.kn.funforlabelapp.dtos.ProductoCarrito;


public class KNCustomCarritoAdapter extends ArrayAdapter<ProductoCarrito> {

	Context myContext;


	public KNCustomCarritoAdapter(@NonNull Context context, int resource, @NonNull ProductoCarrito[] objects) {
		super(context, resource, objects);
		this.myContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final View view = super.getView(position, convertView, parent);
		return view;
	}


	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		/*if (null == convertView) {
			LayoutInflater inflater = (LayoutInflater) localContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.stagerow, null);
		}
*/
		final View view = super.getView(position, convertView, parent);
		ProductoCarrito pc = getItem(position);
		/*
		int srcWidth = li.getImage().getWidth();
		int srcHeight = li.getImage().getHeight();
		int dstWidth = (int)(srcWidth*0.50f);
		int dstHeight = (int)(srcHeight*0.50f);

		Bitmap b =Bitmap.createScaledBitmap(li.getImage(), dstWidth,dstHeight, false);
		BitmapDrawable icon = new BitmapDrawable(myContext.getResources(), b);
		txt.setCompoundDrawablesWithIntrinsicBounds(null, null, icon,null);
		GradientDrawable border = new GradientDrawable();
		border.setColor(Color.TRANSPARENT); //white background
		border.setStroke(2, Color.DKGRAY); //black border with full opacity
		//linearTag.

		view.setBackground(border);
		view.setPadding(10,10,10,10);
		return view;*/
		return view;
	}
}
