package com.android.yijiang.kzx.adapter;

import java.io.Serializable;
import java.text.DateFormat;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter.OnItemClickListener;
import com.android.yijiang.kzx.bean.Contacts;
import com.android.yijiang.kzx.bean.LeaderBean;
import com.android.yijiang.kzx.bean.MemberBean;
import com.android.yijiang.kzx.bean.TaskBean;
import com.android.yijiang.kzx.fragment.KzxFaceBackDialogFragment;
import com.android.yijiang.kzx.sdk.ACache;
import com.android.yijiang.kzx.sdk.ChineseSpelling;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.StringMatcher;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.widget.HorizontalListView;
import com.android.yijiang.kzx.widget.SquaredImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
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

/**
 * 邀请新伙伴适配器
 * @title com.android.yijiang.kzx.adapter
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxInvitePartnerAdapter extends BaseAdapter implements Filterable,SectionIndexer{

	private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private Context context;
	private String typeStr;
	private DataFilter filter;
	private List<Contacts> contactsList=new ArrayList<Contacts>();
	private static HanyuPinyinOutputFormat spellFormat = new HanyuPinyinOutputFormat();
	private Map<String,String> memberMap=new HashMap<String,String>();
	
	public KzxInvitePartnerAdapter(Context context,String typeStr) {
		this.context = context;
		this.typeStr=typeStr;
		//索引首字母格式
		spellFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		spellFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		spellFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
	}
	
	public void setDataForLoad(List<Contacts> contactsList){
		this.contactsList.clear();
		this.contactsList.addAll(contactsList);
		notifyDataSetChanged();
	}
	
	public void setDataForMember(Map<String,String> memberMap){
		this.memberMap=memberMap;
		notifyDataSetChanged();
	}
	
	public void setDataForOneMember(MemberBean memberBean){
		this.memberMap.put(memberBean.getPhone(), memberBean.getState()+"");
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return contactsList.size();
	}

	@Override
	public Contacts getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= getCount()) {
			return null;
		}
		return contactsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.kzx_invite_lv_item_fragment, parent, false);
			holder = new ViewHolder();
			holder.nameTv=(TextView)convertView.findViewById(R.id.nameTv);
			holder.phoneTv=(TextView)convertView.findViewById(R.id.phoneTv);
			holder.inviteBtn=(ImageButton)convertView.findViewById(R.id.inviteBtn);
			holder.stateTv=(TextView)convertView.findViewById(R.id.stateTv);
			holder.letterTv=(TextView)convertView.findViewById(R.id.letterTv);
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
		holder.nameTv.setText(contactsList.get(position).getName());
		holder.phoneTv.setText(contactsList.get(position).getPhoneNumber());
		holder.inviteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnItemClickListener.onItemClick(position, contactsList.get(position));
			}
		});
		if("partner".equals(typeStr)){
			holder.inviteBtn.setColorFilter(Color.parseColor("#b59269"));
		}else if("task".equals(typeStr)){
			holder.inviteBtn.setColorFilter(Color.parseColor("#397bc5"));//#c7d75d
		}
		//匹配通讯录和全体成员关系
		String state=memberMap.get(contactsList.get(position).getPhoneNumber().replaceAll(" ", "").replaceAll("[+]86", ""));
		if(StringUtils.isEmpty(state)){
			holder.inviteBtn.setVisibility(View.VISIBLE);
			holder.stateTv.setVisibility(View.GONE);
		}else if("1".equals(state)){
//			holder.inviteBtn.setVisibility(View.GONE);
//			holder.stateTv.setVisibility(View.VISIBLE);
//			holder.stateTv.setText("邀请中");
			holder.inviteBtn.setVisibility(View.VISIBLE);
			holder.stateTv.setVisibility(View.GONE);
		}else if("2".equals(state)){
			holder.inviteBtn.setVisibility(View.GONE);
			holder.stateTv.setVisibility(View.VISIBLE);
			holder.stateTv.setText("已加入");
			if("partner".equals(typeStr)){
				holder.stateTv.setTextColor(Color.parseColor("#b59269"));
			}else if("task".equals(typeStr)){
				holder.stateTv.setTextColor(Color.parseColor("#397bc5"));//#c7d75d
			}
		}else if("3".equals(state)){
			holder.inviteBtn.setVisibility(View.VISIBLE);
			holder.stateTv.setVisibility(View.GONE);
		}else{
			holder.inviteBtn.setVisibility(View.VISIBLE);
			holder.stateTv.setVisibility(View.GONE);
		}
		return convertView;
	}

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(int position, Contacts contacts);
	}

	static class ViewHolder {
		public TextView letterTv;
		public TextView nameTv;
		public TextView phoneTv;
		public ImageButton inviteBtn;
		public TextView stateTv;
	}
	
	@Override
	public Filter getFilter() {
		if (filter == null) {
			filter = new DataFilter(contactsList);
		}
		return filter;
	}

	private class DataFilter extends Filter {

		private List<Contacts> original;
		public DataFilter(List<Contacts> list) {
			this.original = list;
		}
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			if (constraint == null || constraint.length() == 0) {
				results.values = original;
				results.count = original.size();
			} else {
				List<Contacts> mList = new ArrayList<Contacts>();
				for (Contacts lb : original) {
					if (lb.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()) || lb.getPhoneNumber().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
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
			contactsList = (List<Contacts>) results.values;
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
