package com.example.drink_order_system;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Right_adapter extends RecyclerView.Adapter {

    private final ArrayList<Drinks> mList;
    private final LayoutInflater mLayoutInflater;
    private MyClickListener mListener;
    private Context mContext;

    Right_adapter(LayoutInflater layoutInflater, ArrayList<Drinks> list, Context context) {
        this.mList = list;
        this.mLayoutInflater = layoutInflater;
        this.mContext = context;
        System.out.println("rightAdapter used");
    }

    public void buttonSetOnClick(MyClickListener mListener) {
        this.mListener = mListener;
    }

    public interface MyClickListener {
        public void onclick(View v, int position);
    }

    private Drinks getItem(int position) {
        return mList.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //绑定xml组件
        return new RightViewHolder(
                mLayoutInflater.inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Drinks target = getItem(position);
        if (holder instanceof RightViewHolder) {
            ((RightViewHolder) holder).bindBean(target);

            // 根据类别设置按钮文本
            Button chooseButton = ((RightViewHolder) holder).chooseBt;
            if ("🎶 点歌".equals(target.get_type())) {
                chooseButton.setText("查找歌曲");
            } else {
                chooseButton.setText("选规格");
            }

            // 设置点击事件
            chooseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("🎶 点歌".equals(target.get_type())) {
                        // 点歌项点击事件
                        showSongSearchDialog();
                    } else if (mListener != null) {
                        // 普通商品点击事件
                        mListener.onclick(v, holder.getAdapterPosition());
                    }
                }
            });
        } else {
            throw new IllegalStateException("Illegal state Exception onBindviewHolder");
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void showSongSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = mLayoutInflater.inflate(R.layout.dialogue_song_search, null);
        builder.setView(view);

        ImageButton searchButton = view.findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 弹出提示“系统暂无曲目”
                Toast.makeText(mContext, "系统暂无曲目", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class RightViewHolder extends RecyclerView.ViewHolder {
        private final TextView drinkType;
        private final TextView drinkName;
        private final TextView drinkIntro;
        private final TextView drinkPrice;
        private Button chooseBt;
        private final ImageView drinkImg;

        RightViewHolder(View itemView) {
            super(itemView);
            drinkType = (TextView) itemView.findViewById(R.id.Text_drinkType);
            drinkName = (TextView) itemView.findViewById(R.id.Text_drinkName);
            drinkIntro = (TextView) itemView.findViewById(R.id.Text_drinkIntro);
            drinkPrice = (TextView) itemView.findViewById(R.id.Text_drinkPrice);
            drinkImg = (ImageView) itemView.findViewById(R.id.img_drink);
            chooseBt = (Button) itemView.findViewById(R.id.BT_choose);
        }

        void bindBean(final Drinks bean) {
            drinkName.setText(bean.get_name() + "  #" + (bean.get_number() + 1));
            if (bean.get_type() != null) {
                drinkType.setText(bean.get_type());
            } else {
                drinkType.setText(null);
            }
            drinkImg.setImageResource(bean.getImageResId());
            drinkPrice.setText(String.format("￥ %.0f", bean.get_price()));
            drinkIntro.setText(bean.get_introduction());
        }
    }
}