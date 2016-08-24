package com.developer.volley.bean;

import java.lang.reflect.Field;
import java.io.Serializable;
import java.util.List;

public class YuleBean implements Serializable {

	public String reason;
	public int error_code;
	public Result result;

	public class Result implements Serializable {

		public String stat;
		public List<Data> data;

		public class Data implements Serializable {

			public String title;
			public String category;
			public String thumbnail_pic_s03;
			public String author_name;
			public String thumbnail_pic_s;
			public String date;
			public String url;

			@Override
			public String toString() {
				String s = "";
				Field[] arr = this.getClass().getFields();
				for (Field f : getClass().getFields()) {
					try {
						s += f.getName() + "=" + f.get(this) + "\n,";
					} catch (Exception e) {
					}
				}
				return getClass().getSimpleName()
						+ "["
						+ (arr.length == 0 ? s : s.substring(0, s.length() - 1))
						+ "]";
			}
		}

		@Override
		public String toString() {
			String s = "";
			Field[] arr = this.getClass().getFields();
			for (Field f : getClass().getFields()) {
				try {
					s += f.getName() + "=" + f.get(this) + "\n,";
				} catch (Exception e) {
				}
			}
			return getClass().getSimpleName() + "["
					+ (arr.length == 0 ? s : s.substring(0, s.length() - 1))
					+ "]";
		}
	}

	@Override
	public String toString() {
		String s = "";
		Field[] arr = this.getClass().getFields();
		for (Field f : getClass().getFields()) {
			try {
				s += f.getName() + "=" + f.get(this) + "\n,";
			} catch (Exception e) {
			}
		}
		return getClass().getSimpleName() + "["
				+ (arr.length == 0 ? s : s.substring(0, s.length() - 1)) + "]";
	}
}
