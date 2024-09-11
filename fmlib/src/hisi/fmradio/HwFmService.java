//
// Copyright (C) 2024 The LineageOS Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// Author : Raphael Mounier (Iceows)
//

package hisi.fmradio;


import android.content.Context;
import android.media.AudioDeviceAttributes;
import android.media.AudioSystem;

import android.os.Binder;
import android.os.RemoteException;
import android.util.Log;


import java.util.Arrays;


public class HwFmService extends hisi.fmradio.IHwFmService.Stub {
    private static final int EVENT_LISTEN = 1;

    // Constants copied from AudioSystem
    private static final int DEVICE_OUT_FM              = 0x100000;
    private static final int DEVICE_IN_WIRED_HEADSET    = 0x400000;
    private static final int DEVICE_OUT_EARPIECE        = 0x1;
    private static final int DEVICE_OUT_WIRED_HEADSET   = 0x4;
    private static final int DEVICE_STATE_UNAVAILABLE   = 0;
    private static final int DEVICE_STATE_AVAILABLE     = 1;
    private static final int STD_BUF_SIZE = 128;
    private static final String TAG = "HwFmService";
    private Context mContext;
    private boolean mIsFmConnected = false;
    private int mUid = -1;
    private Thread mThread = null;
    private hisi.fmradio.IFmEventCallback mCallback = null;

    public HwFmService(Context context) {
        Log.d(TAG, "HwFmService constructor context");
        this.mContext = context;
    }

    public HwFmService() {
        Log.d(TAG, "HwFmService constructor");
    }


    /* Force device through AudioSystem  */
    private void setDeviceStatFM(int bState) {
        try {
            AudioDeviceAttributes attrDevice = new AudioDeviceAttributes(AudioSystem.DEVICE_OUT_FM,"","");

            int deviceConnectionState = AudioSystem.setDeviceConnectionState(attrDevice, bState, 0);
            if (deviceConnectionState == AudioSystem.SUCCESS) {
                Log.d("FMRadioService", "setDeviceConnectionState OK : " + bState);
            } else {
                Log.d("FMRadioService", "setDeviceConnectionState KO : " + deviceConnectionState);
            }
        } catch (Exception e) {
            Log.d("FMRadioService", "setDeviceConnectionState failed: " + e);
        }
    }
	
    @Override // com.huawei.android.hardware.fmradio.IHwFmService
    public int acquireFd(String path) {
        Log.d(TAG, "acquireFd");
        int uid = Binder.getCallingUid();
        Log.d(TAG, "acquireFd uid = " + uid);
        if (uid != this.mUid && this.mUid != -1) {
            Log.d(TAG, "support only one client now");
            return -1;
        }
        int fd = FmReceiverJNI.acquireFdNative(path);
        if (fd != -1) {
            this.mUid = uid;
        }
        return fd;
    }

    @Override // com.huawei.android.hardware.fmradio.IHwFmService
    public int audioControl(int fd, int control, int field) {
        return FmReceiverJNI.audioControlNative(fd, control, field);
    }


    @Override // com.huawei.android.hardware.fmradio.IHwFmService
    public int cancelSearch(int fd) {
        return FmReceiverJNI.cancelSearchNative(fd);
    }

    @Override // com.huawei.android.hardware.fmradio.IHwFmService
    public int closeFd(int fd) {
        int uid = Binder.getCallingUid();
        if (uid != this.mUid) {
            Log.d(TAG, "can not close fd");
            return -1;
        }
        this.mUid = -1;
        return FmReceiverJNI.closeFdNative(fd);
    }

    @Override // android.os.IHwFmService
    public int getFreq(int fd) {
        return FmReceiverJNI.getFreqNative(fd);
    }

    @Override // android.os.IHwFmService
    public int setFreq(int fd, int freq) {
        return FmReceiverJNI.setFreqNative(fd, freq);
    }

    @Override // hisi.fmradio.IHwFmService
    public int getControl(int fd, int id) {
        return FmReceiverJNI.getControlNative(fd, id);
    }

    @Override // hisi.fmradio.IHwFmService
    public int setControl(int fd, int id, int value) {
        int uid = Binder.getCallingUid();
        if (uid != this.mUid) {
            return -1;
        }
        return FmReceiverJNI.setControlNative(fd, id, value);
    }

    @Override // hisi.fmradio.IHwFmService
    public int startSearch(int fd, int dir) {
        return FmReceiverJNI.startSearchNative(fd, dir);
    }

    @Override // hisi.fmradio.IHwFmService
    public int getBuffer(int fd, byte[] buff, int index) {
        return FmReceiverJNI.getBufferNative(fd, buff, index);
    }

    @Override // hisi.fmradio.IHwFmService
    public int getRSSI(int fd) {
        return FmReceiverJNI.getRSSINative(fd);
    }

    @Override // hisi.fmradio.IHwFmService
    public int setBand(int fd, int low, int high) {
        return FmReceiverJNI.setBandNative(fd, low, high);
    }

    @Override // hisi.fmradio.IHwFmService
    public int getLowerBand(int fd) {
        return FmReceiverJNI.getLowerBandNative(fd);
    }

    @Override // hisi.fmradio.IHwFmService
    public int getUpperBand(int fd) {
        return FmReceiverJNI.getUpperBandNative(fd);
    }

    @Override // hisi.fmradio.IHwFmService
    public int setMonoStereo(int fd, int val) {
        return FmReceiverJNI.setMonoStereoNative(fd, val);
    }

    @Override // hisi.fmradio.IHwFmService
    public int getRawRds(int fd, byte[] buff, int count) {
        return FmReceiverJNI.getRawRdsNative(fd, buff, count);
    }

    @Override // hisi.fmradio.IHwFmService
    public void setNotchFilter(boolean value) {
        FmReceiverJNI.setNotchFilterNative(value);
    }

    @Override // hisi.fmradio.IHwFmService
    public int getAudioQuilty(int fd, int value) {
        return FmReceiverJNI.getAudioQuiltyNative(fd, value);
    }

    @Override // hisi.fmradio.IHwFmService
    public int setFmSnrThresh(int fd, int value) {
        return FmReceiverJNI.setFmSnrThreshNative(fd, value);
    }

    @Override // hisi.fmradio.IHwFmService
    public int setFmRssiThresh(int fd, int value) {
        return FmReceiverJNI.setFmRssiThreshNative(fd, value);
    }

    @Override // hisi.fmradio.IHwFmService
    public void setFmDeviceConnectionState(int state) {
        Log.d(HwFmService.TAG, "setFmDeviceConnectionState state: " + state);

        if (state == DEVICE_STATE_UNAVAILABLE && this.mIsFmConnected) {
            setDeviceStatFM(state);
            this.mIsFmConnected = false;
        } else if (state == DEVICE_STATE_AVAILABLE && (!this.mIsFmConnected)) {
            setDeviceStatFM(state);
            this.mIsFmConnected = true;
        }
    }

    @Override // hisi.fmradio.IHwFmService
    public void startListner(final int fd, hisi.fmradio.IFmEventCallback cb) {
        this.mCallback = cb;
        this.mThread = new Thread() {
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                byte[] buff = new byte[128];
                Log.d(HwFmService.TAG, "Starting listener " + fd);
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Arrays.fill(buff, (byte) 0);
                        int eventCount = HwFmService.this.getBuffer(fd, buff, 1);
                        Log.d(HwFmService.TAG, "Received event. Count: " + eventCount);
                        if (HwFmService.this.mCallback != null) {
                            for (int index = 0; index < eventCount; index++) {
                                Log.d(HwFmService.TAG, "Received <" + ((int) buff[index]) + ">");
                                switch (buff[index]) {
                                    case 1:
                                    case 2:
                                        HwFmService.this.mCallback.onEventCallback(buff[index], HwFmService.this.getFreq(fd), -1);
                                        break;
                                    case 3:
                                    case 4:
                                    case 5:
                                    case 6:
                                    case 7:
                                    default:
                                        HwFmService.this.mCallback.onEventCallback(buff[index], -1, -1);
                                        break;
                                    case 8:
                                    case 11:
                                    case 13:
                                        HwFmService.this.mCallback.onEventCallback(buff[index], 0, -1);
                                        break;
                                    case 9:
                                    case 10:
                                    case 12:
                                        HwFmService.this.mCallback.onEventCallback(buff[index], 1, -1);
                                        break;
                                }
                            }
                        }
                    } catch (RuntimeException ex) {
                        Log.d(HwFmService.TAG, ex.toString());
                        Thread.currentThread().interrupt();
                    } catch (Exception ex2) {
                        Log.d(HwFmService.TAG, "RunningThread InterruptedException ex = " + ex2);
                        Thread.currentThread().interrupt();
                        try {
                            if (HwFmService.this.mCallback != null) {
                                Log.d(HwFmService.TAG, "mCallback is not null");
                                HwFmService.this.mCallback.onEventCallback(2, -1, -1);
                            } else {
                                Log.d(HwFmService.TAG, "mCallback == null");
                            }
                        } catch (RemoteException rx) {
                            Log.d(HwFmService.TAG, "RunningThread InterruptedException for callback = " + rx);
                        }
                    }
                }
            }
        };
        this.mThread.start();
    }

    @Override // hisi.fmradio.IHwFmService
    public void stopListner() {
        int uid = Binder.getCallingUid();
        if (uid != this.mUid) {
            return;
        }
        this.mCallback = null;
        Log.d(TAG, "stopping the Listener\n");
        if (this.mThread != null) {
            this.mThread.interrupt();
        }
    }
}
