package com.choubao.www.softwareengineeringproject.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.choubao.www.softwareengineeringproject.AssembleAComputerApplication;
import com.choubao.www.softwareengineeringproject.MainActivity;
import com.choubao.www.softwareengineeringproject.R;
import com.choubao.www.softwareengineeringproject.adapter.ShoppingCarListAdapter;
import com.choubao.www.softwareengineeringproject.touchripplewidget.RippleTextView;
import com.choubao.www.softwareengineeringproject.utils.Constant;
import com.choubao.www.softwareengineeringproject.utils.ListItemClickHelp;
import com.choubao.www.softwareengineeringproject.vo.HardWare;
import com.choubao.www.softwareengineeringproject.vo.Order;
import com.choubao.www.softwareengineeringproject.vo.User;

import java.util.ArrayList;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.choubao.www.softwareengineeringproject.AssembleAComputerApplication.sp;

/**
 * Created by choubao on 17/5/12.
 */

public class CustomPopupWindowPayMoney extends PopupWindow implements ListItemClickHelp, View.OnClickListener {

    private TextView textView_name, textView_phonenumber, textView_address;
    private ListView listView_paymoney;
    private TextView textView_heji;
    private RippleTextView pay_money_tijiaodingdan;
    private ImageView dismissSelfClick;
    private LinearLayout xiugaigerenxinxi;

    private View mPopView;
    private ShoppingCarListAdapter shoppingCarListAdapter;
    private MainActivity mainActivity;
    private Context context;

    private AssembleAComputerApplication app;

    private boolean UserInformationIsComplete = false;

    public CustomPopupWindowPayMoney(Context context) {
        super(context);
        this.context = context;
        mainActivity = (MainActivity) context;
        init(context);
        setPopupWindow();

        shoppingCarListAdapter = new ShoppingCarListAdapter(mainActivity, mainActivity.shoppingCars, this);
        listView_paymoney.setAdapter(shoppingCarListAdapter);
    }

    //初始化布局
    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //绑定布局
        mPopView = inflater.inflate(R.layout.pay_money, null);
        textView_name = (TextView) mPopView.findViewById(R.id.textView_name);
        textView_phonenumber = (TextView) mPopView.findViewById(R.id.textView_phonenumber);
        textView_address = (TextView) mPopView.findViewById(R.id.textView_address);
        listView_paymoney = (ListView) mPopView.findViewById(R.id.listView_paymoney);
        textView_heji = (TextView) mPopView.findViewById(R.id.textView_heji);
        pay_money_tijiaodingdan = (RippleTextView) mPopView.findViewById(R.id.pay_money_tijiaodingdan);
        dismissSelfClick = (ImageView) mPopView.findViewById(R.id.dismissSelfClick);
        xiugaigerenxinxi = (LinearLayout) mPopView.findViewById(R.id.xiugaigerenxinxi);

        app = (AssembleAComputerApplication) mainActivity.getApplication();
        textView_name.setText(sp.getString(Constant.NAME, "未填写"));
        textView_phonenumber.setText("联系电话：" + sp.getString(Constant.PHONENUMBER, "未填写"));
        textView_address.setText("收货地址：" + sp.getString(Constant.ADDRESS, "未填写"));

        UserInformationIsComplete = sp.getBoolean(Constant.USERINFORMATIONISCOMPLETE, false);

        textView_heji.setText(heji() + "");

        pay_money_tijiaodingdan.setOnClickListener(this);
        dismissSelfClick.setOnClickListener(this);
        xiugaigerenxinxi.setOnClickListener(this);
    }

    private String order_content = "";

    private double heji() {
        double sum = 0;
        for (HardWare h : mainActivity.shoppingCars) {
            sum += h.getJiage();
            order_content += h.get_id() + " ";
        }
        return sum;
    }

    private void setPopupWindow() {
        this.setContentView(mPopView);//设置View
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);//设置宽
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //获取当前窗口焦点
        this.setFocusable(true);
        //设置动画
        this.setAnimationStyle(R.style.mypopwindow_anim_style);
        //设置背景
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    //实现ListItemClickHelp接口的onClick     点击添加按钮后将物品从购物车中删除
    @Override
    public void onClick(View item, View parent, int position, int which) {

        switch (which) {
            case R.id.imageView_shoppingcar_remove:
                HardWare hardWare = mainActivity.shoppingCars.get(position);
                mainActivity.shoppingCars.remove(hardWare);

                //刷新列表
                shoppingCarListAdapter = new ShoppingCarListAdapter(mainActivity, mainActivity.shoppingCars, this);
                listView_paymoney.setAdapter(shoppingCarListAdapter);
                mainActivity.setSumMoneyText();
                textView_heji.setText(heji() + "");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_money_tijiaodingdan:
                checkThePayment();
                break;
            case R.id.dismissSelfClick:
                dismiss();
                break;
            case R.id.xiugaigerenxinxi:
                int login_state = app.sp.getInt(Constant.LOGIN_STATE, 3);
                if (login_state == 2)//是账号密码登录的人才弹出编辑框
                {
                    userInformationDialog();
                }else
                {
                    Toast.makeText(mainActivity, "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void checkThePayment() {
        if (app.sp.getInt(Constant.LOGIN_STATE, 3)!=2) {
            Toast.makeText(context, "请先登录", Toast.LENGTH_LONG).show();
            return;
        }
        if (UserInformationIsComplete == false) {
            Toast.makeText(context, "请完善个人信息", Toast.LENGTH_LONG).show();
            return;
        }


        final String[] HardWareNames = {"处理器", "机械硬盘", "固态硬盘", "显示器", "内存", "机箱", "主板", "键盘", "鼠标", "光驱", "显卡"};
        int[] count = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        ArrayList<String> notCompatible = new ArrayList<>();//把不兼容的东西都存进来
        final ArrayList<String> moreThanOne = new ArrayList<>();//多选的硬件
        ArrayList<String> isZero = new ArrayList<>();//没选的硬件
        for (HardWare h : mainActivity.shoppingCars) {
            switch (h.getHardware_Name()) {
                case "处理器":
                    count[0]++;
                    break;
                case "机械硬盘":
                    count[1]++;
                    break;
                case "固态硬盘":
                    count[2]++;
                    break;
                case "显示器":
                    count[3]++;
                    break;
                case "内存":
                    count[4]++;
                    break;
                case "机箱":
                    count[5]++;
                    break;
                case "主板":
                    count[6]++;
                    break;
                case "键盘":
                    count[7]++;
                    break;
                case "鼠标":
                    count[8]++;
                    break;
                case "光驱":
                    count[9]++;
                    break;
                case "显卡":
                    count[10]++;
                    break;
            }

            //判断是否不兼容的循环
            String key = ".*" + h.getXinghao() + ".*";
            for (HardWare h2 : mainActivity.shoppingCars) {
                boolean isMatch = Pattern.matches(key, h2.getBeizhu());
                if (isMatch) {
                    notCompatible.add(h.getXinghao());
                    notCompatible.add(h2.getXinghao());
                }
            }
        }

        for (int i = 0; i < 11; i++) {
            switch (count[i]) {
                case 1://数量为1刚刚好
                    //什么都不干
                    break;
                case 0://数量为0
                    isZero.add(HardWareNames[i]);
                    break;
                default://数量不止一个
                    moreThanOne.add(HardWareNames[i]);
                    break;
            }
        }

        String hint = new String();
        if (moreThanOne.size() != 0) {
            hint += "多选购的硬件:";
            for (String s : moreThanOne) {
                hint += s + " ";
                System.out.println(s);
            }
        }
        if (isZero.size() != 0) {
            hint += "\n未选购的硬件:";
            for (String s : isZero) {
                hint += s + " ";
                System.out.println(s);
            }
        }
        if (notCompatible.size() != 0) {
            hint += "\n下列硬件存在不兼容情形:";
            for (String s : notCompatible) {
                hint += s + " ";
                System.out.println(s);
            }
        }
        hint += "\n确认提交订单？";
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("提示");
        builder.setMessage(hint);
        //正面的按钮
        builder.setPositiveButton("确认提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Order order = new Order();
                order.setUsername(sp.getString(Constant.USERNAME, "null"));
                int r1 = (int) (Math.random() * 10);//第一个随机数
                int r2 = (int) (Math.random() * 10);//第二个随机数
                long now = System.currentTimeMillis();//一个13位的时间戳
                order.setOrder_id(now + r1 + r2);
                order.setOrder_amount(heji());
                order.setOrder_content(order_content);
                order.setName(sp.getString(Constant.NAME, "null"));
                order.setPhonenumber(sp.getString(Constant.PHONENUMBER, "null"));
                order.setAddress(sp.getString(Constant.ADDRESS, "null"));
                order.setPay(false);//默认未支付
//                Toast.makeText(mainActivity,order.toString(),Toast.LENGTH_LONG).show();
                if (order.getOrder_content().length() != 0) { //订单内容不能为空
                    order.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(mainActivity, "订单提交成功", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(mainActivity, "订单提交失败", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(mainActivity, "订单不允许为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //负面的按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(context,"取消",Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    //个人信息函数 会弹出一个Dialog
    private void userInformationDialog() {

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(mainActivity);
        builder1.setTitle("编辑个人信息");
        final View view1 = mainActivity.getLayoutInflater().inflate(R.layout.user_information, null);
        builder1.setView(view1);

        //绑定控件
        final EditText editText_name = (EditText) view1.findViewById(R.id.editText_name);
        final EditText editText_phonenumber = (EditText) view1.findViewById(R.id.editText_phonenumber);
        final EditText editText_address = (EditText) view1.findViewById(R.id.editText_address);
        //先获取数据
        String name = sp.getString(Constant.NAME, "");
        String phonenumber = sp.getString(Constant.PHONENUMBER, "");
        String address = sp.getString(Constant.ADDRESS, "");

        if (name.length() > 0) {
            editText_name.setText(name);
        }
        if (phonenumber.length() > 0) {
            editText_phonenumber.setText(phonenumber);
        }
        if (address.length() > 0) {
            editText_address.setText(address);
        }

        //负面的按钮
        builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        //正面的按钮
        builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editText_name.getText().toString();
                String phonenumber = editText_phonenumber.getText().toString();
                String address = editText_address.getText().toString();
                SharedPreferences.Editor editor3 = sp.edit();
                editor3.putString(Constant.NAME, name);
                editor3.putString(Constant.PHONENUMBER, phonenumber);
                editor3.putString(Constant.ADDRESS, address);
                //Toast.makeText(mainActivity, " "+name.length()+" "+phonenumber.length()+" "+address.length(), Toast.LENGTH_SHORT).show();
                if (name.length() > 0 && phonenumber.length() > 0 && address.length() > 0) {
                    editor3.putBoolean(Constant.USERINFORMATIONISCOMPLETE, true);
                } else {
                    editor3.putBoolean(Constant.USERINFORMATIONISCOMPLETE, false);
                }
                editor3.commit();
                User newUser = new User();
                newUser.setName(name);
                newUser.setPhonenumber(phonenumber);
                newUser.setAddress(address);
                BmobUser bmobUser = BmobUser.getCurrentUser();
                newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(mainActivity, "更新信息成功", Toast.LENGTH_SHORT).show();
                            textView_name.setText(sp.getString(Constant.NAME, "未填写"));
                            textView_phonenumber.setText("联系电话：" + sp.getString(Constant.PHONENUMBER, "未填写"));
                            textView_address.setText("收货地址：" + sp.getString(Constant.ADDRESS, "未填写"));
                            UserInformationIsComplete = sp.getBoolean(Constant.USERINFORMATIONISCOMPLETE, false);
                        } else {
                            Toast.makeText(mainActivity, "更新数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        builder1.show();
    }
}
