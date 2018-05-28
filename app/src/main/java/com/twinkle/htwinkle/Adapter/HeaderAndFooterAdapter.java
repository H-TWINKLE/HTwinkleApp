package com.twinkle.htwinkle.Adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.bean.ViewTypes;

import java.util.List;

public class HeaderAndFooterAdapter extends BaseQuickAdapter<ViewTypes, BaseViewHolder> {

    public HeaderAndFooterAdapter(List<ViewTypes> list) {
        super(list);
        setMultiTypeDelegate(new MultiTypeDelegate<ViewTypes>() {
            @Override
            protected int getItemType(ViewTypes s) {
                return s.type;
            }
        });

        getMultiTypeDelegate()
                .registerItemType(ViewTypes.Title_TYPE, R.layout.side_content_rv)
                .registerItemType(ViewTypes.Div_TYPE, R.layout.base_div);

    }

    @Override
    protected void convert(BaseViewHolder helper, ViewTypes item) {
        switch (helper.getItemViewType()) {
            case ViewTypes.Title_TYPE:
                helper.setText(R.id.main_side_tv_title, item.getTitle());
                break;
            case ViewTypes.Div_TYPE:
                break;
        }

    }


}
