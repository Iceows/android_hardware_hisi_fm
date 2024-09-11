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

public class FmRxRdsData {
    private static final String LOGTAG = "FmRxRdsData";
    private static final int RDS_AF_AUTO = 64;
    private static final int RDS_GROUP_AF = 4;
    private static final int RDS_GROUP_PS = 2;
    private static final int RDS_GROUP_RT = 1;
    private static final int RDS_PS_ALL = 16;
    private static final int V4L2_CID_PRIVATE_BASE = 134217728;
    private static final int V4L2_CID_PRIVATE_TAVARUA_PSALL = 134217748;
    private static final int V4L2_CID_PRIVATE_TAVARUA_RDSD_BUF = 134217747;
    private static final int V4L2_CID_PRIVATE_TAVARUA_RDSGROUP_MASK = 134217734;
    private static final int V4L2_CID_PRIVATE_TAVARUA_RDSGROUP_PROC = 134217744;
    private static final int V4L2_CID_PRIVATE_TAVARUA_RDSON = 134217743;
    private int mFd;
    private int mECountryCode;
    private int mPrgmId;
    private String mPrgmServices;
    private int mPrgmType;
    private String mRadioText = "";
    private String mERadioText = "";

    // false means left-right
    // true means right-left
    private boolean formatting_dir = false;
    private byte []mTagCode = new byte[2];
    private String []mTag = new String[2];
    private int tag_nums = 0;
    private static final int MAX_NUM_TAG = 2;
    private boolean rt_ert_flag;

    public FmRxRdsData(int fd) {
        this.mFd = fd;
    }

    public int rdsOn(boolean on) {
        Log.d(LOGTAG, "In rdsOn: RDS is " + on);
        if (on) {
            int ret = FmReceiverWrapper.setControlNative(this.mFd, V4L2_CID_PRIVATE_TAVARUA_RDSON, 1);
            return ret;
        }
        int ret2 = FmReceiverWrapper.setControlNative(this.mFd, V4L2_CID_PRIVATE_TAVARUA_RDSON, 0);
        return ret2;
    }

    public int rdsGrpOptions(int grpMask, int buffSize, boolean rdsFilter) {
        int rdsFilt;
        byte rds_group_mask = (byte) FmReceiverWrapper.getControlNative(this.mFd, V4L2_CID_PRIVATE_TAVARUA_RDSGROUP_PROC);
        int rds_group_mask2 = (byte) (rds_group_mask & 254);
        if (rdsFilter) {
            rdsFilt = 1;
        } else {
            rdsFilt = 0;
        }
        int re = FmReceiverWrapper.setControlNative(this.mFd, V4L2_CID_PRIVATE_TAVARUA_RDSGROUP_PROC, (byte) (rds_group_mask2 | rdsFilt));
        if (re != 0) {
            return re;
        }
        int re2 = FmReceiverWrapper.setControlNative(this.mFd, V4L2_CID_PRIVATE_TAVARUA_RDSD_BUF, buffSize);
        if (re2 != 0) {
            return re2;
        }
        return FmReceiverWrapper.setControlNative(this.mFd, V4L2_CID_PRIVATE_TAVARUA_RDSGROUP_MASK, grpMask);
    }

    public int rdsOptions(int rdsMask) {
        byte rds_group_mask = (byte) FmReceiverWrapper.getControlNative(this.mFd, V4L2_CID_PRIVATE_TAVARUA_RDSGROUP_PROC);
        int psAllVal = rdsMask & 16;
        Log.d(LOGTAG, "In rdsOptions: rdsMask: " + rdsMask);
        FmReceiverWrapper.setControlNative(this.mFd, V4L2_CID_PRIVATE_TAVARUA_RDSGROUP_PROC, (byte) (((rdsMask & 7) << 3) | ((byte) (rds_group_mask & 199))));
        int re = FmReceiverWrapper.setControlNative(this.mFd, V4L2_CID_PRIVATE_TAVARUA_PSALL, psAllVal >> 4);
        return re;
    }

    public int enableAFjump(boolean AFenable) {
        Log.d(LOGTAG, "In enableAFjump: AFenable: " + AFenable);
        int rds_group_mask = FmReceiverWrapper.getControlNative(this.mFd, V4L2_CID_PRIVATE_TAVARUA_RDSGROUP_PROC);
        Log.d(LOGTAG, "In enableAFjump: rds_group_mask: " + rds_group_mask);
        if (AFenable) {
            FmReceiverWrapper.setControlNative(this.mFd, V4L2_CID_PRIVATE_TAVARUA_RDSGROUP_PROC, rds_group_mask | RDS_AF_AUTO);
            return 1;
        }
        FmReceiverWrapper.setControlNative(this.mFd, V4L2_CID_PRIVATE_TAVARUA_RDSGROUP_PROC, rds_group_mask & (-65));
        return 1;
    }

    public String getRadioText() {
        return this.mRadioText;
    }

    public void setRadioText(String x) {
        this.mRadioText = x;
    }

    /*****************************************************************
     *
     * All this functions after don't exist in EMUI8 framework
     * TODO Iceows
     *
     *  */
    public String getERadioText () {
        return mERadioText;
    }

    public void setERadioText (String x) {
        mERadioText = x;
    }
    public String getPrgmServices() {
        return this.mPrgmServices;
    }

    public void setPrgmServices(String x) {
        this.mPrgmServices = x;
    }

    public int getPrgmId() {
        return this.mPrgmId;
    }

    public void setPrgmId(int x) {
        this.mPrgmId = x;
    }

    public int getPrgmType() {
        return this.mPrgmType;
    }

    public void setPrgmType(int x) {
        this.mPrgmType = x;
    }

    public void setECountryCode(int x) {
        mECountryCode = x;
    }
    public int getECountryCode() {
        return mECountryCode;
    }
    public boolean getFormatDir() {
        return formatting_dir;
    }
    public void setFormatDir(boolean dir) {
        formatting_dir = dir;
    }
    public void setTagValue (String x, int tag_num) {
        if ((tag_num > 0) && (tag_num <= MAX_NUM_TAG)) {
            mTag[tag_num - 1] = x;
            tag_nums++;
        }
    }
    public void setTagCode (byte tag_code, int tag_num) {
        if ((tag_num > 0) && (tag_num <= MAX_NUM_TAG))
            mTagCode[tag_num - 1] = tag_code;
    }
    public String getTagValue (int tag_num) {
        if ((tag_num > 0) && (tag_num <= MAX_NUM_TAG))
            return mTag[tag_num - 1];
        else
            return "";
    }
    public byte getTagCode (int tag_num) {
        if ((tag_num > 0) && (tag_num <= MAX_NUM_TAG))
            return mTagCode[tag_num - 1];
        else
            return 0;
    }
    public int getTagNums() {
        return tag_nums;
    }
    public void setTagNums(int x) {
        if ((x >= 0) && (x <= MAX_NUM_TAG))
            tag_nums = x;
    }
}
