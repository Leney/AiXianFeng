package com.bjzcht.lovebeequick.gui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzcht.lovebeequick.R;
import com.bjzcht.lovebeequick.gui.activity.GoodsDetailActivity;
import com.bjzcht.lovebeequick.gui.activity.LoginActivity;
import com.bjzcht.lovebeequick.gui.activity.MoreGoodsActivity;
import com.bjzcht.lovebeequick.gui.activity.NewExclusiveActivity;
import com.bjzcht.lovebeequick.gui.activity.SearchActivity;
import com.bjzcht.lovebeequick.gui.activity.ToBeVipActivity;
import com.bjzcht.lovebeequick.gui.activity.TopUpActivity;
import com.bjzcht.lovebeequick.gui.activity.WebActivity;
import com.bjzcht.lovebeequick.gui.adapter.BannerAdapter;
import com.bjzcht.lovebeequick.gui.adapter.HomeListViewAdapter;
import com.bjzcht.lovebeequick.gui.view.WraperExpandableListView;
import com.bjzcht.lovebeequick.manager.UserInfoManager;
import com.bjzcht.lovebeequick.model.AdItem;
import com.bjzcht.lovebeequick.model.Goods;
import com.bjzcht.lovebeequick.model.HomeGroupItem;
import com.bjzcht.lovebeequick.model.UserInfo;
import com.bjzcht.lovebeequick.util.Constants;
import com.bjzcht.lovebeequick.util.ParseUtil;
import com.bjzcht.lovebeequick.util.PreferencesUtils;
import com.bjzcht.lovebeequick.util.Util;
import com.bjzcht.lovebeequick.util.log.DLog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 首页Tab Fragment
 * Created by yb on 2016/8/12.
 */
public class TabHomeFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "TabHomeFragment";
    /**
     * 请求首页的tag
     */
    private static String GET_HOME_DATA_TAG = "index/index";
    /**
     * 登陆tag
     */
    private final String LOGIN_USER = "supplier/login";

    //    private PullToRefreshLayout pullToRefreshLayout;
//    private RefreshExpandableListView2 listView;
    private WraperExpandableListView listView;
    private HomeListViewAdapter adapter;
    private List<HomeGroupItem> groupList;
    private List<List<Goods[]>> childList;
    private View headerView;

    //    private SimpleDraweeView titleImg;
    private EditText searchInput;
    private Button searchBtn;

//    private TextView headerHotName;
//    private TextView headerHotMore;
//    private RelativeLayout headerHotTitleLay;
//    private LinearLayout headerHotItemLay;
//    /**
//     * 头部推荐标题数据
//     */
//    private RecommendTitle headerRecommendTitle;
//    /**
//     * 头部推荐商品列表数据集合
//     */
//    private List<Goods> headerRecommendList;

    /**
     * 轮播banner控件
     */
    private RollPagerView headerBannerPagerView;
    private BannerAdapter headerBannerAdapter;
//    /**
//     * 轮播消息控件
//     */
//    private CustomTextView customTextView;
    /**
     * 首次体验、消费充值、会员专卡、蜜蜂招募
     */
    private TextView[] headerAdTabs;
    /**
     * 顶部banner数据集合
     */
    private List<AdItem> headerBannerList;

    private ImageView bannerAdCloseBtn;

//    /**
//     * 头部水平广告位
//     */
//    private HorizontalScrollView headerHorizontalScrollView;
//    /**
//     * 添加子视图的父类容器
//     */
//    private LinearLayout headerHoriziontalItemLay;
//    private List<AdItem> horizontalList;

//    /**
//     * 标题上logo图片路径
//     */
//    private String logoUrl;

//    /**
//     * 标记当前是否是在下拉刷新
//     */
//    private boolean isRefreshing = false;


    private IFLYNativeAd bannerAd;
    private SimpleDraweeView adBannerImg;
    private TextView adTips;
    private FrameLayout bannerLay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView(RelativeLayout view) {
        setCenterView(R.layout.fragment_home);

        groupList = new ArrayList<>();
        childList = new ArrayList<>();
        listView = (WraperExpandableListView) view.findViewById(R.id.home_list_view);
        adapter = new HomeListViewAdapter(groupList, childList);
//        pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.home_refresh_layout);
//        pullToRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                DLog.i("lilijun", "下拉刷新！！！");
//                isRefreshing = true;
//                String loginInfoByPwd = PreferencesUtils.getString(Constants
//                        .LOGIN_INFO_BY_PWD);
//                if (!TextUtils.isEmpty(loginInfoByPwd)) {
//                    // 有通过账号密码登陆过的信息,则去登陆(每次进入都需要登陆)
//                    HashMap<String, String> loginParams = new HashMap<>();
//                    loginParams.put("parms", loginInfoByPwd);
//                    loadDataPost(LOGIN_USER, loginParams);
//                } else {
//                    loadDataGet(GET_HOME_DATA_TAG, null);
//                }
//            }
//        });


//        adBannerImg.setVisibility(View.GONE);

        // 标题部分
//        titleImg = (SimpleDraweeView) view.findViewById(R.id.home_title_img);
        searchInput = (EditText) view.findViewById(R.id.home_title_input_edit);
        searchBtn = (Button) view.findViewById(R.id.home_title_search_btn);
        searchBtn.setOnClickListener(this);
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event
                        .getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    // 标题搜索按钮
                    // 隐藏键盘
                    Util.hideInput(getActivity(), searchInput);
                    SearchActivity.startActivity(getActivity(), searchInput.getText().toString());
                    return true;
                }
                return false;
            }
        });

        initHeaderView();
        listView.addHeaderView(headerView);
        listView.setAdapter(adapter);
//        listView.setonRefreshListener(new RefreshExpandableListView2.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                isRefreshing = true;
//                loadDataGet(GET_HOME_DATA_TAG, null);
//            }
//        });
        String loginInfoByPwd = PreferencesUtils.getString(Constants
                .LOGIN_INFO_BY_PWD);
        if (!TextUtils.isEmpty(loginInfoByPwd)) {
            // 有通过账号密码登陆过的信息,则去登陆(每次进入都需要登陆)
            HashMap<String, String> loginParams = new HashMap<>();
            loginParams.put("parms", loginInfoByPwd);
            loadDataPost(LOGIN_USER, loginParams);
        } else {
            loadDataGet(GET_HOME_DATA_TAG, null);
        }
    }

    private void initHeaderView() {
//        headerRecommendTitle = new RecommendTitle();
//        headerRecommendList = new ArrayList<>();
        headerBannerList = new ArrayList<>();
//        horizontalList = new ArrayList<>();
        headerAdTabs = new TextView[4];
        headerView = View.inflate(getActivity(), R.layout.hot_header_item, null);

//        // 热门推荐
//        headerHotTitleLay = (RelativeLayout) headerView.findViewById(R.id
//                .home_header_hot_title_lay);
//        headerHotName = (TextView) headerView.findViewById(R.id.home_header_hot_name);
//        headerHotMore = (TextView) headerView.findViewById(R.id.home_header_hot_more);
//        headerHotMore.setOnClickListener(this);
//        headerHotItemLay = (LinearLayout) headerView.findViewById(R.id.home_header_hot_goods_lay);

        //banner
        headerBannerPagerView = (RollPagerView) headerView.findViewById(R.id.home_banner);
        headerBannerAdapter = new BannerAdapter(headerBannerPagerView, headerBannerList);
        headerBannerPagerView.setAdapter(headerBannerAdapter);
        headerBannerPagerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 顶部banner item点击事件
                doBannerItemClickListener(headerBannerList.get(position));
            }
        });

//        headerAdTabs[0] = (TextView) headerView.findViewById(R.id.home_tab_ad_0);
//        headerAdTabs[1] = (TextView) headerView.findViewById(R.id.home_tab_ad_1);
//        headerAdTabs[2] = (TextView) headerView.findViewById(R.id.home_tab_ad_2);
//        headerAdTabs[3] = (TextView) headerView.findViewById(R.id.home_tab_ad_3);

        headerAdTabs[0] = (TextView) headerView.findViewById(R.id.home_tab_ad_0);
        headerAdTabs[1] = (TextView) headerView.findViewById(R.id.home_tab_ad_1);
        headerAdTabs[2] = (TextView) headerView.findViewById(R.id.home_tab_ad_2);
        headerAdTabs[3] = (TextView) headerView.findViewById(R.id.home_tab_ad_3);
        headerAdTabs[0].setOnClickListener(this);
        headerAdTabs[1].setOnClickListener(this);
        headerAdTabs[2].setOnClickListener(this);
        headerAdTabs[3].setOnClickListener(this);

//        customTextView = (CustomTextView) headerView.findViewById(R.id.home_header_show_message);

        //horizontalScrollView
//        headerHorizontalScrollView = (HorizontalScrollView) headerView.findViewById(R.id
//                .home_header_horizontalScrollView);
//        headerHoriziontalItemLay = (LinearLayout) headerView.findViewById(R.id
//                .home_header_horizontal_layout);


        bannerLay = (FrameLayout) headerView.findViewById(R.id.home_ad_banner_lay);
        bannerLay.setVisibility(View.GONE);
        adBannerImg = (SimpleDraweeView) headerView.findViewById(R.id.home_ad_banner_img);
        adTips = (TextView) headerView.findViewById(R.id.home_ad_banner_tips);

        bannerAdCloseBtn = (ImageView) headerView.findViewById(R.id.home_ad_banner_close_img);
        bannerAdCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bannerLay.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, GET_HOME_DATA_TAG)) {
            // 首页数据加载成功
            groupList.clear();
            childList.clear();
            headerBannerList.clear();
            ParseUtil.parseHomeData(resultObject, groupList, childList, headerBannerList);


            AdItem adItem = new AdItem();
            adItem.setIcon("http://img01.bqstatic.com//upload/activity/2017072819235096.jpg@90Q.jpg");
            adItem.setName("鲜果天天特价");
            adItem.setType(AdItem.TYPE_WEB);
            adItem.setSkipUrl("http://m.beequick.cn/show/active?bigids=2%2C0&delivery_type=0&location=116.449732%2C39.926387&trackid=focus1%7C22004&g_screen_pixels=720x1280&g_mac=080027d3308b&location_time=1501494083&city_id=2&id=22004&zchtauth=7902940G7rGwUXE3Lhq5S7qcMT3iW%252BJOEBEGYKaXnf9avrAuA&shopId=14910&name=%E9%B2%9C%E6%9E%9C%E5%A4%A9%E5%A4%A9%E7%89%B9%E4%BB%B7&addrid=6336477&location_hash=2e1560Lb%2Ba1Ktwsj9UoAem08dU0LkcLJKQuprxRseDNacb5RHz2clr27gVS56N6rz9zUX7XDsv%2BKABV2IgUmFH&GPS_LatLng=115.574593%2C23.004691&g_ipAddress=172.17.99.15&hide_cart=0&isGotoNative=0&activitygroup=shuiguokuanghuanyue&__v=android6.3&random=-1126741866&g_platform=ANDROID&show_reload=1&cityid=2&zchtid=14910&g_imei=863756010208174&androidnewwebview=true&g_screen_points=720x1280&__d=d14jyDobSxt2feIyrPUvSTmr5gr7Af5RxVk3b1Ick%2FNPPZXr%2Bt86AJeF2SJocPklB2kCA15Qqx7ngCb5VtaFah2gj%2FyeyejH2B5hrfT4gGLb54PsbAjLppvX%2FkN9ig5IZ1vegVYY");

            AdItem adItem2 = new AdItem();
            adItem2.setIcon("http://img01.bqstatic.com//upload/activity/2017072718493789.jpg@90Q.jpg");
            adItem2.setName("舌尖上的红宝石");
            adItem2.setType(AdItem.TYPE_WEB);
            adItem2.setSkipUrl("http://m.beequick.cn/show/active?bigids=2%2C0&delivery_type=0&location=116.449732%2C39.926387&trackid=focus1%7C22018&g_screen_pixels=720x1280&g_mac=080027d3308b&location_time=1501494083&city_id=2&id=22018&zchtauth=7902940G7rGwUXE3Lhq5S7qcMT3iW%252BJOEBEGYKaXnf9avrAuA&shopId=14910&name=%E8%88%8C%E5%B0%96%E4%B8%8A%E7%9A%84%E7%BA%A2%E5%AE%9D%E7%9F%B3&addrid=6336477&location_hash=2e1560Lb%2Ba1Ktwsj9UoAem08dU0LkcLJKQuprxRseDNacb5RHz2clr27gVS56N6rz9zUX7XDsv%2BKABV2IgUmFH&GPS_LatLng=115.574593%2C23.004691&g_ipAddress=172.17.99.15&hide_cart=0&isGotoNative=0&activitygroup=meiguochelizi&__v=android6.3&random=1653528286&g_platform=ANDROID&show_reload=1&cityid=2&zchtid=14910&g_imei=863756010208174&androidnewwebview=true&g_screen_points=720x1280&__d=d14jyDobSxt2feIyrPUvSTmr5gr7Af5RxVk3b1Ick%2FNPPZXr%2Bt86AJeF2SJocPklB2kCA15Qqx7ngCb5VtaFah2gj%2FyeyejH2B5hrfT4gGLb54PsbAjLppvX%2FkN9ig5IZ1vegVYY");

            AdItem adItem3 = new AdItem();
            adItem3.setIcon("http://img01.bqstatic.com//upload/activity/2017073111215592.jpg@90Q.jpg");
            adItem3.setName("东方黑珍珠");
            adItem3.setType(AdItem.TYPE_WEB);
            adItem3.setSkipUrl("http://m.beequick.cn/show/active?bigids=2%2C0&delivery_type=0&location=116.449732%2C39.926387&trackid=focus1%7C22008&g_screen_pixels=720x1280&g_mac=080027d3308b&location_time=1501494083&city_id=2&id=22008&zchtauth=7902940G7rGwUXE3Lhq5S7qcMT3iW%252BJOEBEGYKaXnf9avrAuA&shopId=14910&name=%E4%B8%9C%E6%96%B9%E9%BB%91%E7%8F%8D%E7%8F%A0&addrid=6336477&location_hash=2e1560Lb%2Ba1Ktwsj9UoAem08dU0LkcLJKQuprxRseDNacb5RHz2clr27gVS56N6rz9zUX7XDsv%2BKABV2IgUmFH&GPS_LatLng=115.574593%2C23.004691&g_ipAddress=172.17.99.15&hide_cart=0&isGotoNative=0&activitygroup=xiaheiputao&__v=android6.3&random=-1827693368&g_platform=ANDROID&show_reload=1&cityid=2&zchtid=14910&g_imei=863756010208174&androidnewwebview=true&g_screen_points=720x1280&__d=d14jyDobSxt2feIyrPUvSTmr5gr7Af5RxVk3b1Ick%2FNPPZXr%2Bt86AJeF2SJocPklB2kCA15Qqx7ngCb5VtaFah2gj%2FyeyejH2B5hrfT4gGLb54PsbAjLppvX%2FkN9ig5IZ1vegVYY");


            AdItem adItem4 = new AdItem();
            adItem4.setIcon("http://img01.bqstatic.com//upload/activity/2017071410573397.jpg@90Q.jpg");
            adItem4.setName("下午茶分享活动");
            adItem4.setType(AdItem.TYPE_WEB);
            adItem4.setSkipUrl("http://m.beequick.cn/tea/index.html?cookie_id=&as_id=&__d=d14jyDobSxt2feIyrPUvSTmr5gr7Af5RxVk3b1Ick%2FNPPZXr%2Bt86AJeF2SJocPklB2kCA15Qqx7ngCb2UtSHbBSjjqydyLzE2E1mofTx2jLb6tK7PQvLqcbXr0R42gxIZ1vegVYY&zchtauth=7902940G7rGwUXE3Lhq5S7qcMT3iW%252BJOEBEGYKaXnf9avrAuA&location_hash=2e1560Lb%2Ba1Ktwsj9UoAem08dU0LkcLJKQuprxRseDNacb5RHz2clr27gVS56N6rz9zUX7XDsv%2BKABV2IgUmFH&bigids=2%2C0&__v=android6.3&zchtid=14910&delivery_type=0&location=116.449732%2C39.926387&trackid=focus1%7C21927&g_screen_pixels=720x1280&g_mac=080027d3308b&location_time=1501494083&city_id=2&id=21927&shopId=14910&name=%E4%B8%8B%E5%8D%88%E8%8C%B6%E5%88%86%E4%BA%AB%E6%B4%BB%E5%8A%A8&addrid=6336477&GPS_LatLng=115.574593%2C23.004691&g_ipAddress=172.17.99.15&hide_cart=1&isGotoNative=0&activitygroup=xiawuchafenxianghuodong&random=692858800&g_platform=ANDROID&show_reload=0&cityid=2&g_imei=863756010208174&androidnewwebview=true&g_screen_points=720x1280#/");

            headerBannerList.clear();
            headerBannerList.add(adItem);
            headerBannerList.add(adItem2);
            headerBannerList.add(adItem3);
            headerBannerList.add(adItem4);


//            if (isRefreshing) {
//                listView.onRefreshComplete();
//                isRefreshing = false;
//            }
            // 请求讯飞广告
            initBannerAd();
            refreshView();
        } else if (TextUtils.equals(tag, LOGIN_USER)) {
            DLog.i("lilijun", "登录成功！！！！@！@！@！");
            // 登陆成功
            ParseUtil.parseLoginUserInfoResult(resultObject);
            // 加载首页数据
            loadDataGet(GET_HOME_DATA_TAG, null);
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        DLog.i("tag----->>" + tag);
        if (TextUtils.equals(tag, GET_HOME_DATA_TAG)) {
            // 首页数据加载失败
//            if (!isRefreshing) {
            // 不是下拉刷新，是首次进入加载数据失败
            showErrorView();
//            } else {
            // 下拉刷新失败
//                listView.onRefreshComplete();
//                isRefreshing = false;
//                Util.showErrorMessage(getActivity(), msg, getResources().getString(R.string
//                        .refresh_fail));
//            }
        } else if (TextUtils.equals(tag, LOGIN_USER)) {
            // 登陆失败
            // 重置UserInfo 信息
            DLog.i("lilijun", "登录失败！！！！@！@！@！");
            UserInfoManager.getInstance().setUserInfo(new UserInfo());
            // 加载首页数据
            loadDataGet(GET_HOME_DATA_TAG, null);
        }
    }

    @Override
    protected void tryAgain() {
        super.tryAgain();
        String loginInfoByPwd = PreferencesUtils.getString(Constants
                .LOGIN_INFO_BY_PWD);
        if (!TextUtils.isEmpty(loginInfoByPwd)) {
            // 有通过账号密码登陆过的信息,则去登陆(每次进入都需要登陆)
            HashMap<String, String> loginParams = new HashMap<>();
            loginParams.put("parms", loginInfoByPwd);
            loadDataPost(LOGIN_USER, loginParams);
        } else {
            loadDataGet(GET_HOME_DATA_TAG, null);
        }
    }


    private void refreshView() {
//        titleImg.setImageURI(logoUrl);
        adapter.notifyDataSetChanged();
        // 展开所有item
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            listView.expandGroup(i);
        }
//        addRecommendView();
        //banner
        if (headerBannerList.isEmpty()) {
            headerBannerPagerView.setVisibility(View.GONE);
        } else {
            headerBannerPagerView.setVisibility(View.VISIBLE);
            headerBannerAdapter.notifyDataSetChanged();
        }
        showCenterView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void initBannerAd() {
        bannerAd = new IFLYNativeAd(getActivity(), "09840B44546AA78634331F0B5AADDDAC", bannerAdListener);
        bannerAd.loadAd(1);
    }

    private IFLYNativeListener bannerAdListener = new IFLYNativeListener() {

        @Override
        public void onConfirm() {

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onADLoaded(List<NativeADDataRef> list) {
            DLog.i("llj", "banner广告请求成功！！！");
            final NativeADDataRef nativeADDataRef = list.get(0);
            DLog.i("llj", "icon----->>>" + nativeADDataRef.getIcon());
            DLog.i("llj", "imageUrl----->>>" + nativeADDataRef.getImage());

            bannerLay.setVisibility(View.VISIBLE);
            nativeADDataRef.onExposured(adBannerImg);
            adBannerImg.setImageURI(nativeADDataRef.getImage());
            if (!TextUtils.isEmpty(nativeADDataRef.getAdSourceMark())) {
                adTips.setText(nativeADDataRef.getAdSourceMark() + "|广告");
            }
            adBannerImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nativeADDataRef.onClicked(view);
                }
            });
        }

        @Override
        public void onAdFailed(AdError adError) {
            DLog.e("llj", "banner广告请求错误，code--->>>" + adError.getErrorCode() + "描述---->>>" + adError.getErrorDescription());
        }
    };


    /**
     * 顶部banner item点击处理
     */
    private void doBannerItemClickListener(AdItem adItem) {
        switch (adItem.getType()) {
            case AdItem.TYPE_GROUP_MORE:
                // 楼层更多
                DLog.i("跳转至楼层更多！！");
                MoreGoodsActivity.startActivityByStoreId(getActivity(), adItem.getStoreId());
                break;
            case AdItem.TYPE_VIP_SPC:
                // 会员专卡
                DLog.i("跳转至会员专卡！！");
                ToBeVipActivity.startActivity(getActivity());
                break;
            case AdItem.TYPE_TOP_UP:
                // 消费充值
                DLog.i("跳转至消费充值！！");
                TopUpActivity.startActivity(getActivity());
                break;
            case AdItem.TYPE_GOODS_DETAIL:
                // 商品详情
                DLog.i("跳转至商品详情！！");
                GoodsDetailActivity.startActivity(getActivity(), adItem.getGoodsId());
                break;
            case AdItem.TYPE_CLASSIFY_GOODS:
                // 类目商品
                DLog.i("跳转至类目商品！！");
                SearchActivity.startActivity(getActivity(), adItem.getStoreId());
                break;
            case AdItem.TYPE_WEB:
                // 网页web
                DLog.i("跳转至网页web！！");
                if (TextUtils.equals(adItem.getSkipUrl().trim(), "#")) {
                    Util.showToast(getActivity(), getResources().getString(R.string.invalid_url));
                    return;
                }
                WebActivity.startActivity(getActivity(), adItem.getSkipUrl(), adItem.getName());
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_title_search_btn:
                // 标题搜索按钮
                // 隐藏键盘
                Util.hideInput(getActivity(), searchInput);
                SearchActivity.startActivity(getActivity(), searchInput.getText().toString());
                break;
//            case R.id.home_header_hot_more:
//                // 热推商品更多
//                MoreGoodsActivity.startActivityByRecommendTitle(getActivity(),
//                        headerRecommendTitle);
//                break;
            case R.id.home_tab_ad_0:
                // 新人专区
                NewExclusiveActivity.startActivity(getActivity());
                break;
            case R.id.home_tab_ad_1:
                // 消费充值
                TopUpActivity.startActivity(getActivity());
                break;
            case R.id.home_tab_ad_2:
                // 会员专卡
                if (UserInfoManager.getInstance().isValidUserInfo()) {
                    ToBeVipActivity.startActivity(getActivity());
                } else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.home_tab_ad_3:
                // 蜜蜂招募
                LoginActivity.startActivity(getActivity());
                break;
        }
    }

//    @Subscribe
//    public void onEvent(String msg){
//        Log.i("lilijun","接收到发送过来的消息-onEvent---->>"+msg);
//    }
}
