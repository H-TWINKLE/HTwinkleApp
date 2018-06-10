package com.twinkle.htwinkle.Adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.bean.ViewTypes;

import java.util.List;

public class MenuHeaderAndFooterAdapter extends BaseQuickAdapter<ViewTypes, BaseViewHolder> {

    public MenuHeaderAndFooterAdapter(List<ViewTypes> list) {
        super(list);
        setMultiTypeDelegate(new MultiTypeDelegate<ViewTypes>() {
            @Override
            protected int getItemType(ViewTypes s) {
                return s.getType();
            }
        });

        getMultiTypeDelegate()
                .registerItemType(ViewTypes.Title_TYPE, R.layout.side_content_rv)
                .registerItemType(ViewTypes.Div_TYPE, R.layout.side_content_div)
                .registerItemType(ViewTypes.DivHeader_TYPE, R.layout.side_content_div_head);

    }

    @Override
    protected void convert(BaseViewHolder helper, ViewTypes item) {
        switch (helper.getItemViewType()) {
            case ViewTypes.Title_TYPE:
                helper.setText(R.id.main_side_tv_title, item.getMenuTitle());
                helper.setImageResource(R.id.main_side_iv_icon,item.getMenuIcon());
                if(item.getNewTip()){
                    View view = helper.getView(R.id.main_side_iv_newTip);
                    view.setVisibility(View.VISIBLE);
                }
                break;
            case ViewTypes.Div_TYPE:
                break;
            case ViewTypes.DivHeader_TYPE:
                helper.setText(R.id.main_side_tv_head, item.getMenuTitle());
        }

    }


}
