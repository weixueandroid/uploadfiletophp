package com.zhangyp.higo.uploadfiletophp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baoyz.actionsheet.ActionSheet;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.otto.Subscribe;
import com.zhangyp.higo.uploadfiletophp.adapter.ChoosePhotoListAdapter;
import com.zhangyp.higo.uploadfiletophp.bean.Message;
import com.zhangyp.higo.uploadfiletophp.core.BusProvider;
import com.zhangyp.higo.uploadfiletophp.core.QuestNetDataHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.widget.HorizontalListView;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private final int REQUEST_CODE_CROP = 1002;
    private final int REQUEST_CODE_EDIT = 1003;
    @Bind(R.id.bt_select)
    Button btSelect;
    @Bind(R.id.lv_photo)
    HorizontalListView lvPhoto;
    @Bind(R.id.bt_update)
    Button btUpdate;
    @Bind(R.id.lv_photo_show)
    HorizontalListView lvPhotoShow;


    private List<PhotoInfo> mPhotoList;
    private ChoosePhotoListAdapter mChoosePhotoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        BusProvider.register(this);

        mPhotoList = new ArrayList<>();
        mChoosePhotoListAdapter = new ChoosePhotoListAdapter(this, mPhotoList);
        lvPhoto.setAdapter(mChoosePhotoListAdapter);

        initImageLoader(this);

        btSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertSelect();
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

    }

    private void upload() {
        if (mPhotoList != null && mPhotoList.size() > 0) {
            List<String> list = new ArrayList<>();
//            new QuestNetDataHelper().uploadFile(photoPath);
            for (PhotoInfo photoInfo : mPhotoList) {
                list.add(photoInfo.getPhotoPath());
                Log.i("AAA", "all ==>" + photoInfo.getPhotoPath());
            }
            new QuestNetDataHelper().uploadFiles(list);
        }
    }

    private ChoosePhotoListAdapter choosePhotoListAdapter;

    @Subscribe
    public void uploadFileResponse(String response) {
        Message msg = JSON.parseObject(response, Message.class);
        List<String> imgUrls = msg.getImgUrls();
        if (msg.getCode() == 1) {
            if (choosePhotoListAdapter == null) {
                choosePhotoListAdapter = new ChoosePhotoListAdapter(this, null, imgUrls);
                lvPhotoShow.setAdapter(choosePhotoListAdapter);
            }
            choosePhotoListAdapter.notifyDataSetChanged();
        }
    }


    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                mPhotoList.addAll(resultList);
                mChoosePhotoListAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    private boolean mutiSelect = true;
    private FunctionConfig functionConfig;

    private void alertSelect() {
        functionConfig = new FunctionConfig.Builder()
                .setMutiSelectMaxSize(9)
//                .setMutiSelect(boolean)//配置是否多选
//                .setEnableEdit(boolean)//开启编辑功能
//                .setEnableCrop(boolean)//开启裁剪功能
//                .setEnableRotate(boolean)//开启旋转功能
//                .setEnableCamera(boolean)//开启相机功能
//                .setCropWidth(int width)//裁剪宽度
//                .setCropHeight(int height)//裁剪高度
//                .setCropSquare(boolean)//裁剪正方形
//                .setSelected(List)//添加已选列表,只是在列表中默认呗选中不会过滤图片
//                .setFilter(List list)//添加图片过滤，也就是不在GalleryFinal中显示
//                .takePhotoFolter(File file)//配置拍照保存目录，不做配置的话默认是/sdcard/DCIM/GalleryFinal/
//                .setRotateReplaceSource(boolean)//配置选择图片时是否替换原始图片，默认不替换
//                .setCropReplaceSource(boolean)//配置裁剪图片时是否替换原始图片，默认不替换
//                .setForceCrop(boolean)//启动强制裁剪功能,一进入编辑页面就开启图片裁剪，不需要用户手动点击裁剪，此功能只针对单选操作
//                .setForceCropEdit(boolean)//在开启强制裁剪功能时是否可以对图片进行编辑（也就是是否显示旋转图标和拍照图标）
//                .setEnablePreview(boolean)//是否开启预览功能
                .build();
        ActionSheet.createBuilder(MainActivity.this, getSupportFragmentManager())
                .setCancelButtonTitle("取消(Cancel)")
                .setOtherButtonTitles("打开相册(Open Gallery)", "拍照(Camera)", "裁剪(Crop)", "编辑(Edit)")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        String path = "/sdcard/pk1-2.jpg";
                        switch (index) {
                            case 0:
                                if (mutiSelect) {
                                    GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
                                } else {
                                    GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
                                }
                                break;
                            case 1:
                                GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);
                                break;
                            case 2:
                                if (new File(path).exists()) {
                                    GalleryFinal.openCrop(REQUEST_CODE_CROP, functionConfig, path, mOnHanlderResultCallback);
                                } else {
                                    Toast.makeText(MainActivity.this, "图片不存在", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 3:
                                if (new File(path).exists()) {
                                    GalleryFinal.openEdit(REQUEST_CODE_EDIT, functionConfig, path, mOnHanlderResultCallback);
                                } else {
                                    Toast.makeText(MainActivity.this, "图片不存在", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();

    }

    private void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        BusProvider.unregister(this);
        super.onDestroy();

    }
}
