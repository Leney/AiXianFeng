package com.bjzcht.lovebeequick.gui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjzcht.lovebeequick.R;
import com.bjzcht.lovebeequick.gui.view.CountDownTextView2;
import com.bjzcht.lovebeequick.util.PreferencesUtils;
import com.bjzcht.lovebeequick.util.log.DLog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;

import java.util.List;

/**
 * 全屏界面
 * Created by lilijun on 2017/7/4.
 */

public class FullScreenActivity extends BaseActivity {

    private SimpleDraweeView tabImg;
    private TextView tabTips;
    private IFLYNativeAd tabAd;
    private CountDownTextView2 countDownText;

    @Override
    protected void initView() {
        showCenterView();
        if (PreferencesUtils.getBoolean("is_first_in",true)) {
            PreferencesUtils.putBoolean("is_first_in", false);
            FirstInActivity.startActivity(FullScreenActivity.this);
            finish();
        } else {
            setTitleVisible(false);
            setCenterView(R.layout.full_screen_ad_lay);

            tabImg = (SimpleDraweeView) findViewById(R.id.home_ad_banner_img);
            tabTips = (TextView) findViewById(R.id.home_ad_banner_tips);

            ImageView closeImg = (ImageView) findViewById(R.id.home_ad_banner_close_img);
            closeImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    countDownText.stop();
                    MainActivity.startActivity(FullScreenActivity.this);
                    finish();
                }
            });
            countDownText = (CountDownTextView2) findViewById(R.id.home_ad_banner_count_down_text);
            countDownText.setOnDoneListner(new CountDownTextView2.OnCountDownDoneListener() {
                @Override
                public void onDone() {
                    MainActivity.startActivity(FullScreenActivity.this);
                    finish();
                }
            });

            getFullScreenAd();
        }
    }

    @Override
    public void onBackPressed() {
        countDownText.stop();
        super.onBackPressed();
    }

    private void getFullScreenAd() {
        tabAd = new IFLYNativeAd(this, "72ADDD0A7D6DB73002B50127EFB9058E", tabAdListener);
        tabAd.loadAd(1);
    }

    private IFLYNativeListener tabAdListener = new IFLYNativeListener() {

        @Override
        public void onConfirm() {

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onADLoaded(List<NativeADDataRef> list) {
            showCenterView();
            DLog.i("llj", "全屏广告请求成功！！！");
            final NativeADDataRef nativeADDataRef = list.get(0);

            countDownText.start(3);
            nativeADDataRef.onExposured(tabImg);
            tabImg.setImageURI(nativeADDataRef.getImage());
            if (!TextUtils.isEmpty(nativeADDataRef.getAdSourceMark())) {
                tabTips.setText(nativeADDataRef.getAdSourceMark() + "|广告");
            }
            tabImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DLog.i("llj", "点击全屏广告！！！");
                    countDownText.stop();
                    MainActivity.startActivity(FullScreenActivity.this);
                    nativeADDataRef.onClicked(view);
                    finish();
                }
            });

        }

        @Override
        public void onAdFailed(AdError adError) {
//            showErrorView();
            DLog.e("llj", "全屏广告请求错误，code--->>>" + adError.getErrorCode() + "描述---->>>" + adError.getErrorDescription());
            MainActivity.startActivity(FullScreenActivity.this);
            finish();
        }
    };
}
