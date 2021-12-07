package com.skipservices.thread;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.skipservices.utils.RotateAnimation;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("deprecation")
public class AsyncTaskClass extends AsyncTask<Void, Void, String>{

	public static String DEBUG_TAG = "AsyncTaskClass";
	
	
	private AsyncTaskCompleteListener<String> callback;
	
	
	BufferedReader reader = null;
	HttpURLConnection con = null;
	boolean isdialougeshow = false;
	Dialog dialog;
	RotateAnimation rotateAnimation;
	Context context;
	InputStream is = null;
	StringBuffer result = null;
	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	String contentAsString="";
	String url = "";
	
	public AsyncTaskClass(Context context, AsyncTaskCompleteListener<String> cb,List<NameValuePair> nameValuePair, String url, boolean isdialogeshow) {
        this.context = context;
        this.callback = cb;
        this.nameValuePair = nameValuePair;
        this.url = url;
        isdialougeshow = isdialogeshow;
    }

	 @Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
        
		
		if(isdialougeshow==true)
		 {
			 if(rotateAnimation!=null)
			 {
				 if(rotateAnimation.isShowing())
				 {
					 rotateAnimation.dismiss();
					 rotateAnimation = null;
				 }
			 }
			 else
			 {
				 rotateAnimation = new RotateAnimation(context);
				 rotateAnimation.show();
			 }

		 }
	}
	 
	 

		@Override
		protected String  doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// Encoding POST data
			try {
				Log.d("Http Post Response:", "nameValuePair="+nameValuePair.toString());
				
				HttpClient httpClient = new  DefaultHttpClient();

				HttpPost httpPost = new HttpPost(url);

				
			    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		
			HttpResponse response = httpClient.execute(httpPost);
			// write response to log
			Log.d("Http Post Response:", "url="+httpClient.getParams());
			BufferedReader rd = new BufferedReader(
					new InputStreamReader(response.getEntity()
							.getContent()));

			result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);

			}
			contentAsString = result.toString();
			
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				if(rotateAnimation!=null)
				{
					if(rotateAnimation.isShowing())
					{
						rotateAnimation.dismiss();
						rotateAnimation = null;
					}
				}
				/*if(dialog!=null)
				{
					if(dialog.isShowing())
					{
						dialog.dismiss();
					}
				}*/
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(rotateAnimation!=null)
				{
					if(rotateAnimation.isShowing())
					{
						rotateAnimation.dismiss();
						rotateAnimation = null;
					}
				}
				/*if(dialog!=null)
				{
					if(dialog.isShowing())
					{
						dialog.dismiss();
					}
				}*/
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(rotateAnimation!=null)
				{
					if(rotateAnimation.isShowing())
					{
						rotateAnimation.dismiss();
						rotateAnimation = null;
					}
				}
				/*if(dialog!=null)
				{
					if(dialog.isShowing())
					{
						dialog.dismiss();
					}
				}*/
				
			}finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						if(rotateAnimation!=null)
						{
							if(rotateAnimation.isShowing())
							{
								rotateAnimation.dismiss();
								rotateAnimation = null;
							}
						}
						/*if(dialog!=null)
						{
							if(dialog.isShowing())
							{
								dialog.dismiss();
							}
						}*/
						/*if(dialog!=null)
						{
							if(dialog.isShowing())
							{
								dialog.dismiss();
							}
						}*/
					}
				}
				
			}
			
			
			
			return null;
		}

	
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		/*if(dialog!=null)
		{
			if(dialog.isShowing())
			{
				dialog.dismiss();
			}
		}
		*/
        if(rotateAnimation!=null)
        {
            if(rotateAnimation.isShowing())
            {
                rotateAnimation.dismiss();
                rotateAnimation = null;
            }
        }
		callback.onTaskComplete(contentAsString);
			
	}



	/*// Reads an InputStream and converts it to a String.
	public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
	    Reader reader = null;
	    reader = new InputStreamReader(stream, "UTF-8");        
	    char[] buffer = new char[len];
	    reader.read(buffer);
	    return new String(buffer);
	}

*/

	

}
