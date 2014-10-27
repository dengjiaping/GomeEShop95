package com.gome.ecmall.home.mygome;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.StationLetterDetails;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class StationLetterDetailsActivity extends AbsSubActivity implements OnClickListener {
    private Button backBtn;
    private TextView titleText;
    private LinearLayout titleLayout;
    private ScrollView contentLayout;
    private TextView letterTitleText;
    private TextView letterContentText;
    private TextView letterTimeText;
    private ImageView noNetImg;

    private String msgId;
    private String readStus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mygome_station_letter_details);

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        msgId = intent.getStringExtra(JsonInterface.JK_MESSAGE_ID);
        readStus = intent.getStringExtra(JsonInterface.JK_READ_STATUS);

        initView();
    }

    private void initView() {
        backBtn = (Button) findViewById(R.id.common_title_btn_back);
        backBtn.setText(R.string.back);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(this);

        titleText = (TextView) findViewById(R.id.common_title_tv_text);
        titleText.setVisibility(View.VISIBLE);
        titleText.setText(R.string.station_letter_details);

        titleLayout = (LinearLayout) findViewById(R.id.mygome_station_letter_details_title_layout);
        contentLayout = (ScrollView) findViewById(R.id.mygome_station_letter_details_content_layout);
        letterTitleText = (TextView) findViewById(R.id.mygome_station_letter_details_title_textView);
        letterTimeText = (TextView) findViewById(R.id.mygome_station_letter_details_time_textView1);
        letterContentText = (TextView) findViewById(R.id.mygome_station_letter_details_content_textView1);
        noNetImg = (ImageView) findViewById(R.id.no_net_img);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (!NetUtility.isNetworkAvailable(StationLetterDetailsActivity.this)) {
            CommonUtility.showMiddleToast(StationLetterDetailsActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            noNetImg.setVisibility(View.VISIBLE);
            contentLayout.setVisibility(View.GONE);
            titleLayout.setVisibility(View.GONE);
            return;
        }
        noNetImg.setVisibility(View.GONE);
        contentLayout.setVisibility(View.VISIBLE);
        titleLayout.setVisibility(View.VISIBLE);

        new AsyncTask<Void, Void, StationLetterDetails>() {
            LoadingDialog dialog = null;

            protected void onPreExecute() {
                dialog = CommonUtility.showLoadingDialog(StationLetterDetailsActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            };

            @Override
            protected StationLetterDetails doInBackground(Void... params) {
                String json = StationLetterDetailsService.createJson(msgId, readStus);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_MESSAGE_DETAIL, json);
                if (result == null || JsonInterface.JV_FAIL.equals(result)) {
                    return null;
                }
                return StationLetterDetailsService.parseStationLetterDetails(result);
            }

            protected void onPostExecute(StationLetterDetails result) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (result == null) {
                    return;
                }

                setData(result);
            }

        }.execute();
    }

    private void setData(StationLetterDetails letter) {
        letterTitleText.setText(letter.getMessageTitle());
        letterTimeText.setText(letter.getMessageTime());
        letterContentText.setText(letter.getMessageContent());
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.common_title_btn_back:
            goback();
            break;
        default:
            break;
        }

    }
}
