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
        //Holder��ӻص��ӿ�
        sh.addCallback(this);
        /*SURFACE_TYPE_PUSH_BUFFERS��surface_type_push_buffers������Surface������ԭ�����ݣ�
         * Surface�õ������������������ṩ��
         * ��Cameraͼ��Ԥ���о�ʹ�ø����͵�Surface����Camera �����ṩ��Ԥ��Surface���ݣ�����ͼ��Ԥ
         * ����Ƚ������������������������Ͳ��ܵ���lockCanvas����ȡCanvas�����ˡ�*/
        sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    //SurfaceHolder.Callback�ص�������������surfaceCreated(),surfaceChanged(),surfaceDestroyed()
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
        
        //���������������camera
        Camera.Parameters parameters = camera.getParameters();
        
//        parameters.setPreviewSize(640, 368);��������Ĳ�������������������Ԥ���Ĵ�С
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();

		previewSize[0] = getSize(supportedPreviewSizes, 1000, camera.new Size(1280, 720));
		
		Log.i("hy", "previewSize[0].width:" + previewSize[0].width + ":previewSize[0].height:" + previewSize[0].height);
		
		parameters.setPreviewSize(previewSize[0].width, previewSize[0].height);
		parameters.setPictureSize(previewSize[0].width, previewSize[0].height);

//        parameters.setPreviewSize(1920, 1080);            //��������Ĳ�������������������Ԥ���Ĵ�С
        camera.setParameters(parameters);                //�����������
        try {
            camera.setPreviewDisplay(sh);
        } catch (IOException e) {
            System.out.println(e.getMessage());           //���ִ�ӡlog�ķ�����Log.e(TAG,e.toString());���࿪ͷ�Ĳ�������public static final String TAG
        }
        //��ʼԤ�������������ո������Զ��Խ�
        camera.startPreview();
     	Log.i("hy", "startPreview:");
        
        ispreview = true;

    }
    
    
    
	public static Camera.Size[] previewSize = new Camera.Size[2];
	public static Size getSize(List<Size> list, int th, Size defaultSize) {
		if (null == list || list.isEmpty())
			return defaultSize;
		Collections.sort(list, new Comparator<Size>() {
			public int compare(Size lhs, Size rhs) {// ����������
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
                //����ֹͣԤ��
                camera.stopPreview();
            }
            //Ȼ�����ͷ�camera
            camera.release();
        }
        //����cameraΪ��
        camera = null;

    }
}