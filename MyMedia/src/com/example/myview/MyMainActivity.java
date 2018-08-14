package com.example.myview;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;



public class MyMainActivity extends Activity implements SurfaceHolder.Callback {

    private Camera camera;
    private Camera cameratwo;
    private Camera camerathree;
    private SurfaceHolder sh;
    private boolean ispreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview);
        sh = surfaceView.getHolder();
        //Holder添加回调接口
        sh.addCallback(this);
        /*SURFACE_TYPE_PUSH_BUFFERS：surface_type_push_buffers表明该Surface不包含原生数据，
         * Surface用到的数据由其他对象提供，
         * 在Camera图像预览中就使用该类型的Surface，由Camera 负责提供给预览Surface数据，这样图像预
         * 览会比较流畅。如果设置这种类型则就不能调用lockCanvas来获取Canvas对象了。*/
        sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    //SurfaceHolder.Callback回调的三个方法，surfaceCreated(),surfaceChanged(),surfaceDestroyed()
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	
//    	int numberOfCameras = Camera.getNumberOfCameras();
        try {
        	camera = Camera.open(1); 
        	Log.i("hy", "camera:" +camera +":cameratwo:"+cameratwo);
        	cameratwo = Camera.open(1);  
        	Log.i("hy",":cameratwo:"+cameratwo);
        	camerathree = Camera.open(2);  
        	Log.i("hy", "camerathree:" +camerathree);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		Log.i("hy", "camera:" +camera +":cameratwo:"+cameratwo+"camerathree:" +camerathree);
        
        //打开相机，赋给对象camera
        Camera.Parameters parameters = camera.getParameters();
        
//        parameters.setPreviewSize(640, 368);设置相机的参数，这里是设置它的预览的大小
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();

		previewSize[0] = getSize(supportedPreviewSizes, 1000, camera.new Size(1280, 720));
		
		Log.i("hy", "previewSize[0].width:" + previewSize[0].width + ":previewSize[0].height:" + previewSize[0].height);
		
		parameters.setPreviewSize(previewSize[0].width, previewSize[0].height);
		parameters.setPictureSize(previewSize[0].width, previewSize[0].height);

//        parameters.setPreviewSize(1920, 1080);            //设置相机的参数，这里是设置它的预览的大小
        camera.setParameters(parameters);                //设置相机参数
        try {
            camera.setPreviewDisplay(sh);
        } catch (IOException e) {
            System.out.println(e.getMessage());           //两种打印log的方法，Log.e(TAG,e.toString());在类开头的部分设置public static final String TAG
        }
        //开始预览，但不是拍照更不是自动对焦
        camera.startPreview();
     	Log.i("hy", "startPreview:");
        
        ispreview = true;

    }
    
    
    
	public static Camera.Size[] previewSize = new Camera.Size[2];
	public static Size getSize(List<Size> list, int th, Size defaultSize) {
		if (null == list || list.isEmpty())
			return defaultSize;
		Collections.sort(list, new Comparator<Size>() {
			public int compare(Size lhs, Size rhs) {// 作升序排序
				if (lhs.width == rhs.width) {
					return 0;
				} else if (lhs.width > rhs.width) {
					return 1;
				} else {
					return -1;
				}
			}
		});

		for (Size s : list) {
			Log.i("hy", "s.width" + s.width + "s.height" + s.height);
		}

		return list.get(list.size() - 1);
	}

    
    

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            if (ispreview) {
                //首先停止预览
                camera.stopPreview();
            }
            //然后再释放camera
            camera.release();
        }
        //设置camera为空
        camera = null;

    }
}