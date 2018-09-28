package com.twinkle.htwinkle.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.twinkle.htwinkle.R;

import java.util.List;

public class JwglTtbAdapter extends BaseAdapter {

    private Context context;

    private List<String> list;

    public JwglTtbAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_ttb_content, null);
        }
        TextView textView = convertView.findViewById(R.id.text);
        if (!TextUtils.isEmpty(list.get(i))) {
            textView.setText((list.get(i)));
            textView.setTextColor(Color.WHITE);
            int rand = i % 6;
            switch (rand) {
                case 0:
                    textView.setBackground(context.getResources().getDrawable(R.drawable.bg_1));
                    break;
                case 1:
                    textView.setBackground(context.getResources().getDrawable(R.drawable.bg_2));
                    break;
                case 2:
                    textView.setBackground(context.getResources().getDrawable(R.drawable.bg_3));
                    break;
                case 3:
                    textView.setBackground(context.getResources().getDrawable(R.drawable.bg_4));
                    break;
                case 4:
                    textView.setBackground(context.getResources().getDrawable(R.drawable.bg_5));
                    break;
                case 5:
                    textView.setBackground(context.getResources().getDrawable(R.drawable.bg_6));
                    break;
                case 6:
                    textView.setBackground(context.getResources().getDrawable(R.drawable.bg_7));
                    break;
            }
        }
        return convertView;
    }
}
