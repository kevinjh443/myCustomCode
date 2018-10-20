package com.hogee.myapplicationdemo.viepagerswitchcard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hogee.myapplicationdemo.R;

import java.util.ArrayList;
import java.util.List;

public class SwitchFragment extends Fragment {

    private LinearLayout groupViewLl;
    private ViewPager viewPager;

    private ImageView[] imageViews;
    private List<View> viewList = new ArrayList<View>();

    private LayoutInflater mInflater;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View switchView = inflater.inflate(R.layout.fragment_switch, container, false);
        mInflater = inflater;
        groupViewLl = (LinearLayout) switchView.findViewById(R.id.viewGroup);
        viewPager = (ViewPager) switchView.findViewById(R.id.viewPager);

        return switchView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /**
         * 将需要滑动的View加入到viewList
         */
        View oneView = mInflater.inflate(R.layout.fragment_view_one, null);
        viewList.add(oneView);
        viewList.add(mInflater.inflate(R.layout.fragment_view_two, null));
        viewList.add(mInflater.inflate(R.layout.fragment_view_three, null));

        /**
         * 定义个圆点滑动导航ImageView，根据View的个数而定
         */
        imageViews = new ImageView[viewList.size()];

        for (int i = 0; i < viewList.size(); i++) {
            ImageView imageView = new ImageView(this.getActivity());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(20, 20));
            imageView.setPadding(20, 0, 20, 0);
            imageViews[i] = imageView;

            if (i == 0) { // 默认选中第一张图片
                imageViews[i] .setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.page_indicator);
            }
            groupViewLl.addView(imageViews[i]);
        }

        viewPager.setAdapter(new MyPagerAdapter(viewList));
        viewPager.setOnPageChangeListener(new SwitchPageChangeListener());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // 指引页面更改事件监听器，设置圆点滑动时的背景变化。
    class SwitchPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0] .setBackgroundResource(R.drawable.page_indicator_focused);
                if (arg0 != i) { imageViews[i] .setBackgroundResource(R.drawable.page_indicator);
                }
            }
        }
    }
}
