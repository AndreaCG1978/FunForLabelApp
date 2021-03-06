package com.boxico.android.kn.funforlabelapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.boxico.android.kn.funforlabelapp.R;
import com.boxico.android.kn.funforlabelapp.dtos.Product;

import java.util.List;


public class KNCustomBackgroundProductAdapter extends ArrayAdapter<Product> {

	final Context myContext;


	public KNCustomBackgroundProductAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Product> objects) {
		super(context, resource, textViewResourceId, objects);
		this.myContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final View view = super.getView(position, convertView, parent);
		TextView txt = view.findViewById(R.id.rowValor);
		Product p = getItem(position);
		txt.setTextSize(12);
		int srcWidth = p.getImage().getWidth();
		int srcHeight = p.getImage().getHeight();
		int dstWidth = (int)(srcWidth*0.70f);
		int dstHeight = (int)(srcHeight*0.70f);
	//	Bitmap dstBitmap = Bitmap.createScaledBitmap(srcBitmap, dstWidth, dstHeight, true);
		Bitmap b =Bitmap.createScaledBitmap(p.getImage(), dstWidth,dstHeight, false);
	//	Bitmap b =Bitmap.createScaledBitmap
		BitmapDrawable icon = new BitmapDrawable(myContext.getResources(), b);
		txt.setCompoundDrawablesWithIntrinsicBounds(icon, null, null,null);
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
		TextView txt = view.findViewById(R.id.rowValor);
		Product p = getItem(position);
		int srcWidth = p.getImage().getWidth();
		int srcHeight = p.getImage().getHeight();
        //	Bitmap dstBitmap = Bitmap.createScaledBitmap(srcBitmap, dstWidth, dstHeight, true);
		Bitmap b =Bitmap.createScaledBitmap(p.getImage(), srcWidth, srcHeight, false);
		BitmapDrawable icon = new BitmapDrawable(myContext.getResources(), b);
		txt.setCompoundDrawablesWithIntrinsicBounds(icon, null, null,null);
		txt.setTextSize(14);
		GradientDrawable border = new GradientDrawable();
		border.setColor(Color.TRANSPARENT); //white background
		border.setStroke(2, Color.DKGRAY); //black border with full opacity
		//linearTag.

		view.setBackground(border);
		view.setPadding(10,10,10,10);
		ImageView img = view.findViewById(R.id.imgChecked);
		if(p.isChecked()){
			img.setVisibility(View.VISIBLE);
		}else{
			img.setVisibility(View.GONE);
		}
		return view;
	}
}
