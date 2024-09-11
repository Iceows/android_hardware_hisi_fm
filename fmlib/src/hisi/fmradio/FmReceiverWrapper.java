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

import android.util.Log;

import hisi.fmradio.HwFmService;


class FmReceiverWrapper {
    static final int FM_JNI_FAILURE = -1;
    static final int FM_JNI_SUCCESS = 0;

    private static final String TAG = "FmReceiverWrapper";
    private static hisi.fmradio.HwFmService sService;

    FmReceiverWrapper() {
    }

    private static hisi.fmradio.HwFmService getService() {
        if (sService != null) {
            return sService;
        }

        Log.d(TAG, "try to get service HwFmService ");
        sService = new HwFmService();
        return sService;
    }

    public static int acquireFdNative(String path) {
        return getService().acquireFd(path);
    }

    static int audioControlNative(int fd, int control, int field) {
        return getService().audioControl(fd, control, field);
    }

    public static int cancelSearchNative(int fd) {
        return getService().cancelSearch(fd);
    }

    public static int closeFdNative(int fd) {
        return getService().closeFd(fd);
    }

    public static int getFreqNative(int fd) {
        return getService().getFreq(fd);
    }

    public static int setFreqNative(int fd, int freq) {
        return getService().setFreq(fd, freq);
    }

    public static int getControlNative(int fd, int id) {
        return getService().getControl(fd, id);
    }

    public static int setControlNative(int fd, int id, int value) {
        return getService().setControl(fd, id, value);

    }

    public static int startSearchNative(int fd, int dir) {
        return getService().startSearch(fd, dir);
    }

    public static int getBufferNative(int fd, byte[] buff, int index) {
        return getService().getBuffer(fd, buff, index);
    }

    public static int getRSSINative(int fd) {
        return getService().getRSSI(fd);
    }

    public static int setBandNative(int fd, int low, int high) {
        return getService().setBand(fd, low, high);
    }

    public static int getLowerBandNative(int fd) {
        return getService().getLowerBand(fd);
    }

    static int getUpperBandNative(int fd) {
        return getService().getUpperBand(fd);
    }

    public static int setMonoStereoNative(int fd, int val) {
        return getService().setMonoStereo(fd, val);
    }

    public static int getRawRdsNative(int fd, byte[] buff, int count) {
        return getService().getRawRds(fd, buff, count);
    }

    public static void setNotchFilterNative(boolean value) {
        getService().setNotchFilter(value);
    }

    public static int getAudioQuiltyNative(int fd, int value) {
        return getService().getAudioQuilty(fd, value);
    }

    public static int setFmSnrThreshNative(int fd, int value) {
        return getService().setFmSnrThresh(fd, value);
    }

    public static int setFmRssiThreshNative(int fd, int value) {
        return getService().setFmRssiThresh(fd, value);
    }

    public static void setFmDeviceConnectionState(int state) {
        getService().setFmDeviceConnectionState(state);
    }

    public static void startListner(int fd, hisi.fmradio.IFmEventCallback cb) {
        getService().startListner(fd, cb);
    }

    public static void stopListner() {
        getService().stopListner();
    }
}