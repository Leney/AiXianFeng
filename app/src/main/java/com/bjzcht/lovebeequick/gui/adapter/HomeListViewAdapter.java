package com.bjzcht.lovebeequick.gui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import com.bjzcht.lovebeequick.R;
import com.bjzcht.lovebeequick.gui.activity.GoodsDetailActivity;
import com.bjzcht.lovebeequick.gui.activity.MoreGoodsActivity;
import com.bjzcht.lovebeequick.gui.activity.SearchActivity;
import com.bjzcht.lovebeequick.gui.activity.ToBeVipActivity;
import com.bjzcht.lovebeequick.gui.activity.TopUpActivity;
import com.bjzcht.lovebeequick.gui.activity.WebActivity;
import com.bjzcht.lovebeequick.gui.view.CenterLineTextView;
import com.bjzcht.lovebeequick.gui.view.SimpleDraweeViewLab;
import com.bjzcht.lovebeequick.model.AdItem;
import com.bjzcht.lovebeequick.model.Goods;
import com.bjzcht.lovebeequick.model.HomeGroupItem;
import com.bjzcht.lovebeequick.util.Util;
import com.bjzcht.lovebeequick.util.log.DLog;

/**
 * 首页列表的Adatper
 * Created by liijun on 2016/8/22.
 */
public class HomeListViewAdapter extends BaseExpandableListAdapter {
    /**
     * 商品类型
     */
    public static final int GOODS_TYPE = 1;
    /**
     * 商品广告类型
     */
    public static final int AD_TYPE = 2;
    /**
     * 热销推荐类型
     */
    public static final int HOT_TYPE = 3;
    private List<HomeGroupItem> groupList;
    private List<List<Goods[]>> childList;

    public HomeListViewAdapter(List<HomeGroupItem> groupList, List<List<Goods[]>> childList) {
        this.groupList = groupList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return childList.get(i).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        if (childList.get(groupPosition).get(childPosition)[0].getType() == Goods.GOODS_TYPE) {
            return GOODS_TYPE;
        } else if (childList.get(groupPosition).get(childPosition)[0].getType() == Goods.HOT_TYPE) {
            return HOT_TYPE;
        } else {
            return AD_TYPE;
        }
    }

    @Override
    public int getChildTypeCount() {
        return 4;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupHolderView groupHolderView = null;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.home_classify_group_item, null);
            groupHolderView = new GroupHolderView(view);
            groupHolderView.more.setOnClickListener(groupMoreClickListener);
            groupHolderView.groupLay.setOnClickListener(groupOtherLayClickListener);
            view.setTag(groupHolderView);
        } else {
            groupHolderView = (GroupHolderView) view.getTag();
        }
        HomeGroupItem item = groupList.get(i);
        groupHolderView.name.setText(item.getName());
        groupHolderView.more.setTag(item);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view,
                             ViewGroup viewGroup) {
        ChildGoodsHolderView goodsHolderView = null;
        ChildAdHolderView adHolderView = null;
        ChildHotGoodHolderView hotGoodHolderView = null;
        int type = getChildType(groupPosition, childPosition);
        if (view == null) {
            if (type == GOODS_TYPE) {
                // 商品类型
                view = View.inflate(viewGroup.getContext(), R.layout.home_classify_child_item_1,
                        null);
                goodsHolderView = new ChildGoodsHolderView(view);
                view.setTag(goodsHolderView);
            } else if (type == AD_TYPE) {
                //商品广告类型
                view = View.inflate(viewGroup.getContext(), R.layout.home_classify_child_item_2,
                        null);
                adHolderView = new ChildAdHolderView(view);
                view.setTag(adHolderView);
            } else if (type == HOT_TYPE) {
                // 热销推荐商品
                view = View.inflate(viewGroup.getContext(), R.layout.hot_goods_item,null);
                hotGoodHolderView = new ChildHotGoodHolderView(view);
                view.setTag(hotGoodHolderView);
            }
        } else {
            if (type == GOODS_TYPE) {
                goodsHolderView = (ChildGoodsHolderView) view.getTag();
            } else if (type == AD_TYPE) {
                adHolderView = (ChildAdHolderView) view.getTag();
            } else if (type == HOT_TYPE) {
                hotGoodHolderView = (ChildHotGoodHolderView) view.getTag();
            }
        }
        if (type == GOODS_TYPE) {
            // 商品类型
            Goods[] childItem = (Goods[]) getChild(groupPosition, childPosition);
            //左边子item的视图
            goodsHolderView.leftIcon.setImageURI(childItem[0].getIcon());
            goodsHolderView.leftName.setText(childItem[0].getName());
//            goodsHolderView.leftMarketPrice1.setText(String.format(viewGroup.getResources()
//                    .getString(R.string.format_money), childItem[0].getMarketPrice() + ""));
//            goodsHolderView.leftPrice1.setText(String.format(viewGroup.getResources().getString(R
//                    .string.format_money), childItem[0].getPrice()));
            goodsHolderView.leftLay.setTag(childItem[0]);
            if (childItem[0].getSalesLevel() == 1) {
                // 售卖新人价，显示市场价和新人价
                goodsHolderView.leftPrice1.setVisibility(View.GONE);
                goodsHolderView.leftPrice2.setVisibility(View.VISIBLE);
                goodsHolderView.leftMarketPrice1.setVisibility(View.VISIBLE);
                goodsHolderView.leftMarketPrice2.setVisibility(View.GONE);
//                goodsHolderView.leftVipLab.setVisibility(View.GONE);
                goodsHolderView.leftIcon.showLab(false);

                goodsHolderView.leftPrice2.setText(String.format(viewGroup.getResources()
                        .getString(R.string.format_new_money), childItem[0].getNewbiePrice() + ""));
                goodsHolderView.leftMarketPrice1.setText(String.format(viewGroup.getResources()
                        .getString(R.string.format_money), childItem[0].getMarketPrice() + ""));
            } else if (childItem[0].getSalesLevel() == 2) {
                // 只显示售卖会员价
                goodsHolderView.leftPrice1.setVisibility(View.VISIBLE);
                goodsHolderView.leftPrice2.setVisibility(View.GONE);
                goodsHolderView.leftMarketPrice1.setVisibility(View.GONE);
                goodsHolderView.leftMarketPrice2.setVisibility(View.GONE);
//                goodsHolderView.leftVipLab.setVisibility(View.VISIBLE);
                goodsHolderView.leftIcon.showLab(true);

                goodsHolderView.leftPrice1.setText(String.format(viewGroup.getResources()
                        .getString(R.string.format_money), childItem[0].getMemberPrice() + ""));
            } else if (childItem[0].getSalesLevel() == 3) {
                // 显示非会员价和会员价
                goodsHolderView.leftPrice1.setVisibility(View.VISIBLE);
                goodsHolderView.leftPrice2.setVisibility(View.VISIBLE);
                goodsHolderView.leftMarketPrice1.setVisibility(View.GONE);
                goodsHolderView.leftMarketPrice2.setVisibility(View.GONE);
//                goodsHolderView.leftVipLab.setVisibility(View.GONE);
                goodsHolderView.leftIcon.showLab(false);

                goodsHolderView.leftPrice1.setText(String.format(viewGroup.getResources()
                        .getString(R.string.format_money), childItem[0].getPrice() + ""));
                goodsHolderView.leftPrice2.setText(String.format(viewGroup.getResources()
                        .getString(R.string.format_vip_money), childItem[0].getMemberPrice() + ""));
            } else {
                // 显示非会员价和市场价
                goodsHolderView.leftPrice1.setVisibility(View.VISIBLE);
                goodsHolderView.leftPrice2.setVisibility(View.GONE);
                goodsHolderView.leftMarketPrice1.setVisibility(View.GONE);
                goodsHolderView.leftMarketPrice2.setVisibility(View.VISIBLE);
//                goodsHolderView.leftVipLab.setVisibility(View.GONE);
                goodsHolderView.leftIcon.showLab(false);

                goodsHolderView.leftPrice1.setText(String.format(viewGroup.getResources()
                        .getString(R.string.format_money), childItem[0].getPrice()));
                goodsHolderView.leftMarketPrice2.setText(String.format(viewGroup.getResources()
                        .getString(R.string.format_money), childItem[0].getMarketPrice() + ""));
            }
            goodsHolderView.rightLay.setVisibility(View.INVISIBLE);
            if (childItem[1] != null) {
                //证明有下一个数据
                //右边子item的视图
                goodsHolderView.rightIcon.setImageURI(childItem[1].getIcon());
                goodsHolderView.rightName.setText(childItem[1].getName());
//                goodsHolderView.rightMarketPrice1.setText(String.format(viewGroup.getResources()
//                        .getString(R.string.format_money), childItem[1].getMarketPrice() + ""));
//                goodsHolderView.rightPrice1.setText(String.format(viewGroup.getResources()
//                        .getString(R.string.format_money), childItem[1].getPrice()));
                goodsHolderView.rightLay.setTag(childItem[1]);
                goodsHolderView.rightLay.setVisibility(View.VISIBLE);

                if (childItem[1].getSalesLevel() == 1) {
                    // 售卖新人价，显示市场价和新人价
                    goodsHolderView.rightPrice1.setVisibility(View.GONE);
                    goodsHolderView.rightPrice2.setVisibility(View.VISIBLE);
                    goodsHolderView.rightMarketPrice1.setVisibility(View.VISIBLE);
                    goodsHolderView.rightMarketPrice2.setVisibility(View.GONE);
//                    goodsHolderView.rightVipLab.setVisibility(View.GONE);
                    goodsHolderView.rightIcon.showLab(false);

                    goodsHolderView.rightPrice2.setText(String.format(viewGroup.getResources()
                            .getString(R.string.format_new_money), childItem[1].getNewbiePrice()
                            + ""));
                    goodsHolderView.rightMarketPrice1.setText(String.format(viewGroup.getResources()
                            .getString(R.string.format_money), childItem[1].getMarketPrice() + ""));
                } else if (childItem[1].getSalesLevel() == 2) {
                    // 只显示售卖会员价
                    goodsHolderView.rightPrice1.setVisibility(View.VISIBLE);
                    goodsHolderView.rightPrice2.setVisibility(View.GONE);
                    goodsHolderView.rightMarketPrice1.setVisibility(View.GONE);
                    goodsHolderView.rightMarketPrice2.setVisibility(View.GONE);
//                    goodsHolderView.rightVipLab.setVisibility(View.VISIBLE);
                    goodsHolderView.rightIcon.showLab(true);

                    goodsHolderView.rightPrice1.setText(String.format(viewGroup.getResources()
                            .getString(R.string.format_money), childItem[1].getMemberPrice()
                            + ""));
                } else if (childItem[1].getSalesLevel() == 3) {
                    // 显示非会员价和会员价
                    goodsHolderView.rightPrice1.setVisibility(View.VISIBLE);
                    goodsHolderView.rightPrice2.setVisibility(View.VISIBLE);
                    goodsHolderView.rightMarketPrice1.setVisibility(View.GONE);
                    goodsHolderView.rightMarketPrice2.setVisibility(View.GONE);
//                    goodsHolderView.rightVipLab.setVisibility(View.GONE);
                    goodsHolderView.rightIcon.showLab(false);

                    goodsHolderView.rightPrice1.setText(String.format(viewGroup.getResources()
                            .getString(R.string.format_money), childItem[1].getPrice() + ""));
                    goodsHolderView.rightPrice2.setText(String.format(viewGroup.getResources()
                            .getString(R.string.format_vip_money), childItem[1].getMemberPrice()
                            + ""));
                } else {
                    // 显示非会员价和市场价
                    goodsHolderView.rightPrice1.setVisibility(View.VISIBLE);
                    goodsHolderView.rightPrice2.setVisibility(View.GONE);
                    goodsHolderView.rightMarketPrice1.setVisibility(View.GONE);
                    goodsHolderView.rightMarketPrice2.setVisibility(View.VISIBLE);
//                    goodsHolderView.rightVipLab.setVisibility(View.GONE);
                    goodsHolderView.rightIcon.showLab(false);

                    goodsHolderView.rightPrice1.setText(String.format(viewGroup.getResources()
                            .getString(R.string.format_money), childItem[1].getPrice()));
                    goodsHolderView.rightMarketPrice2.setText(String.format(viewGroup.getResources()
                            .getString(R.string.format_money), childItem[1].getMarketPrice() + ""));
                }
            }
        } else if (type == AD_TYPE) {
            // 商品广告类型
            Goods[] childItem = (Goods[]) getChild(groupPosition, childPosition);
            adHolderView.adImg.setTag(childItem[0].getAdItem());
            adHolderView.adImg.setImageURI(childItem[0].getAdItem().getIcon());
        } else if (type == HOT_TYPE) {
            Goods[] childItem = (Goods[]) getChild(groupPosition, childPosition);
            hotGoodHolderView.hotImg.setImageURI(childItem[0].getIcon());
            hotGoodHolderView.hotName.setText(childItem[0].getName());
            if (childItem[0].getSalesLevel() == 1) {
                // 售卖新人价，显示市场价和新人价
                hotGoodHolderView.leftPrice.setVisibility(View.INVISIBLE);
                hotGoodHolderView.rightPrice.setVisibility(View.VISIBLE);
                hotGoodHolderView.leftMarketPrice.setVisibility(View.VISIBLE);
                hotGoodHolderView.rightMarketPrice.setVisibility(View.INVISIBLE);
                hotGoodHolderView.hotImg.showLab(false);

                hotGoodHolderView.rightPrice.setText(String.format(viewGroup.getResources()
                        .getString(R.string
                                .format_new_money), childItem[0].getNewbiePrice() + ""));
                hotGoodHolderView.leftMarketPrice.setText(String.format(viewGroup.getResources()
                        .getString(R.string
                                .format_money), childItem[0].getMarketPrice() + ""));
            } else if (childItem[0].getSalesLevel() == 2) {
                // 只显示售卖会员价
                hotGoodHolderView.leftPrice.setVisibility(View.VISIBLE);
                hotGoodHolderView.rightPrice.setVisibility(View.GONE);
                hotGoodHolderView.leftMarketPrice.setVisibility(View.GONE);
                hotGoodHolderView.rightMarketPrice.setVisibility(View.GONE);
                hotGoodHolderView.hotImg.showLab(true);

                hotGoodHolderView.leftPrice.setText(String.format(viewGroup.getResources()
                        .getString(R.string
                                .format_vip_money), childItem[0].getMemberPrice() + ""));
            } else if (childItem[0].getSalesLevel() == 3) {
                // 显示非会员价和会员价
                hotGoodHolderView.leftPrice.setVisibility(View.VISIBLE);
                hotGoodHolderView.rightPrice.setVisibility(View.VISIBLE);
                hotGoodHolderView.leftMarketPrice.setVisibility(View.GONE);
                hotGoodHolderView.rightMarketPrice.setVisibility(View.GONE);
                hotGoodHolderView.hotImg.showLab(false);

                hotGoodHolderView.leftPrice.setText(String.format(viewGroup.getResources()
                        .getString(R.string
                                .format_money), childItem[0].getPrice() + ""));
                hotGoodHolderView.rightPrice.setText(String.format(viewGroup.getResources()
                        .getString(R.string
                                .format_vip_money), childItem[0].getMemberPrice() + ""));
            } else {
                // 显示非会员价和市场价
                hotGoodHolderView.leftPrice.setVisibility(View.VISIBLE);
                hotGoodHolderView.rightPrice.setVisibility(View.GONE);
                hotGoodHolderView.leftMarketPrice.setVisibility(View.GONE);
                hotGoodHolderView.rightMarketPrice.setVisibility(View.VISIBLE);
                hotGoodHolderView.hotImg.showLab(false);

                hotGoodHolderView.leftPrice.setText(String.format(viewGroup.getResources()
                        .getString(R.string
                                .format_money), childItem[0].getPrice()));
                hotGoodHolderView.rightMarketPrice.setText(String.format(viewGroup.getResources()
                        .getString(R.string
                                .format_money), childItem[0].getMarketPrice() + ""));
            }
            hotGoodHolderView.hotLay.setTag(childItem[0]);
//            recommendItemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    GoodsDetailActivity.startActivity(getActivity(), goods.getId());
//                }
//            });
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }


    class GroupHolderView {
        public TextView name;
        public TextView more;
        public RelativeLayout groupLay;

        public void init(View baseView) {
            groupLay = (RelativeLayout) baseView.findViewById(R.id.home_group_lay);
            name = (TextView) baseView.findViewById(R.id.home_group_name);
            more = (TextView) baseView.findViewById(R.id.home_group_more);
        }

        public GroupHolderView(View baseView) {
            init(baseView);
        }
    }

    class ChildGoodsHolderView {
        public RelativeLayout leftLay, rightLay;
        public SimpleDraweeViewLab leftIcon, rightIcon;
        //        public ImageView leftVipLab, rightVipLab;
        public TextView leftName, rightName;
        public TextView leftPrice1, leftPrice2, rightPrice1, rightPrice2;
        public CenterLineTextView leftMarketPrice1, leftMarketPrice2, rightMarketPrice1,
                rightMarketPrice2;

        public void init(View baseView) {
            leftIcon = (SimpleDraweeViewLab) baseView.findViewById(R.id.home_chlid_icon1);
//            leftVipLab = (ImageView) baseView.findViewById(R.id.home_child_vip_lab1);
            leftName = (TextView) baseView.findViewById(R.id.home_child_name1);
            leftPrice1 = (TextView) baseView.findViewById(R.id.home_child_left_item_price_left1);
            leftPrice2 = (TextView) baseView.findViewById(R.id.home_child_left_item_price_right1);
            leftMarketPrice1 = (CenterLineTextView) baseView.findViewById(R.id
                    .home_child_left_item_market_price_left1);
            leftMarketPrice2 = (CenterLineTextView) baseView.findViewById(R.id
                    .home_child_left_item_market_price_right1);
            leftLay = (RelativeLayout) baseView.findViewById(R.id.home_child_left_lay);

            rightIcon = (SimpleDraweeViewLab) baseView.findViewById(R.id.home_chlid_icon2);
//            rightVipLab = (ImageView) baseView.findViewById(R.id.home_child_vip_lab2);
            rightName = (TextView) baseView.findViewById(R.id.home_child_name2);
            rightPrice1 = (TextView) baseView.findViewById(R.id.home_child_right_item_price_left2);
            rightPrice2 = (TextView) baseView.findViewById(R.id.home_child_right_item_price_right2);
            rightMarketPrice1 = (CenterLineTextView) baseView.findViewById(R.id
                    .home_child_right_item_market_price_left2);
            rightMarketPrice2 = (CenterLineTextView) baseView.findViewById(R.id
                    .home_child_right_item_market_price_right2);
            rightLay = (RelativeLayout) baseView.findViewById(R.id.home_child_right_lay);

            leftLay.setOnClickListener(childItemClickListener);
            rightLay.setOnClickListener(childItemClickListener);
        }

        public ChildGoodsHolderView(View baseView) {
            init(baseView);
        }
    }

    class ChildAdHolderView {
        public SimpleDraweeView adImg;

        public void init(View baseView) {
            adImg = (SimpleDraweeView) baseView.findViewById(R.id.home_child_ad_img);
            adImg.setOnClickListener(childAdClickListener);
        }

        public ChildAdHolderView(View baseView) {
            init(baseView);
        }
    }

    class ChildHotGoodHolderView {
        public RelativeLayout hotLay;
        public SimpleDraweeViewLab hotImg;
        public TextView hotName;
        public TextView leftPrice;
        public TextView rightPrice;
        public CenterLineTextView leftMarketPrice;
        public CenterLineTextView rightMarketPrice;

        public void init(View baseView) {
            hotLay = (RelativeLayout) baseView.findViewById(R.id.hot_item_lay);
            hotImg = (SimpleDraweeViewLab) baseView.findViewById(R.id.hot_item_img);
            hotName = (TextView) baseView.findViewById(R.id.hot_item_goods_name);
            leftPrice = (TextView) baseView.findViewById(R.id.hot_item_price_left);
            rightPrice = (TextView) baseView.findViewById(R.id.hot_item_price_right);
            leftMarketPrice = (CenterLineTextView) baseView.findViewById(R.id
                    .hot_item_market_price_left);
            rightMarketPrice = (CenterLineTextView) baseView.findViewById(R.id
                    .hot_item_market_price_right);
            hotLay.setOnClickListener(childItemClickListener);
        }

        public ChildHotGoodHolderView(View baseView) {
            init(baseView);
        }
    }

    /**
     * "更多" 点击事件对象
     */
    private View.OnClickListener groupMoreClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            HomeGroupItem homeGroupItem = (HomeGroupItem) view.getTag();
            if (homeGroupItem.getType() == HomeGroupItem.HOT_TYPE) {
                MoreGoodsActivity.startActivityByRecommendTitle(view.getContext(), homeGroupItem);
            } else {
                MoreGoodsActivity.startActivityByClassify(view.getContext(), homeGroupItem);
            }
        }
    };

    /**
     * "更多" 点击事件对象
     */
    private View.OnClickListener groupOtherLayClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DLog.i("lilijun", "点击group，不做任何事,达到点击groupItem 不收缩的效果");
        }
    };

    /**
     * 商品类型item 点击事件
     */
    private View.OnClickListener childItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Goods goods = (Goods) view.getTag();
            DLog.i("lilijun", "商品类型item点击，goods.getName()---->>>" + goods.getName());
            DLog.i("lilijun", "商品类型item点击，goods.getId()---->>>" + goods.getId());
            GoodsDetailActivity.startActivity(view.getContext(), goods.getId());
        }
    };

    /**
     * 广告类型item 点击事件
     */
    private View.OnClickListener childAdClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AdItem adItem = (AdItem) view.getTag();
            doAdClickListener(view.getContext(), adItem);
        }
    };

    /**
     * 顶部banner item点击处理
     */
    private void doAdClickListener(Context context, AdItem adItem) {
        switch (adItem.getType()) {
            case AdItem.TYPE_GROUP_MORE:
                // 楼层更多
                DLog.i("跳转至楼层更多！！");
                MoreGoodsActivity.startActivityByStoreId(context, adItem.getStoreId());
                break;
            case AdItem.TYPE_VIP_SPC:
                // 会员专卡
                DLog.i("跳转至会员专卡！！");
                ToBeVipActivity.startActivity(context);
                break;
            case AdItem.TYPE_TOP_UP:
                // 消费充值
                DLog.i("跳转至消费充值！！");
                TopUpActivity.startActivity(context);
                break;
            case AdItem.TYPE_GOODS_DETAIL:
                // 商品详情
                DLog.i("跳转至商品详情！！");
                GoodsDetailActivity.startActivity(context, adItem.getGoodsId());
                break;
            case AdItem.TYPE_CLASSIFY_GOODS:
                // 类目商品
                DLog.i("跳转至类目商品！！");
                SearchActivity.startActivity(context, adItem.getStoreId());
                break;
            case AdItem.TYPE_WEB:
                // 网页web
                DLog.i("跳转至网页web！！");
                DLog.i("skipUrl----->>>" + adItem.getSkipUrl());
                if (TextUtils.equals(adItem.getSkipUrl().trim(), "#")) {
                    Util.showToast(context, context.getResources().getString(R.string.invalid_url));
                    return;
                }
                WebActivity.startActivity(context, adItem.getSkipUrl(), context.getResources()
                        .getString(R.string.web_page));
                break;
            default:
                break;
        }
    }
}
