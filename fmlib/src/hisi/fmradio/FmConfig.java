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


public class FmConfig {
    private static final int FM_EU_BAND = 1;
    private static final int FM_JAPAN_STANDARD_BAND = 3;
    private static final int FM_JAPAN_WIDE_BAND = 2;
    private static final int FM_USER_DEFINED_BAND = 4;
    private static final int FM_US_BAND = 0;
    private static final String TAG = "FmConfig";
    private static final int V4L2_CID_PRIVATE_BASE = 134217728;
    private static final int V4L2_CID_PRIVATE_TAVARUA_EMPHASIS = 134217740;
    private static final int V4L2_CID_PRIVATE_TAVARUA_RDS_STD = 134217741;
    private static final int V4L2_CID_PRIVATE_TAVARUA_REGION = 134217735;
    private static final int V4L2_CID_PRIVATE_TAVARUA_SPACING = 134217742;
    private int mBandLowerLimit;
    private int mBandUpperLimit;
    private int mChSpacing;
    private int mEmphasis;
    private int mRadioBand;
    private int mRdsStd;

    public int getRadioBand() {
        return this.mRadioBand;
    }

    public void setRadioBand(int band) {
        this.mRadioBand = band;
    }

    public int getEmphasis() {
        return this.mEmphasis;
    }

    public void setEmphasis(int emp) {
        this.mEmphasis = emp;
    }

    public int getChSpacing() {
        return this.mChSpacing;
    }

    public void setChSpacing(int spacing) {
        this.mChSpacing = spacing;
    }

    public int getRdsStd() {
        return this.mRdsStd;
    }

    public void setRdsStd(int rdsStandard) {
        this.mRdsStd = rdsStandard;
    }

    public int getLowerLimit() {
        return this.mBandLowerLimit;
    }

    public void setLowerLimit(int lowLimit) {
        this.mBandLowerLimit = lowLimit;
    }

    public int getUpperLimit() {
        return this.mBandUpperLimit;
    }

    public void setUpperLimit(int upLimit) {
        this.mBandUpperLimit = upLimit;
    }

    public static boolean fmConfigure(int fd, FmConfig configSettings) {
        Log.d(TAG, "In fmConfigure");
        FmReceiverWrapper.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_EMPHASIS, configSettings.getEmphasis());
        FmReceiverWrapper.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_RDS_STD, configSettings.getRdsStd());
        FmReceiverWrapper.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_SPACING, configSettings.getChSpacing());
        FmReceiverWrapper.setBandNative(fd, configSettings.getLowerLimit(), configSettings.getUpperLimit());
        int re = FmReceiverWrapper.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_REGION, 4);
        return re >= 0;
    }
}