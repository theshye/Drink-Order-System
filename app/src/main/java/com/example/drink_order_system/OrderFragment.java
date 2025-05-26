package com.example.drink_order_system;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderFragment extends Fragment {
    private ArrayList<Drinks> drinks_array = new ArrayList<Drinks>(); //可选的饮品列表
    private ArrayList<LeftBean> titles_array = new ArrayList<LeftBean>(); //饮品类别列表
    private RecyclerView right_listView; //右侧饮品列表
    private RecyclerView left_listView; //左侧类别列表
    private LinearLayoutManager right_llM;
    private TextView right_title;
    private SearchView searchView;

    private AlertDialog chooseDialog = null;
    private AlertDialog.Builder builder = null;
    private View view_choose;

    private Context mContext = this.getActivity();

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        SearchView mSearch = (SearchView) view.findViewById(R.id.my_search);
        int id = mSearch.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView text_search = (TextView) mSearch.findViewById(id);
        text_search.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        right_title = (TextView) view.findViewById(R.id.Top_drinkType);

        right_listView = (RecyclerView) view.findViewById(R.id.rec_right);
        left_listView = (RecyclerView) view.findViewById(R.id.rec_left);
        searchView = (SearchView) view.findViewById(R.id.my_search);
        builder = new AlertDialog.Builder(this.getActivity());
        view_choose = inflater.inflate(R.layout.dialogue_choose, null, false);
        builder.setView(view_choose);
        builder.setCancelable(false);
        chooseDialog = builder.create();

        view_choose.findViewById(R.id.button_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDialog.dismiss();
            }
        });

        view_choose.findViewById(R.id.button_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String size = "中杯";
                String temperature = "全冰";
                String sugar = "全糖";
                RadioGroup radiogroup = (RadioGroup) view_choose.findViewById(R.id.radioGroup_size);
                for (int i = 0; i < radiogroup.getChildCount(); i++) {
                    RadioButton rd = (RadioButton) radiogroup.getChildAt(i);
                    if (rd.isChecked()) {
                        size = String.valueOf(rd.getText());
                    }
                }
                radiogroup = (RadioGroup) view_choose.findViewById(R.id.radioGroup_ice);
                for (int i = 0; i < radiogroup.getChildCount(); i++) {
                    RadioButton rd = (RadioButton) radiogroup.getChildAt(i);
                    if (rd.isChecked()) {
                        temperature = String.valueOf(rd.getText());
                    }
                }
                radiogroup = (RadioGroup) view_choose.findViewById(R.id.radioGroup_sugar);
                for (int i = 0; i < radiogroup.getChildCount(); i++) {
                    RadioButton rd = (RadioButton) radiogroup.getChildAt(i);
                    if (rd.isChecked()) {
                        sugar = String.valueOf(rd.getText());
                    }
                }
                TextView drinkName = view_choose.findViewById(R.id.choose_drinkName);
                //写买进购物车的逻辑
                System.out.println("drinkName:" + String.valueOf(drinkName.getText()).split("  #")[0]);
                Drinks drink = new Drinks(Integer.parseInt(String.valueOf(drinkName.getText()).split("  #")[1]));
                Flavor flavor = new Flavor(size, temperature, sugar);
                TextView numberTV = (TextView) view_choose.findViewById(R.id.textView_drinkNumber);
                int number = Integer.parseInt((String) numberTV.getText());
                Ordered_drinks od = new Ordered_drinks(drink, flavor, number);
                chooseDialog.dismiss();
            }
        });

        view_choose.findViewById(R.id.button_subtract).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView numberText = (TextView) view_choose.findViewById(R.id.textView_drinkNumber);
                int i = Integer.parseInt(String.valueOf(numberText.getText()));
                if (i > 1) {
                    i--;
                }
                numberText.setText(String.valueOf(i));
            }
        });

        view_choose.findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView numberText = (TextView) view_choose.findViewById(R.id.textView_drinkNumber);
                int i = Integer.parseInt(String.valueOf(numberText.getText()));
                if (i < 100) {
                    i++;
                }
                numberText.setText(String.valueOf(i));
            }
        });

        initData();
        right_llM = new LinearLayoutManager(this.getActivity());
        right_listView.setLayoutManager(right_llM);
        Right_adapter rightAdapter = new Right_adapter(inflater, drinks_array);
        right_listView.setAdapter(rightAdapter);

        titles_array.get(0).setSelect(true);
        left_listView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        LeftAdapter leftAdapter = new LeftAdapter(titles_array);
        left_listView.setAdapter(leftAdapter);

        right_listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstItemPosition = right_llM.findFirstVisibleItemPosition();
                leftAdapter.setCurrentPosition(firstItemPosition);
                if (leftAdapter.getCurrentTitle() != "") {
                    right_title.setText(leftAdapter.getCurrentTitle());
                }
            }
        });


        leftAdapter.setOnItemClickListener(new LeftAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int rightPosition) {
                if (right_llM != null) {
                    right_llM.scrollToPositionWithOffset(rightPosition, 0);
                }
            }
        });

        rightAdapter.buttonSetOnClick(new Right_adapter.MyClickListener() {
            @Override
            public void onclick(View v, int position) {
                chooseDialog.show();
                if (view_choose != null) {
                    Drinks drink = drinks_array.get(position);
                    ImageView img = view_choose.findViewById(R.id.choose_drink_img);
                    img.setImageResource(drink.getImageResId() - 1);
                    TextView name = view_choose.findViewById(R.id.choose_drinkName);
                    name.setText(drink.get_name() + "  #" + (drink.get_number() + 1));
                    TextView intro = view_choose.findViewById(R.id.choose_drinkIntro);
                    intro.setText(drink.get_introduction());
                    TextView drink_number = view_choose.findViewById(R.id.textView_drinkNumber);
                    drink_number.setText("1");
                }
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryText) {
                for (int i = 0; i < drinks_array.size(); i++) {
                    if (drinks_array.get(i).get_name().contains(queryText)) {
                        if (right_llM != null) {
                            right_llM.scrollToPositionWithOffset(i, 0);
                            break;
                        }
                    }
                }
                return true;
            }
        });
        return view;
    }

    private void initData() {
        drinks_array.add(new Drinks("牧场酸酪牛油果", "✨ 季节限定",
                23f, "定制牧场奶源酸酪·百分百进口牛油果鲜果·不使用过你，清爽顺滑", R.drawable.avocado_square));
        drinks_array.add(new Drinks("喜悦黄果茶", 19f, "匠心甄选黄色系水果·当季芒果·鲜制橙丁百香果，真果无香精",
                R.drawable.yellow_sq));
        drinks_array.add(new Drinks("东坡荔枝生椰露", 19f, "当季新鲜荔枝果肉·定制生椰乳·每日现制西米，椰椰荔香清甜交融",
                R.drawable.coco_sq));

        drinks_array.add(new Drinks("拿铁咖啡", "☕ 咖啡",
                22f, "鲜牛奶与浓缩咖啡的完美融合，口感丝滑", R.drawable.latte_coffee));
        drinks_array.add(new Drinks("美式咖啡", 18f, "纯粹的浓缩咖啡与水的搭配，口感清爽",
                R.drawable.americano_coffee));

        drinks_array.add(new Drinks("提拉米苏蛋糕", "🍰 甜点",
                25f, "经典的意大利甜点，咖啡与奶酪的美妙结合", R.drawable.tiramisu_cake));
        drinks_array.add(new Drinks("香草冰淇淋", 15f, "浓郁的香草味道，口感细腻",
                R.drawable.vanilla_icecream));
        drinks_array.add(new Drinks("甜甜圈",
                12f, "香甜松软，新鲜出炉", R.drawable.donut));

        drinks_array.add(new Drinks("水牛乳·粉黛玫影", "\uD83C\uDF7C 牛乳茶",
                15f, "无香精[玫影]玫瑰红茶·优选广西水牛乳调制奶底", R.drawable.pinkmilk_square));
        drinks_array.add(new Drinks("水牛乳双拼波波",
                19f, "优选广西牧场水牛乳·水牛乳冻·慢数黑糖波波，口感甜腻不喜慎点", R.drawable.black_sq));
        drinks_array.add(new Drinks("轻波波牛乳茶",
                15f, "人气轻波波牛乳灵感延伸·慢熬黑糖波波，口感香醇，真牛乳无奶精", R.drawable.bobo_sq));

        drinks_array.add(new Drinks("多肉桃李", "\uD83C\uDF52 时令鲜果",
                15f, "当季三华李与当季黄油桃，脆、鲜、甜层层递进", R.drawable.peach_square));
        drinks_array.add(new Drinks("芝芝多肉桃桃", 28f, "优选当季新鲜水蜜桃·新岩岚，岩茶·醇香芝士，不添加香精色素",
                R.drawable.pinkpeach_sq));
        drinks_array.add(new Drinks("芝芝多肉青提", 28f, "优选阳光玫瑰青提·鲜果颗颗去皮·无奶精芝士，甜脆香郁。",
                R.drawable.grape_sq));
        drinks_array.add(new Drinks("芝芝莓莓", 28f,
                "当季新鲜草莓·定制绿妍茶底·无奶精芝士，奶香浓醇，莓香满溢", R.drawable.strawberry_sq));

        drinks_array.add(new Drinks("打包盒", "🥡 打包盒",
                2f, "高品质打包盒，安全卫生", R.drawable.takeout_box));

        for (int i = 0; i < drinks_array.size(); i++) {
            Drinks temp = drinks_array.get(i);
            if (temp.get_type() != null) {
                titles_array.add(new LeftBean(i, temp.get_type()));
            }
        }
    }
}