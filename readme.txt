

mAcb = mC.getMethod("addCallbackBuffer", mPartypes);    // finds  a hidden methods "addCallbackBuffer"
mAcb.invoke(mCamera, mArglist); 						// invoked with a preallocated buffer in mArglist


// a bit tricky way to find another important function "setPreviewCallbackWithBuffer"
				Class c = Class.forName("android.hardware.Camera");				
				Method spcwb = null;
				Method[] m = c.getMethods(); // get all methods of camera
				for(int i=0; i<m.length; i++){
					if(m[i].getName().compareTo("setPreviewCallbackWithBuffer") == 0){
						spcwb = m[i];
						break;
					}
				}