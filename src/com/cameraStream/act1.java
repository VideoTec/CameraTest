package com.cameraStream;

import java.io.IOException;
import java.lang.Math;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;


//----------------------------------------------------------------------

public class act1 extends Activity {
	
	boolean calculate_Igray;
	boolean calculate_Ipack;
	boolean calculate_Iset;
	boolean calculate_Idraw;
	boolean calculate_Iprimitives;
	boolean calculate_Iresponce;
	
	ToggleButton bt_Igray;
	ToggleButton bt_Ipack;
	ToggleButton bt_Iset;
	ToggleButton bt_Idraw;
	ToggleButton bt_Iprimitives;
	ToggleButton bt_Iresponce;
	
	public static Bitmap bm; 
	public final static int IMAGE_W = 320;
	public final static int IMAGE_H = 240;
	
	private ImageView mImageView;
	private Preview mPreview;
	private FrameLayout mContainer;
	static int[] im_gray = new int[IMAGE_W * IMAGE_H];
	static int[] pixels = new int[IMAGE_W * IMAGE_H];
	
	static int cur_priority;
	TextView scr_message;
	Canvas mCanvas;	

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		

		cur_priority = android.os.Process.getThreadPriority(android.os.Process.myTid()); // 0
		//android.os.Process.setThreadPriority(-20);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		
		mContainer = (FrameLayout) findViewById(R.id.FrameLayout01);
		mImageView = (ImageView)findViewById(R.id.ImageView01);
		mPreview = new Preview(this);
		scr_message = (TextView) findViewById(R.id.status_str);	
		
		scr_message.setTextSize(30);
		scr_message.setTextColor(Color.RED);

		// add camera preview
		mContainer.addView(mPreview, IMAGE_W, IMAGE_H); // surfaceChanged h, w depends on that!
		
		// empty bitmap
		bm = Bitmap.createBitmap(IMAGE_W, IMAGE_H, Bitmap.Config.ARGB_8888 );
			
		// canvas will draw into the bitmap assigned to mImageView
		
		mImageView.setImageBitmap(bm);
		mCanvas = new Canvas();
		mCanvas.setBitmap(bm);
		
		// buttons
		bt_Igray = (ToggleButton) findViewById(R.id.btn_Igray);
		bt_Ipack = (ToggleButton) findViewById(R.id.btn_Ipack);
		bt_Iset = (ToggleButton) findViewById(R.id.btn_Iset);
		bt_Idraw = (ToggleButton) findViewById(R.id.btn_Idraw);
		bt_Iprimitives = (ToggleButton) findViewById(R.id.btn_Iprimitives);
		bt_Iresponce = (ToggleButton) findViewById(R.id.btn_Responce);
		
		calculate_Igray = false;
		bt_Igray.setChecked(calculate_Igray);
		bt_Igray.setText("Gray OFF");
		bt_Igray.setTextOff("Gray OFF");
		bt_Igray.setTextOn("Gray ON");
		bt_Igray.setOnClickListener(mIgray_Listener);
		
		calculate_Ipack = false;
		bt_Ipack.setChecked(calculate_Ipack);
		bt_Ipack.setText("Packing OFF");
		bt_Ipack.setTextOff("Packing OFF");
		bt_Ipack.setTextOn("Packing ON");
		bt_Ipack.setOnClickListener(mIpack_Listener);	
		
		calculate_Iset = false;
		bt_Iset.setChecked(calculate_Iset);
		bt_Iset.setText("SetBitmap OFF");
		bt_Iset.setTextOff("SetBitmap OFF");
		bt_Iset.setTextOn("SetBitmap ON");
		bt_Iset.setOnClickListener(mIset_Listener);
		
		calculate_Idraw = false;
		bt_Idraw.setChecked(calculate_Idraw);
		bt_Idraw.setText("Drawing OFF");
		bt_Idraw.setTextOff("Drawing OFF");
		bt_Idraw.setTextOn("Drawing ON");
		bt_Idraw.setOnClickListener(mIdraw_Listener);	
		
		calculate_Iprimitives = false;
		bt_Iprimitives.setChecked(calculate_Iprimitives);
		bt_Iprimitives.setText("Primitives OFF");
		bt_Iprimitives.setTextOff("Primitives OFF");
		bt_Iprimitives.setTextOn("Primitives ON");
		bt_Iprimitives.setOnClickListener(mIprimitives_Listener);
		
		calculate_Iresponce = false;
		bt_Iresponce.setChecked(calculate_Iresponce);
		bt_Iresponce.setText("Responsive OFF");
		bt_Iresponce.setTextOff("Responsive OFF");
		bt_Iresponce.setTextOn("Responsive ON");
		bt_Iresponce.setOnClickListener(mIresponce_Listener);
		
	}

	OnClickListener mIgray_Listener = new OnClickListener() {
		public void onClick(View v) {
			calculate_Igray = !calculate_Igray;
			
			if (calculate_Igray)
			{
				;
			}			
			else
			{
				calculate_Ipack = false;	
				calculate_Iset = false;
				calculate_Idraw = false;
				calculate_Iprimitives = false;
				bt_Iresponce.setVisibility(View.INVISIBLE);
			}
			
			bt_Igray.setChecked(calculate_Igray);
			bt_Ipack.setChecked(calculate_Ipack);
			bt_Iset.setChecked(calculate_Iset);
			bt_Idraw.setChecked(calculate_Idraw);
			bt_Idraw.setChecked(calculate_Iprimitives);
		}		
	};
	
	OnClickListener mIpack_Listener = new OnClickListener() {
		public void onClick(View v) {
			calculate_Ipack = !calculate_Ipack;
			if (calculate_Ipack)
			{
				calculate_Igray = true;
			}			
			else
			{
				calculate_Ipack = false;	
				calculate_Iset = false;
				calculate_Idraw = false;
				calculate_Iprimitives = false;
				bt_Iresponce.setVisibility(View.INVISIBLE);
			}
			
			bt_Igray.setChecked(calculate_Igray);
			bt_Ipack.setChecked(calculate_Ipack);
			bt_Iset.setChecked(calculate_Iset);
			bt_Idraw.setChecked(calculate_Idraw);
			bt_Idraw.setChecked(calculate_Iprimitives);
		}
	};

	OnClickListener mIset_Listener = new OnClickListener() {
		public void onClick(View v) {
			calculate_Iset = !calculate_Iset;
			if (calculate_Iset)
			{
				calculate_Igray = true;
				calculate_Ipack = true;
			}
			else
			{	
				calculate_Idraw = false;
				calculate_Iprimitives = false;
				bt_Iresponce.setVisibility(View.INVISIBLE);
			}
			bt_Igray.setChecked(calculate_Igray);
			bt_Ipack.setChecked(calculate_Ipack);
			bt_Iset.setChecked(calculate_Iset);
			bt_Idraw.setChecked(calculate_Idraw);
			bt_Idraw.setChecked(calculate_Iprimitives);
		}
	};
	
	OnClickListener mIdraw_Listener = new OnClickListener() {
		public void onClick(View v) {			
			calculate_Idraw = !calculate_Idraw;
			if (calculate_Idraw)
			{
				calculate_Igray = true;
				calculate_Ipack = true;
				calculate_Iset = true;	
				bt_Iresponce.setVisibility(View.VISIBLE);
			}
			else
			{
				bt_Iresponce.setVisibility(View.INVISIBLE);
			}
			bt_Igray.setChecked(calculate_Igray);
			bt_Ipack.setChecked(calculate_Ipack);
			bt_Iset.setChecked(calculate_Iset);
			bt_Idraw.setChecked(calculate_Idraw);
			bt_Iprimitives.setChecked(calculate_Iprimitives);
		}
	};
	
	OnClickListener mIprimitives_Listener = new OnClickListener() {
		public void onClick(View v) {			
			calculate_Iprimitives = !calculate_Iprimitives;
			if (calculate_Iprimitives)
			{
				calculate_Igray = true;
				calculate_Ipack = true;
				calculate_Iset = true;	
				calculate_Idraw = true;
				bt_Iresponce.setVisibility(View.VISIBLE);
			}
			else
			{
				calculate_Iprimitives = false;
				bt_Iresponce.setVisibility(View.INVISIBLE);
			}
			bt_Igray.setChecked(calculate_Igray);
			bt_Ipack.setChecked(calculate_Ipack);
			bt_Iset.setChecked(calculate_Iset);
			bt_Idraw.setChecked(calculate_Idraw);
			bt_Iprimitives.setChecked(calculate_Iprimitives);
		}
	};
	
	OnClickListener mIresponce_Listener = new OnClickListener() {
		public void onClick(View v) {			
			calculate_Iresponce = !calculate_Iresponce;			
			bt_Iresponce.setChecked(calculate_Iresponce);
			if (calculate_Iresponce)
				android.os.Process.setThreadPriority(-20);
			else
				android.os.Process.setThreadPriority(cur_priority);			
		}
	};

	

	
	public void onStop() { // 'arrow' and 'home' buttons
		super.onDestroy();		
		System.exit(0);
	}

	
	// ----------------------------------------------------------------------

	class Preview extends SurfaceView implements SurfaceHolder.Callback,
	 Camera.PreviewCallback{ //???

	    Method mAcb;       // method for adding a pre-allocated buffer 
	    Object[] mArglist; // list of arguments
	    
		int framecounter;
		float fps;
		SurfaceHolder mHolder;
		Camera mCamera;	
		long startTime;

		Preview(Context context) {
			super(context);

			mHolder = getHolder();
			mHolder.addCallback(this);
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);			
			
			// initialize the timer
			startTime = System.currentTimeMillis();  
			framecounter = 0;
			fps = 0.0f;
		}

		// Preview is started or resumed
		public void surfaceCreated(SurfaceHolder holder) {
			// The Surface has been created, acquire the camera and tell it where
			// to draw.

			mCamera = Camera.open();			

			try {
				mCamera.setPreviewDisplay(mHolder);
				//mCamera.setPreviewDisplay(null);
				
			} catch (IOException exception) {
				mCamera.release();
				mCamera = null;
			}			

		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
			
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPreviewSize(w, h); // 800, 442
			mCamera.setParameters(parameters);				
			
			// newstuff
			PixelFormat p = new PixelFormat();
			PixelFormat.getPixelFormatInfo(parameters.getPreviewFormat(),p);
			int bufSize = (w*h*p.bitsPerPixel)/8;
			 //Must call this before calling addCallbackBuffer to get all the
	        // reflection variables setup
	        initForACB();
	        
	        
	        //Add three buffers to the buffer queue. I re-queue them once they are used in
	        // onPreviewFrame, so we should not need many of them.
	        byte[] buffer = new byte[bufSize];
	        addCallbackBuffer_Android2p2(buffer);                            
	      
	        
	        
	        setPreviewCallbackWithBuffer();

			//mCamera.setPreviewCallback(previewCallback);
			mCamera.startPreview();  
			
		}

	    /**
	     * This method will list all methods of the android.hardware.Camera class,
	     * even the hidden ones. With the information it provides, you can use the same
	     * approach I took below to expose methods that were written but hidden in eclair
	     */
	    private void listAllCameraMethods(){
	    	try {
				Class c = Class.forName("android.hardware.Camera");
				Method[] m = c.getMethods();
				for(int i=0; i<m.length; i++){
					Log.i("AR","  method:"+m[i].toString());
				}
			} catch (Exception e) {
				Log.i("AR",e.toString());
			}
	    }
	    
		/*
		 *  newstuff: addCallbackBuffer();
		 */
	    private void initForACB(){
	    	try {
				Class mC = Class.forName("android.hardware.Camera");
			
				Class[] mPartypes = new Class[1];
				// variable that will hold parameters for a function call
				mPartypes[0] = (new byte[1]).getClass(); //There is probably a better way to do this.
				mAcb = mC.getMethod("addCallbackBuffer", mPartypes);

				mArglist = new Object[1];
			} catch (Exception e) {
				Log.e("AR","Problem setting up for addCallbackBuffer: " + e.toString());
			}
	    }
	    
	    /**
	     * This method allows you to add a byte buffer to the queue of buffers to be used by preview.
	     * See: http://android.git.kernel.org/?p=platform/frameworks/base.git;a=blob;f=core/java/android/hardware/Camera.java;hb=9db3d07b9620b4269ab33f78604a36327e536ce1
	     * 
	     * @param b The buffer to register. Size should be width * height * bitsPerPixel / 8.
	     */
	    private void addCallbackBuffer_Android2p2(byte[] b){ //  this function is native in Android 2.2 ?!
	    	//Check to be sure initForACB has been called to setup
	    	// mAcb and mArglist
	    	if(mArglist == null){
	    		initForACB();
	    	}

	    	mArglist[0] = b;
	    	try {
	    		mAcb.invoke(mCamera, mArglist);
	    	} catch (Exception e) {
	    		Log.e("AR","invoking addCallbackBuffer failed: " + e.toString());
	    	}
	    }
	    
	    /**
	     * Use this method instead of setPreviewCallback if you want to use manually allocated
	     * buffers. Assumes that "this" implements Camera.PreviewCallback
	     */
	    private void setPreviewCallbackWithBuffer(){
	    	try {
				Class c = Class.forName("android.hardware.Camera");
				Method spcwb = null;  // sets a preview with buffers
				//This way of finding our method is a bit inefficient, but I am a reflection novice,
				// and didn't want to waste the time figuring out the right way to do it.
				// since this method is only called once, this should not cause performance issues			
				
				Method[] m = c.getMethods(); // get all methods of camera
				for(int i=0; i<m.length; i++){
					if(m[i].getName().compareTo("setPreviewCallbackWithBuffer") == 0){
						spcwb = m[i];
						break;
					}
				}
				
				
				/*
				Class[] mPartypes = new Class[1];
				mPartypes[0] = (new byte[1]).getClass(); //There is probably a better way to do this.
				spcwb = c.getMethod("setPreviewCallbackWithBuffer", mPartypes);
				*/
				
				//If we were able to find the setPreviewCallbackWithBuffer method of Camera, 
				// we can now invoke it on our Camera instance, setting 'this' to be the
				// callback handler
				if(spcwb != null){
					Object[] arglist = new Object[1];
					arglist[0] = this; // receives a copy of a preview frame
					spcwb.invoke(mCamera, this);
					//Log.i("AR","setPreviewCallbackWithBuffer: Called method");
				} else {
					Log.i("AR","setPreviewCallbackWithBuffer: Did not find method");
				}
				
			} catch (Exception e) {
				Log.i("AR",e.toString());
			}
	    }

		@Override
		public void onPreviewFrame(byte[] _data, Camera camera) {
			
			float Iav = 0;

			// did camera finished initialization?
			if (_data!=null)
			{
				if (calculate_Igray)
				{
					Iav = CppFunctionsJNI.YUVtoGray(IMAGE_W*IMAGE_H, im_gray, _data);

					if (calculate_Ipack)
					{
						CppFunctionsJNI.pack(IMAGE_W * IMAGE_H, pixels, im_gray,
								im_gray, im_gray);

						if (calculate_Iset)
						{
							bm.setPixels(pixels, 0, IMAGE_W, 0, 0, IMAGE_W, IMAGE_H);

							if (calculate_Idraw)
							{
								if (calculate_Iprimitives)
								{

									// Draw on canvas
									Paint paint = new Paint();
									if (framecounter%2==0)
									{
									paint.setStrokeWidth(2);
									paint.setColor(Color.GREEN);
									}
									else
									{
										paint.setStrokeWidth(4);
										paint.setColor(Color.RED);	
									}
									int N = 10;
									for (int startX=0; startX<IMAGE_W; startX+=IMAGE_W/N)
										mCanvas.drawLine(startX, 0, startX, IMAGE_H, paint);	
									for (int startY=0; startY<IMAGE_H; startY+=IMAGE_W/N)
										mCanvas.drawLine(0, startY, IMAGE_W, startY, paint);
								}

								mImageView.invalidate();
							}	

						}
					}
				}
			}
			
			
			framecounter++;          
			int nframes = 30;
			
			if (framecounter%nframes==0)
			{
				//camera.autoFocus(Focused);
				long millis = System.currentTimeMillis() - startTime;
				startTime = System.currentTimeMillis();
				fps = (1000.0f*nframes)/millis;
				if (calculate_Igray)
					scr_message.setText(" fps = " + Float.toString((int)(fps*10)/10.0f) + 
							"  (frames = " + Integer.toString(framecounter) + "); " +
							" Iav = " + Integer.toString((int) Iav) );
				else
					scr_message.setText(" fps = " + Float.toString((int)(fps*10)/10.0f) + 
							"  (frames = " + Integer.toString(framecounter) +")" );

				
			}			

		
			//We are done with this buffer, so add it back to the pool
			addCallbackBuffer_Android2p2(_data);			
			
		}
	
		
	} // surface view
	

	
} // activity

// -----------------------------------------------------------------------------




