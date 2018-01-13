package com.choubao.www.softwareengineeringproject.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.choubao.www.softwareengineeringproject.R;
import com.choubao.www.softwareengineeringproject.vo.HardWare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * Created by choubao on 17/4/22.
 */

public class DataUtils {


    public static ArrayList<HardWare> getData(Context context, String Hardware_Name) {

        ArrayList<HardWare> HardWares = new ArrayList<HardWare>();
        switch (Hardware_Name) {
            case "Cpu": {
                HardWare hardWare = new HardWare();
                hardWare.setHardware_Name("处理器");
                hardWare.setPinpai("Intel");
                hardWare.setXinghao("i7-7700k");
                hardWare.setJiage(2649);
                hardWare.setBeizhu("4.2GHz");
                String str = "/data/data/com.choubao.www.softwareengineeringproject/cache/bmob/" + "a3d7b03baf6330853ee628507ef176c2.png";
                Bitmap bitmap1 = BitmapFactory.decodeFile(str);
                bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.a3d7b03baf6330853ee628507ef176c2);
                hardWare.setPicture(bitmap1);
                //i7处理器
                HardWares.add(hardWare);

                //i5处理器
                HardWare hardWare1 = new HardWare();
                hardWare1.setHardware_Name("处理器");
                hardWare1.setPinpai("Intel");
                hardWare1.setXinghao("i5-7500");
                hardWare1.setJiage(1479);
                hardWare1.setBeizhu("3.4GHz");
                str = "/data/data/com.choubao.www.softwareengineeringproject/cache/bmob/" + "ec0069ac43f06a10f02d20af318c4874.png";
                bitmap1 = BitmapFactory.decodeFile(str);
                bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ec0069ac43f06a10f02d20af318c4874);
                hardWare1.setPicture(bitmap1);
                HardWares.add(hardWare1);

                return HardWares;
            }
            case "HardDisk":

                break;
            case "SSD":

                break;
            case "Monitor":

                break;
            case "MemoryBar":

                break;
            case "Case":

                break;
            case "MotherBoard":

                break;
            case "Keyboard":

                break;
            case "Mouse":

                break;
            case "CDdrive":

                break;
            case "GraphicsCard":

                break;
        }
        return null;
    }

    //按key查询
    public static ArrayList<HardWare> searchByKey(Context context, ArrayList<HardWare> HardWares, String key) {
        ArrayList<HardWare> HardWare_Search_Results = new ArrayList<>();
        key = ".*" + key + ".*";
        for (HardWare hw : HardWares) {
            boolean isMatch = Pattern.matches(key, hw.get_For_Search_String());
            if (isMatch) {
                HardWare_Search_Results.add(hw);
            }
        }
        return HardWare_Search_Results;
    }

    //排序函数
    public static ArrayList<HardWare> getSort(Context context, ArrayList<HardWare> HardWares, int mode) {
        switch (mode) {
            case 1:
                Collections.sort(HardWares, new SortLow_to_High());
                break;
            case 2:
                Collections.sort(HardWares, new SortHigh_to_Low());
                break;
        }
        return HardWares;
    }

    //由低到高
    public static class SortLow_to_High implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            HardWare h1 = (HardWare) o1;
            HardWare h2 = (HardWare) o2;
            if (h1.getJiage() > h2.getJiage()) {
                return 1;
            } else return -1;
        }
    }

    //由高到低
    public static class SortHigh_to_Low implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            HardWare h1 = (HardWare) o1;
            HardWare h2 = (HardWare) o2;
            if (h1.getJiage() > h2.getJiage()) {
                return -1;
            } else return 1;
        }
    }

    //*******************下面代码也没用
 /*   //从网络服务器获取数据的
    private static final int CPU = 0x1;           //处理器
    private static final int HARD_DISK = 0x2;     //机械硬盘
    private static final int SSD = 0x3;           //固态硬盘
    private static final int MONITOR = 0x4;       //显示器
    private static final int MEMORY_BAR = 0x5;    //内存条
    private static final int CASE = 0x6;          //机箱
    private static final int MOTHER_BOARD = 0x7;  //主板
    private static final int KEYBOARD = 0x8;      //键盘
    private static final int MOUSE = 0x9;         //鼠标
    private static final int CD_DRIVE = 0x10;      //光驱
    private static final int GRAPHICS_CARD = 0x11; //显卡
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CPU:

                    break;
                case HARD_DISK:

                    break;
                case SSD:

                    break;
                case MONITOR:

                    break;
                case MEMORY_BAR:

                    break;
                case CASE:

                    break;
                case MOTHER_BOARD:

                    break;
                case KEYBOARD:

                    break;
                case MOUSE:

                    break;
                case CD_DRIVE:

                    break;
                case GRAPHICS_CARD:

                    break;
            }
        }
    };*/


    //下面的代码没用，增加经验用的
    /*
    public static ArrayList<CPU> getCPUs(Context context){
        ArrayList<CPU> CPUs = new ArrayList<CPU>();
        CPU cpu=new CPU();
        cpu.setPinpai("Intel");
        cpu.setXinghao("i7-7700k");
        cpu.setJiage(2649);
        cpu.setXiangxixinxi("4.2GHz");

        //String str=getApplicationContext().getCacheDir()+"/bmob/"+"intel_core_i7_7700k.png";
        //String str=getApplicationContext().getCacheDir()+"/bmob";
    //    /data/data/com.choubao.www.bmobtest/cache/bmob/米（MI）小米便携鼠标.jpg
        //String str="/data/data/com.choubao.www.softwareengineeringproject/cache/bmob/"+"intel_core_i7_7700k.png";
        //上面四行代码没用

        //下面的代码创建文件夹可以成功
//        File f=new File("/data/data/com.choubao.www.softwareengineeringproject/cache/bmob");
//        if (!f.exists()) {
//            f.mkdirs();
//        }

         //下面的代码将R文件中的图片存到文件夹可以成功
//        String str="/data/data/com.choubao.www.softwareengineeringproject/cache/bmob/"+"intel_core_i7_7700k.png";
//          Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.intel_core_i7_7700k);
//        File file=new File(str);
//        try {
//            file.createNewFile();
//            FileOutputStream out=new FileOutputStream(str);
//            bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
//            out.flush();
//            out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        String str="/data/data/com.choubao.www.softwareengineeringproject/cache/bmob/"+"intel_core_i7_7700k.png";

        //下面的代码可以显示文件夹里的图片
        Bitmap bitmap1= BitmapFactory.decodeFile(str);
//        Toast t=new Toast(context);
//        TextView textView=new TextView(context);
//        textView.setText("臭包");
//        ImageView imageView=new ImageView(context);
//        imageView.setImageBitmap(bitmap1);
//        LinearLayout linearLayout=new LinearLayout(context);
//        linearLayout.setOrientation(LinearLayout.VERTICAL);
//        linearLayout.setGravity(Gravity.CENTER);
//        linearLayout.addView(imageView);
//        linearLayout.addView(textView);
//        t.setView(linearLayout);
//        t.setGravity(Gravity.CENTER,0,0);
//        t.setDuration(Toast.LENGTH_LONG);
//        t.show();
        cpu.setPicture(bitmap1);

        //System.out.println(cpu.toString());
        //i7处理器
        CPUs.add(cpu);
        for (CPU c : CPUs) {
            System.out.println(c.toString());
        }

        //i5处理器
        CPU cpu1=new CPU();
        cpu1.setPinpai("Intel");
        cpu1.setXinghao("i5-7500");
        cpu1.setJiage(1479);
        cpu1.setXiangxixinxi("3.4GHz");
        str="/data/data/com.choubao.www.softwareengineeringproject/cache/bmob/"+"intel_core_i5_7500.png";
        bitmap1= BitmapFactory.decodeFile(str);
        cpu1.setPicture(bitmap1);
        CPUs.add(cpu1);

        for (CPU c : CPUs) {
            System.out.println(c.toString());
        }
        return CPUs;
    }*/
}
