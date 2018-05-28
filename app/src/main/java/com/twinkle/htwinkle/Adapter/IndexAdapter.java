package com.twinkle.htwinkle.Adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.twinkle.htwinkle.R;

import java.util.List;

public class IndexAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public IndexAdapter(int layout,List<String> list) {
        super(layout,list);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        helper.setText(R.id.index_cr_tv_username,item);

}


}
