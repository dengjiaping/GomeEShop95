package com.gome.ecmall.home.more;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.home.MoreActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.ecmall.util.VersionUpdateUtils;
import com.gome.eshopnew.R;

public class MoreSectionListAdapter extends BaseAdapter {

    private static final String classType_more = "MoreActivity";
    private static final String classType_userhelp = "UseHelpActivity";
    private static final String classType_help = "HelpActivity";
    private int[] resIds = null;
    private int[] idDescIds = null;
    private LayoutInflater inflater;
    private int rid;
    private Context context;
    private String classType;

    public MoreSectionListAdapter(Context context, int[] ids, int[] idDescIds, int rid, String classType) {
        resIds = ids;
        this.rid = rid;
        this.context = context;
        this.classType = classType;
        this.idDescIds = idDescIds;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return resIds.length;
    }

    @Override
    public Integer getItem(int position) {
        return resIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.more_section_item, null);
            holder.tvMain = (TextView) convertView.findViewById(R.id.more_tv_title);
            holder.tvHint = (TextView) convertView.findViewById(R.id.more_tv_hint);
            holder.ivArrow = (ImageView) convertView.findViewById(R.id.more_iv_arrow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvHint.setVisibility(View.GONE);
        holder.tvMain.setText(resIds[position]);
        int count = getCount();
        if (count == 1) {
            convertView.setBackgroundResource(R.drawable.more_item_single_bg_selector);
        } else {
            if (position == 0) {
                convertView.setBackgroundResource(R.drawable.more_item_first_bg_selector);
            } else if (position == getCount() - 1) {
                convertView.setBackgroundResource(R.drawable.more_item_last_bg_selector);
            } else {
                convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
            }
        }
        convertView.setOnClickListener(new MyOnClickListener(position));
        return convertView;
    }

    public static class ViewHolder {
        public TextView tvMain;
        public TextView tvHint;
        public ImageView ivArrow;
    }

    public class MyOnClickListener implements OnClickListener {

        int position;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {

            switch (rid) {
            case R.id.more_help_lv_first: {
                switch (position) {
                case 0:
                    Intent helpIntent = new Intent(context, UseHelpActivity.class);
                    context.startActivity(helpIntent);
                    break;
                case 1:
                    // 进入帮助界面
                    Intent storeHelpIntent = new Intent(context, StoreHelpActivity.class);
                    context.startActivity(storeHelpIntent);
                    break;
                // case 2:
                // Intent useCourseIntent = new Intent(context, UseCourseActivity.class);
                // context.startActivity(useCourseIntent);
                // break;
                // case 3:
                // Intent aboutIntent = new Intent(context, AboutActivity.class);
                // context.startActivity(aboutIntent);
                // break;
                case 2:
                    Intent aboutIntent = new Intent(context, AboutActivity.class);
                    context.startActivity(aboutIntent);
                    break;
                }
            }
                break;

            case R.id.more_store_help_lv_first:
                Intent myStoreHelpIntent = new Intent(context, UseHelpDetailActivity.class);
                String mtitle = "";
                String mcontent = "";
                switch (position) {
                case 0:
                    mtitle = context.getString(resIds[position]);
                    mcontent = context.getString(R.string.liucheng);
                    myStoreHelpIntent.putExtra("show", "html");
                    break;
                case 1:
                    mtitle = context.getString(resIds[position]);
                    mcontent = context.getString(R.string.zhuyi);
                    myStoreHelpIntent.putExtra("show", "html");
                    break;
                case 2:
                    mtitle = context.getString(resIds[position]);
                    mcontent = context.getString(R.string.shuoming);
                    myStoreHelpIntent.putExtra("show", "html");
                    break;
                case 3:
                    mtitle = context.getString(resIds[position]);
                    mcontent = context.getString(R.string.tishi);
                    myStoreHelpIntent.putExtra("show", "html");
                    break;
                }
                myStoreHelpIntent.putExtra(UseHelpDetailActivity.INTENT_KEY_TITLE, mtitle);
                myStoreHelpIntent.putExtra(UseHelpDetailActivity.INTENT_KEY_CONTENT, mcontent);
                context.startActivity(myStoreHelpIntent);
                break;
            case R.id.more_section_lv_first:
                // 进入设置Activity
                if (classType_more.equals(classType)) {
                    Intent settingsIntent = new Intent(context, SettingsActivity.class);
                    context.startActivity(settingsIntent);
                } else if (classType_userhelp.equals(classType)) {
                    if (idDescIds.length > 0) {
                        Intent intent = new Intent(context, UseHelpDetailActivity.class);
                        String title = context.getString(resIds[position]);
                        String content = context.getString(idDescIds[position]);
                        intent.putExtra(UseHelpDetailActivity.INTENT_KEY_TITLE, title);
                        intent.putExtra(UseHelpDetailActivity.INTENT_KEY_CONTENT, content);
                        context.startActivity(intent);
                    }
                }
                break;
            case R.id.more_section_lv_second:
                if (classType_more.equals(classType)) {
                    switch (position) {
                    // 国美门店
                    case 0:
                        boolean isAllow = PreferenceUtils.isAllowLocation();
                        if (isAllow) {
                            if (!TextUtils.isEmpty(String.valueOf(GlobalConfig.getInstance().getLat()))
                                    && !TextUtils.isEmpty(String.valueOf(GlobalConfig.getInstance().getLog()))) {
                                Intent intent = new Intent(context, NearStoreListActivity.class);
                                intent.putExtra(NearStoreListActivity.ISALLOWUSELOACTION, true);
                                intent.setAction("MoreActivity");
                                context.startActivity(intent);
                            } else {
                                CommonUtility.showConfirmDialog(context, "",
                                        context.getString(R.string.isopenloaction),
                                        context.getString(R.string.confirm), openleftListener,
                                        context.getString(R.string.cancel), openrightListener);
                            }
                        } else {
                            CommonUtility.showConfirmDialog(context, "",
                                    context.getString(R.string.is_use_you_now_loaction),
                                    context.getString(R.string.is_use_you_now_yes), leftListener,
                                    context.getString(R.string.is_use_you_now_no), rightListener);
                        }
                        break;
                    case 1:
                        // 浏览历史
                        Intent historyIntent = new Intent(context, ProductBrowseHistoryActivity.class);
                        context.startActivity(historyIntent);
                        break;
                    case 2:
                        // 公告
                        Intent announceIntent = new Intent(context, AnnouncementListActivity.class);
                        context.startActivity(announceIntent);
                        break;
                    }
                } else if (classType_userhelp.equals(classType)) {
                    if (idDescIds.length > 0) {
                        Intent intent = new Intent(context, UseHelpDetailActivity.class);
                        String title = context.getString(resIds[position]);
                        String content = context.getString(idDescIds[position]);
                        intent.putExtra(UseHelpDetailActivity.INTENT_KEY_TITLE, title);
                        intent.putExtra(UseHelpDetailActivity.INTENT_KEY_CONTENT, content);
                        context.startActivity(intent);
                    }
                }

                break;
            case R.id.more_section_lv_third:
                if (classType_more.equals(classType)) {
                    switch (position) {
                    case 0:
                        // 进入帮助界面
                        Intent helpIntent = new Intent(context, HelpActivity.class);
                        context.startActivity(helpIntent);
                        break;
                    case 1:
                        ((MoreActivity) context).launchTargetActivity(FeedbackActivity.class);
                        break;
                    case 2:
                        // 检查更新
                        VersionUpdateUtils versonUpdateUtils = new VersionUpdateUtils(context);
                        versonUpdateUtils.versonUpdate("Y");
                        break;
                    }
                } else if (classType_userhelp.equals(classType)) {
                    if (idDescIds.length > 0) {
                        Intent intent = new Intent(context, UseHelpDetailActivity.class);
                        String title = context.getString(resIds[position]);
                        String content = context.getString(idDescIds[position]);
                        intent.putExtra(UseHelpDetailActivity.INTENT_KEY_TITLE, title);
                        intent.putExtra(UseHelpDetailActivity.INTENT_KEY_CONTENT, content);
                        context.startActivity(intent);
                    }
                }

                break;
            case R.id.more_section_lv_four:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:4008-199-777"));
                context.startActivity(intent);
                break;
            }

        }
    }

    private android.content.DialogInterface.OnClickListener leftListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            PreferenceUtils.setAllowLocation(true);
            dialog.dismiss();
            if (!TextUtils.isEmpty(String.valueOf(GlobalConfig.getInstance().getLat()))
                    && !TextUtils.isEmpty(String.valueOf(GlobalConfig.getInstance().getLog()))) {
                Intent intent = new Intent(context, NearStoreListActivity.class);
                intent.putExtra(NearStoreListActivity.ISALLOWUSELOACTION, true);
                intent.setAction("MoreActivity");
                context.startActivity(intent);
            } else {
                CommonUtility.showConfirmDialog(context, "", context.getString(R.string.isopenloaction),
                        context.getString(R.string.confirm), openleftListener, context.getString(R.string.cancel),
                        openrightListener);
            }
        }
    };
    private android.content.DialogInterface.OnClickListener rightListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.cancel();
            Intent intent = new Intent(context, CityListActivity.class);
            intent.setAction("MoreActivity");
            context.startActivity(intent);
        }
    };
    private android.content.DialogInterface.OnClickListener openleftListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.dismiss();
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
        }
    };
    private android.content.DialogInterface.OnClickListener openrightListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.cancel();
        }

    };
}
