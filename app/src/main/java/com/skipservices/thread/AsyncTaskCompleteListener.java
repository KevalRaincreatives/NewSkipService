package com.skipservices.thread;

@SuppressWarnings("hiding")
public interface AsyncTaskCompleteListener<String>  {
	public void onTaskComplete(String result);
}
