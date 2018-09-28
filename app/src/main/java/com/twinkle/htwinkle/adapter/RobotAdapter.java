package com.twinkle.htwinkle.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.loopj.android.image.SmartImageView;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.entity.Talk;
import com.twinkle.htwinkle.init.Utils;

import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class RobotAdapter extends BaseMultiItemQuickAdapter<Talk, BaseViewHolder> {

    private SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    public RobotAdapter(List<Talk> data) {
        super(data);

        addItemType(Talk.sendByRobot, R.layout.item_talk_in_left);
        addItemType(Talk.sendByUser, R.layout.item_talk_in_rigtht);
    }

    @Override
    protected void convert(BaseViewHolder helper, Talk item) {

        switch (helper.getItemViewType()) {
            case Talk.sendByRobot:
                helper.setText(R.id.item_robot_date, sd.format(item.getDate()));
                helper.setText(R.id.item_robot_message, item.getRobot().getText() + "");
                break;

            case Talk.sendByUser:
                helper.setText(R.id.item_user_date, sd.format(item.getDate()));
                SmartImageView smartImageView = helper.getView(R.id.item_user_siv_icon);
                x.image().bind(smartImageView, item.getUser().getHeaderPic(), Utils.INSTANCE.ImageOptionsInCir());
                helper.setText(R.id.item_user_message, item.getRobot().getText() + "");
                break;


        }

    }
}
