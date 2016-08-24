package com.developer.volley;

import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.developer.volley.bean.YuleBean;
import com.developer.volley.bean.YuleBean.Result.Data;
import com.google.gson.Gson;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private RequestQueue mQueue;
	private YuleBean yuleBean;
	private ListView volley_list;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			initView();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initData();
	}

	private void initData() {

		mQueue = Volley.newRequestQueue(this);

		String requestUrl = "http://v.juhe.cn/toutiao/index";
		String type = "yule";
		String key = "12e09f1041415ea93baf19481bd47405";
		String mUrl = requestUrl + "?type=" + type + "&key=" + key;
		JsonObjectRequest request = new JsonObjectRequest(Method.GET, mUrl,
				null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						backResponse(response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getApplicationContext(), "∑√Œ Õ¯¬Á ß∞‹...", Toast.LENGTH_SHORT).show();
					}
				});
		mQueue.add(request);
	}

	protected void backResponse(JSONObject response) {
		Gson gson = new Gson();
		yuleBean = gson.fromJson(response.toString(), YuleBean.class);
		mHandler.sendEmptyMessage(0);
	}

	protected void initView() {
		volley_list = (ListView) findViewById(R.id.volley_list);
		MListAdapter mListAdapter = new MListAdapter(yuleBean);
		volley_list.setAdapter(mListAdapter);

	}

	class MListAdapter extends BaseAdapter {

		private YuleBean yuleBean;
		private ImageLoader mImageLoader;

		public MListAdapter(YuleBean yuleBean) {
			this.yuleBean = yuleBean;

			mImageLoader = new ImageLoader(mQueue, new BitmapCache());
		}

		@Override
		public int getCount() {
			return yuleBean.result.data.size();
		}

		@Override
		public Object getItem(int position) {
			return yuleBean.result.data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			YuleHolder yuleHolder = null;
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(), R.layout.item_yule, null);
				yuleHolder = new YuleHolder();
				yuleHolder.thumbnail_pic_s = (NetworkImageView) convertView.findViewById(R.id.niv_yule_thumbnail_pic_s);
				yuleHolder.title = (TextView) convertView.findViewById(R.id.tv_yule_title);
				yuleHolder.author_name = (TextView) convertView.findViewById(R.id.tv_yule_author_name);
				yuleHolder.date = (TextView) convertView.findViewById(R.id.tv_yule_date);
				convertView.setTag(yuleHolder);
			} else {
				yuleHolder = (YuleHolder) convertView.getTag();
			}
			
			Data data = yuleBean.result.data.get(position);
			String pic_s_url = data.thumbnail_pic_s;
			String title = data.title;
			String author_name = data.author_name;
			String date = data.date;
			
			yuleHolder.thumbnail_pic_s.setImageUrl(pic_s_url, mImageLoader);
			yuleHolder.thumbnail_pic_s.setErrorImageResId(R.drawable.iv_yule_pic);
			yuleHolder.thumbnail_pic_s.setDefaultImageResId(R.drawable.iv_yule_pic);
			yuleHolder.title.setText(title);
			yuleHolder.author_name.setText(author_name);
			yuleHolder.date.setText(date);
			
			return convertView;
		}
		
		class YuleHolder {
			NetworkImageView thumbnail_pic_s;
			TextView title;
			TextView author_name;
			TextView date;
		}

	}
}

class BitmapCache implements ImageCache {

	private LruCache<String, Bitmap> cache;

	BitmapCache() {
		int maxSize = 10 * 1024 * 1024;
		cache = new LruCache<String, Bitmap>(maxSize) {

			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
	}

	public Bitmap getBitmap(String url) {
		return cache.get(url);
	}

	public void putBitmap(String url, Bitmap bitmap) {
		if (bitmap != null) {
			cache.put(url, bitmap);
		}
	}
}
