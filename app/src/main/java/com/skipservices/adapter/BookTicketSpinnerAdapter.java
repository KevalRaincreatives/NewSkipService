package com.skipservices.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.skipservices.R;
import com.skipservices.utils.WebService;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BookTicketSpinnerAdapter extends BaseAdapter
{

	
	Context context;
	ArrayList<HashMap<String, String>>arrayList = new ArrayList<HashMap<String,String>>();
	LayoutInflater layoutInflater;
	
	public BookTicketSpinnerAdapter(Context context,
			ArrayList<HashMap<String, String>> arrayList) {
		super();
		this.context = context;
		this.arrayList = arrayList;
		layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	static class ViewHolderSpinner
	{
		TextView txt_title;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		try
		{
			ViewHolderSpinner holderSpinner = new ViewHolderSpinner();
			convertView = layoutInflater.inflate(R.layout.adapter_dialogbook, null);
			holderSpinner.txt_title = (TextView)convertView.findViewById(R.id.txt_dataname);
			
			HashMap<String, String>map = arrayList.get(position);
			holderSpinner.txt_title.setText(map.get(WebService.KEY_TASKDETAIL_BOONUMBER));
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		
		
		return convertView;
	}

	
	
}
