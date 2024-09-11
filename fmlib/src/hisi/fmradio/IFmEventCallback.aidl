/*
* aidl file :
* com/huawei/android/hardware/fmradio/IFmEventCallback.aidl
* This file contains definitions of functions which are
* exposed by service.
*
* Author : Raphael Mounier (Iceows)
*/


package hisi.fmradio;


/**{@hide}*/
oneway interface IFmEventCallback {
    void onEventCallback(int i, int i2, int i3);
}
