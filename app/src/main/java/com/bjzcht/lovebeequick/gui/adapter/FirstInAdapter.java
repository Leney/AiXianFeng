package com.bjzcht.lovebeequick.gui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import java.util.List;

/**
 * Banner 轮播Adapter
 * Created by yb on 2016/8/19.
 */
public class FirstInAdapter extends LoopPagerAdapter {
    private List<Integer> bannerItems;

    public FirstInAdapter(RollPagerView viewPager, List<Integer> list) {
        super(viewPager);
        this.bannerItems = list;
    }

    @Override
    public View getView(ViewGroup container, int position) {
//        AdItem item = bannerItems.get(position);
//        SimpleDraweeView bannerImg = new SimpleDraweeView(container.getContext());
//        GenericDraweeHierarchy hierarchy = bannerImg.getHierarchy();
//        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
//        bannerImg.setLayoutParams(
//                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
//        bannerImg.setImageURI(item.getIcon());

        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setImageResource(bannerItems.get(position));
        return imageView;
    }

    @Override
    public int getRealCount() {
        return bannerItems.size();
    }
}
