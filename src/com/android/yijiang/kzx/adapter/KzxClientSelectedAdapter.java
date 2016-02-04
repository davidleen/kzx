package com.android.yijiang.kzx.adapter;

import java.io.Serializable;
import java.text.DateFormat;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.adapter.KzxTaskSelectedAdapter.ViewHolder;
import com.android.yijiang.kzx.bean.ClientBean;
import com.android.yijiang.kzx.bean.ClientBean;
import com.android.yijiang.kzx.bean.TargetCanRelateBean;
import com.android.yijiang.kzx.bean.TaskCanRelateBean;
import com.android.yijiang.kzx.fragment.KzxFaceBackDialogFragment;
import com.android.yijiang.kzx.sdk.ACache;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.DateUtils;
import com.android.yijiang.kzx.sdk.StringMatcher;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.ui.ContentFragmentActivity;
import com.android.yijiang.kzx.widget.HorizontalListView;
import com.android.yijiang.kzx.widget.SquaredImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.test.MoreAsserts;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 关联目标适配器
 * @title com.android.yijiang.kzx.adapter
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxClientSelectedAdapter extends BaseAdapter implements Filterable,SectionIndexer{

	private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static HanyuPinyinOutputFormat spellFormat = new HanyuPinyinOutputFormat();
	private Context context;
	private DataFilter filter;
	private static HashMap<String, ClientBean> isSelected;// 记录选中数据
	private List<ClientBean> leaderList = new ArrayList<ClientBean>();

	public KzxClientSelectedAdapter(Context context) {
		this.context = context;
		isSelected = new HashMap<String, ClientBean>();
		//索引首字母格式
		spellFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		spellFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		spellFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
	}
	
	public void setDataForLoader(List<ClientBean> leaderList,HashMap<String, ClientBean> isSelected) {
		this.leaderList.addAll(leaderList);
		if(isSelected!=null){
			//设置选中值
			this.isSelected=isSelected;
		}
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return leaderList.size();
	}

	@Override
	public ClientBean getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= getCount()) {
			return null;
		}
		return leaderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.kzx_client_selected_lv_item_fragment, parent, false);
			holder = new ViewHolder();
			holder.letterTv=(TextView)convertView.findViewById(R.id.letterTv);
			holder.nameTv = (TextView) convertView.findViewById(R.id.nameTv);
			holder.phoneTv = (TextView) convertView.findViewById(R.id.phoneTv);
			holder.switchBox = (CheckBox) convertView.findViewById(R.id.switchBox);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String firstChar;
		String firstChar2;
		try {
			firstChar = PinyinHelper.toHanyuPinyinString(getItem(position).getName(), spellFormat, "").toUpperCase().substring(0,1);
			if(position>0){
				firstChar2 = PinyinHelper.toHanyuPinyinString(getItem(position-1).getName(), spellFormat, "").toUpperCase().substring(0,1);
				if(firstChar.equals(firstChar2)){
					holder.letterTv.setVisibility(View.GONE);
				}else{
					holder.letterTv.setVisibility(View.VISIBLE);
					holder.letterTv.setText(firstChar);
				}
			}else{
				holder.letterTv.setVisibility(View.VISIBLE);
				holder.letterTv.setText(firstChar);
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		holder.nameTv.setText(leaderList.get(position).getName());
		holder.phoneTv.setText(leaderList.get(position).getPhone());
		holder.switchBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					//清空所有的,在添加一个条目
					clearSelectedPosition();
					isSelected.put(leaderList.get(position).getId(), leaderList.get(position));
				} else {
					isSelected.remove(leaderList.get(position).getId());
				}
			}
		});
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				holder.switchBox.setChecked((isSelected.get(leaderList.get(position).getId()) == null ? true : false));
			}
		});
		holder.switchBox.setChecked((isSelected.get(leaderList.get(position).getId()) == null ? false : true));
		return convertView;
	}
	
	public void clearSelectedPosition(){
		isSelected.clear();
		notifyDataSetChanged();
	}
	
	public static HashMap<String, ClientBean> getIsSelected() {
		return isSelected;
	}
	
	static class ViewHolder {
		public TextView letterTv;
		public TextView nameTv;
		public TextView phoneTv;
		public CheckBox switchBox;
	}

	@Override
	public Filter getFilter() {
		if (filter == null) {
			filter = new DataFilter(leaderList);
		}
		return filter;
	}

	private class DataFilter extends Filter {

		private List<ClientBean> original;

		public DataFilter(List<ClientBean> list) {
			this.original = list;
		}

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			if (constraint == null || constraint.length() == 0) {
				results.values = original;
				results.count = original.size();
			} else {
				List<ClientBean> mList = new ArrayList<ClientBean>();
				for (ClientBean lb : original) {
					if (lb.getName().toUpperCase().startsWith(constraint.toString().toUpperCase())||lb.getPhone().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
						mList.add(lb);
					}
				}
				results.values = mList;
				results.count = mList.size();
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			leaderList = (List<ClientBean>) results.values;
			notifyDataSetChanged();
		}

	}

	@Override
	public int getPositionForSection(int section) {
		// If there is no item for current section, previous section will be selected
		for (int i = section; i >= 0; i--) {
			for (int j = 0; j < getCount(); j++) {
				if (i == 0) {
					// For numeric section
					for (int k = 0; k <= 9; k++) {
						String firstChar;
						try {
							firstChar = PinyinHelper.toHanyuPinyinString(getItem(j).getName(), spellFormat, "").toUpperCase().substring(0,1);
							if (StringMatcher.match(firstChar, String.valueOf(k)))
								return j;
						} catch (BadHanyuPinyinOutputFormatCombination e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					String firstChar;
					try {
						firstChar = PinyinHelper.toHanyuPinyinString(getItem(j).getName(), spellFormat, "").toUpperCase().substring(0,1);
						if (StringMatcher.match(firstChar, String.valueOf(mSections.charAt(i))))
							return j;
					} catch (BadHanyuPinyinOutputFormatCombination e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		String[] sections = new String[mSections.length()];
		for (int i = 0; i < mSections.length(); i++)
			sections[i] = String.valueOf(mSections.charAt(i));
		return sections;
	}
	
}
