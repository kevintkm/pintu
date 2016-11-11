package com.imooc.xpuzzle.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imooc.xpuzzle.R;
import com.imooc.xpuzzle.adapter.GridPicListAdapter;
import com.imooc.xpuzzle.adapter.WheelSkuAdapter;
import com.imooc.xpuzzle.util.ImageUrl;
import com.imooc.xpuzzle.wheelview.WheelView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.imooc.xpuzzle.activity.PuzzleMain.MTYPE;
import static com.imooc.xpuzzle.activity.PuzzleMain.PICPATH;
import static com.imooc.xpuzzle.activity.PuzzleMain.RESOURCEID;

/**
 * 程序主界面：显示默认图片列表、自选图片按钮
 *
 * @author xys
 */
public class MainActivity extends Activity implements OnClickListener {

    // 返回码：系统图库
    private static final int RESULT_IMAGE = 100;
    // 返回码：相机
    private static final int RESULT_CAMERA = 200;
    // IMAGE TYPE
    private static final String IMAGE_TYPE = "image/*";
    // Temp照片路径
    public static String TEMP_IMAGE_PATH;
    // GridView 显示图片
    private GridView mGvPicList;
    private List<String> mPicList;
    // 主页图片资源ID
    private String[] mResPicId;
    // 显示Type
    private LinearLayout mTvPuzzleMainTypeSelected;


    private TextView mTypeText;

    private TextView tvCancel;
    private TextView tvOk;
    private WheelView wheelView;
    private LinearLayout linearLayout;
    private View mask;
    private boolean isSku;
    private List<String> types;

    // 游戏类型N*N
    private int mType = 2;
    // 本地图册、相机选择
    private String[] mCustomItems = new String[]{"本地图册", "相机拍照"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xpuzzle_main);

        TEMP_IMAGE_PATH =
                Environment.getExternalStorageDirectory().getPath() +
                        "/temp.png";
        mPicList = new ArrayList<String>();
        // 初始化Views
        initViews();
        // 数据适配器
        mGvPicList.setAdapter(new GridPicListAdapter(
                MainActivity.this, mPicList));
        // Item点击监听
        mGvPicList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long arg3) {
                if (position == mResPicId.length - 1) {
                    // 选择本地图库 相机
                    showDialogCustom();
                } else {
                    // 选择默认图片
                    Intent intent = new Intent(
                            MainActivity.this,
                            PuzzleMain.class);
                    intent.putExtra(PICPATH, mResPicId[position]);
                    intent.putExtra(MTYPE, mType);
                    startActivity(intent);
                }
            }
        });

        /**
         * 显示难度Type
         */
        mTvPuzzleMainTypeSelected.setOnClickListener(this);
    }

    // 显示选择系统图库 相机对话框
    private void showDialogCustom() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setTitle("选择图像来源：");
        builder.setItems(mCustomItems,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (0 == which) {
                            // 本地图册
                            Intent intent = new Intent(
                                    Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType(IMAGE_TYPE);
                            startActivityForResult(intent, RESULT_IMAGE);
                        } else if (1 == which) {
                            // 系统相机
                            Intent intent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            Uri photoUri = Uri.fromFile(
                                    new File(TEMP_IMAGE_PATH));
                            intent.putExtra(
                                    MediaStore.EXTRA_OUTPUT,
                                    photoUri);
                            startActivityForResult(intent, RESULT_CAMERA);
                        }
                    }
                });
        builder.create().show();
    }

    /**
     * 调用图库相机回调方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_IMAGE && data != null) {
                // 相册
                Cursor cursor = this.getContentResolver().query(
                        data.getData(), null, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(
                        cursor.getColumnIndex("_data"));
                Intent intent = new Intent(
                        MainActivity.this,
                        PuzzleMain.class);
                intent.putExtra(PICPATH, imagePath);
                intent.putExtra(MTYPE, mType);
                cursor.close();
                startActivity(intent);
            } else if (requestCode == RESULT_CAMERA) {
                // 相机
                Intent intent = new Intent(
                        MainActivity.this,
                        PuzzleMain.class);
                intent.putExtra(PICPATH, TEMP_IMAGE_PATH);
                intent.putExtra(MTYPE, mType);
                startActivity(intent);
            }
        }
    }


    /**
     * 初始化Views
     */
    private void initViews() {
        mGvPicList = (GridView) findViewById(
                R.id.gv_xpuzzle_main_pic_list);
        // 初始化Bitmap数据
        mResPicId = new String[]{
                ImageUrl.url, ImageUrl.url1, ImageUrl.url2,
                ImageUrl.url3, ImageUrl.url4, ImageUrl.url5,
                ImageUrl.url6, ImageUrl.url7, ImageUrl.url8,
                ImageUrl.url9, ImageUrl.url10, ImageUrl.url11,
                ImageUrl.url12, ImageUrl.url13,
                ImageUrl.url14 ,ImageUrl.url15};
        for (int i = 0; i < mResPicId.length; i++) {
            mPicList.add(mResPicId[i]);
        }
        // 显示type
        mTvPuzzleMainTypeSelected = (LinearLayout) findViewById(
                R.id.ll_puzzle_main_spinner);
        mTypeText = (TextView) findViewById(R.id.tv_puzzle_main_type_selected);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvOk = (TextView) findViewById(R.id.tv_ok);
        linearLayout = (LinearLayout) findViewById(R.id.layout_wheel);
        wheelView = (WheelView) findViewById(R.id.wheel_sku);
        mask = findViewById(R.id.layout_mask);

        tvCancel.setOnClickListener(this);
        tvOk.setOnClickListener(this);
        types = new ArrayList<>();
        types.add("2 X 2");
        types.add("3 X 3");
        types.add("4 X 4");
        wheelView.setAdapter(new WheelSkuAdapter(types));
        wheelView.setCurrentItem(0);
        showPopupWindow(false);

    }

    private void showPopupWindow(boolean show) {
        if (show) {
            linearLayout.setVisibility(View.VISIBLE);
            mask.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
            mask.setVisibility(View.GONE);
        }
    }

    /**
     * popup window item点击事件
     */
    @Override
    public void onClick(View v) {
        isSku = !isSku;
        showPopupWindow(isSku);
        switch (v.getId()) {
            case R.id.tv_cancel:
                break;
            case R.id.tv_ok:
                mType = wheelView.getCurrentItem() + 2;
                mTypeText.setText(types.get(mType-2));
                break;
            case R.id.ll_puzzle_main_spinner:
                break;
        }
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
