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

import hisi.fmradio.FmReceiverJNI;

class FmRxControls {
    static final int FREQ_MUL = 1000;
    static final int SCAN_BACKWARD = 3;
    static final int SCAN_FORWARD = 2;
    static final int SEEK_BACKWARD = 1;
    static final int SEEK_FORWARD = 0;
    private static final String TAG = "FmRxControls";

    private static final int V4L2_CTRL_CLASS_USER = 0x980000;

    private static final int V4L2_CID_AUDIO_MUTE = 9963785;
    private static final int V4L2_CID_BASE = 9963776;


    private static final int V4L2_CID_PRIVATE_BASE = 134217728;
    private static final int V4L2_CID_PRIVATE_TAVARUA_EMPHASIS = 134217740;
    private static final int V4L2_CID_PRIVATE_TAVARUA_LP_MODE = 134217745;
    private static final int V4L2_CID_PRIVATE_TAVARUA_RDSGROUP_MASK = 134217734;
    private static final int V4L2_CID_PRIVATE_TAVARUA_RDSGROUP_PROC = 134217744;
    private static final int V4L2_CID_PRIVATE_TAVARUA_RDSON = 134217743;
    private static final int V4L2_CID_PRIVATE_TAVARUA_RDS_STD = 134217741;
    private static final int V4L2_CID_PRIVATE_TAVARUA_REGION = 134217735;
    private static final int V4L2_CID_PRIVATE_TAVARUA_SCANDWELL = 134217730;
    private static final int V4L2_CID_PRIVATE_TAVARUA_SIGNAL_TH = 134217736;
    private static final int V4L2_CID_PRIVATE_TAVARUA_SPACING = 134217742;
    private static final int V4L2_CID_PRIVATE_TAVARUA_SRCHMODE = 134217729;
    private static final int V4L2_CID_PRIVATE_TAVARUA_SRCHON = 134217731;
    private static final int V4L2_CID_PRIVATE_TAVARUA_SRCH_CNT = 134217739;
    private static final int V4L2_CID_PRIVATE_TAVARUA_SRCH_PI = 134217738;
    private static final int V4L2_CID_PRIVATE_TAVARUA_SRCH_PTY = 134217737;
    private static final int V4L2_CID_PRIVATE_TAVARUA_STATE = 134217732;
    private static final int V4L2_CID_PRIVATE_TAVARUA_TRANSMIT_MODE = 134217733;


    // TODO Iceows
    private static final int V4L2_CID_PRIVATE_TAVARUA_HLSI = V4L2_CID_PRIVATE_BASE + 29;

    private static final int V4L2_CID_PRIVATE_TAVARUA_ON_CHANNEL_THRESHOLD = V4L2_CID_PRIVATE_BASE + 0x2D;

    private static final int V4L2_CID_PRIVATE_TAVARUA_RDSD_BUF = V4L2_CID_PRIVATE_BASE + 19;
    private static final int V4L2_CID_PRIVATE_TAVARUA_IOVERC = V4L2_CID_PRIVATE_BASE + 24;
    private static final int V4L2_CID_PRIVATE_TAVARUA_INTDET = V4L2_CID_PRIVATE_BASE + 25;
    private static final int V4L2_CID_PRIVATE_TAVARUA_MPX_DCC = V4L2_CID_PRIVATE_BASE + 26;

    private static final int V4L2_CID_PRIVATE_SINR = V4L2_CID_PRIVATE_BASE + 44;

    private static final int V4L2_CID_PRIVATE_TAVARUA_OFF_CHANNEL_THRESHOLD = V4L2_CID_PRIVATE_BASE + 0x2E;
    private static final int V4L2_CID_PRIVATE_TAVARUA_SINR_THRESHOLD = V4L2_CID_PRIVATE_BASE + 0x2F;
    private static final int V4L2_CID_PRIVATE_TAVARUA_SINR_SAMPLES = V4L2_CID_PRIVATE_BASE + 0x30;
    private static final int V4L2_CID_PRIVATE_SPUR_FREQ = V4L2_CID_PRIVATE_BASE + 0x31;
    private static final int V4L2_CID_PRIVATE_SPUR_FREQ_RMSSI = V4L2_CID_PRIVATE_BASE + 0x32;
    private static final int V4L2_CID_PRIVATE_SPUR_SELECTION = V4L2_CID_PRIVATE_BASE + 0x33;
    private static final int V4L2_CID_PRIVATE_AF_RMSSI_TH = V4L2_CID_PRIVATE_BASE + 0x36;
    private static final int V4L2_CID_PRIVATE_AF_RMSSI_SAMPLES = V4L2_CID_PRIVATE_BASE + 0x37;
    private static final int V4L2_CID_PRIVATE_GOOD_CH_RMSSI_TH = V4L2_CID_PRIVATE_BASE + 0x38;
    private static final int V4L2_CID_PRIVATE_SRCHALGOTYPE = V4L2_CID_PRIVATE_BASE + 0x39;
    private static final int V4L2_CID_PRIVATE_CF0TH12 = V4L2_CID_PRIVATE_BASE + 0x3A;
    private static final int V4L2_CID_PRIVATE_SINRFIRSTSTAGE = V4L2_CID_PRIVATE_BASE + 0x3B;
    private static final int V4L2_CID_PRIVATE_RMSSIFIRSTSTAGE = V4L2_CID_PRIVATE_BASE + 0x3C;
    private static final int V4L2_CID_PRIVATE_RXREPEATCOUNT = V4L2_CID_PRIVATE_BASE + 0x3D;
    private static final int V4L2_CID_PRIVATE_RSSI_TH = V4L2_CID_PRIVATE_BASE + 0x3E;
    private static final int V4L2_CID_PRIVATE_AF_JUMP_RSSI_TH = V4L2_CID_PRIVATE_BASE + 0x3F;
    private static final int V4L2_CID_PRIVATE_BLEND_SINRHI = V4L2_CID_PRIVATE_BASE + 0x40;
    private static final int V4L2_CID_PRIVATE_BLEND_RMSSIHI = V4L2_CID_PRIVATE_BASE + 0x41;



    private int mFreq;

    public void fmOn(int fd, int device) {
        FmReceiverWrapper.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_STATE, device);
    }

    public void fmOff(int fd) {
        FmReceiverWrapper.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_STATE, 0);
    }

    public void muteControl(int fd, boolean on) {
        if (on) {
            FmReceiverWrapper.setControlNative(fd, V4L2_CID_AUDIO_MUTE, 3);
        } else {
            FmReceiverWrapper.setControlNative(fd, V4L2_CID_AUDIO_MUTE, 0);
        }
    }

    public int setStation(int fd) {
        Log.d(TAG, "** Tune Using: " + fd);
        int ret = FmReceiverWrapper.setFreqNative(fd, this.mFreq);
        Log.d(TAG, "** Returned: " + ret);
        return ret;
    }

    public int getTunedFrequency(int fd) {
        int frequency = FmReceiverWrapper.getFreqNative(fd);
        Log.d(TAG, "getTunedFrequency: " + frequency);
        return frequency;
    }

    public int getFreq() {
        return this.mFreq;
    }

    public void setFreq(int f) {
        this.mFreq = f;
    }

    public int searchStationList(int fd, int mode, int preset_num, int dir, int pty) {
        int re = FmReceiverWrapper.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_SRCHMODE, mode);
        if (re != 0) {
            return re;
        }
        int re2 = FmReceiverWrapper.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_SRCH_CNT, preset_num);
        if (re2 != 0) {
            return re2;
        }
        if (pty > 0) {
            re2 = FmReceiverWrapper.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_SRCH_PTY, pty);
        }
        if (re2 != 0) {
            return re2;
        }
        int re3 = FmReceiverWrapper.startSearchNative(fd, dir);
        if (re3 != 0) {
            return re3;
        }
        return 0;
    }

    public int[] stationList(int fd) {
        byte[] sList = new byte[100];
        float lowBand = (float) (FmReceiverWrapper.getLowerBandNative(fd) / 1000.0d);
        Log.d(TAG, "lowBand: " + lowBand);
        FmReceiverWrapper.getBufferNative(fd, sList, 0);
        int station_num = sList[0];
        int[] stationList = new int[station_num + 1];
        Log.d(TAG, "station_num: " + station_num);
        for (int i = 0; i < station_num; i++) {
            Log.d(TAG, " Byte1 = " + ((int) sList[(i * 2) + 1]));
            Log.d(TAG, " Byte2 = " + ((int) sList[(i * 2) + 2]));
            int tmpFreqByte1 = sList[(i * 2) + 1] & 255;
            int tmpFreqByte2 = sList[(i * 2) + 2] & 255;
            Log.d(TAG, " tmpFreqByte1 = " + tmpFreqByte1);
            Log.d(TAG, " tmpFreqByte2 = " + tmpFreqByte2);
            int freq = ((tmpFreqByte1 & 3) << 8) | tmpFreqByte2;
            Log.d(TAG, " freq: " + freq);
            float real_freq = (freq * 50) + (1000.0f * lowBand);
            Log.d(TAG, " real_freq: " + real_freq);
            stationList[i] = (int) real_freq;
            Log.d(TAG, " stationList: " + stationList[i]);
        }
        try {
            stationList[station_num] = 0;
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.d(TAG, "ArrayIndexOutOfBoundsException !!");
        }
        return stationList;
    }

    public void searchStations(int fd, int mode, int dwell, int dir, int pty, int pi) {
        Log.d(TAG, "Mode is " + mode + " Dwell is " + dwell);
        Log.d(TAG, "dir is " + dir + " PTY is " + pty);
        Log.d(TAG, "pi is " + pi + " id " + V4L2_CID_PRIVATE_TAVARUA_SRCHMODE);
        FmReceiverWrapper.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_SRCHMODE, mode);
        FmReceiverWrapper.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_SCANDWELL, dwell);
        if (pty != 0) {
            FmReceiverWrapper.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_SRCH_PTY, pty);
        }
        if (pi != 0) {
            FmReceiverWrapper.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_SRCH_PI, pi);
        }
        FmReceiverWrapper.startSearchNative(fd, dir);
    }

    public int stereoControl(int fd, boolean stereo) {
        if (stereo) {
            return FmReceiverWrapper.setMonoStereoNative(fd, 1);
        }
        return FmReceiverWrapper.setMonoStereoNative(fd, 0);
    }

    public void searchRdsStations(int mode, int dwelling, int direction, int RdsSrchPty, int RdsSrchPI) {
    }

    public void cancelSearch(int fd) {
        FmReceiverWrapper.cancelSearchNative(fd);
    }

    public int setLowPwrMode(int fd, boolean lpmOn) {
        if (lpmOn) {
            int re = FmReceiverWrapper.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_LP_MODE, 1);
            return re;
        }
        int re2 = FmReceiverWrapper.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_LP_MODE, 0);
        return re2;
    }

    public int getPwrMode(int fd) {
        int re = FmReceiverWrapper.getControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_LP_MODE);
        return re;
    }

    /*****************************************************************
     *
     * All this functions after don't exist in EMUI8 framework
     * TODO Iceows
     *
     *  */
    public boolean setPSRxRepeatCount(int fd, int count) {
        int ret;
        ret = hisi.fmradio.FmReceiverJNI.setControlNative(fd, V4L2_CID_PRIVATE_RXREPEATCOUNT, count);
        if (ret < 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean getPSRxRepeatCount(int fd) {
        int ret;
        ret = hisi.fmradio.FmReceiverJNI.getControlNative(fd, V4L2_CID_PRIVATE_RXREPEATCOUNT);
        if (ret < 0) {
            return false;
        } else {
            return true;
        }
    }

    /*
     * Get SINR value
     */
    public int getSINR(int fd)
    {
        return  FmReceiverJNI.getControlNative(fd, V4L2_CID_PRIVATE_SINR);
    }

    /*
     * Set Hi-Low injection
     */
    public int setHiLoInj(int fd, int inj) {
        int re = hisi.fmradio.FmReceiverJNI.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_HLSI, inj);
        return re;
    }

    /*.
     * Set On channel threshold
     */
    public int setOnChannelThreshold(int fd, int sBuff) {
        int re = hisi.fmradio.FmReceiverJNI.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_ON_CHANNEL_THRESHOLD, sBuff);
        if (re < 0)
            Log.e(TAG, "Failed to set On channel threshold data");
        return re;
    }

    public int IovercControl(int fd) {
        int ioverc = hisi.fmradio.FmReceiverJNI.getControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_IOVERC);
        Log.d(TAG, "IOVERC value is : " + ioverc);
        return ioverc;
    }

    /*
     * Get IntDet
     */
    public int IntDet(int fd) {
        int intdet = hisi.fmradio.FmReceiverJNI.getControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_INTDET);
        Log.d(TAG, "IOVERC value is : " + intdet);
        return intdet;
    }

    /*
     * Get MPX_DCC
     */
    public int Mpx_Dcc(int fd) {
        int mpx_dcc = hisi.fmradio.FmReceiverJNI.getControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_MPX_DCC);
        Log.d(TAG, "MPX_DCC value is : " + mpx_dcc);
        return mpx_dcc;
    }

    /*
     * Set number of sinr samples to take in to account for SINR avg calculation
     */
    public int setSINRsamples(int fd, int sBuff) {
        int re = hisi.fmradio.FmReceiverJNI.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_SINR_SAMPLES, sBuff);
        if (re < 0)
            Log.e(TAG, "Failed to set SINR samples ");
        return re;
    }

    /*
     * Get SINR samples
     */
    public int getSINRsamples(int fd) {
        return hisi.fmradio.FmReceiverJNI.getControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_SINR_SAMPLES);
    }


    public byte getBlendSinr(int fd) {
        return (byte) hisi.fmradio.FmReceiverJNI.getControlNative(fd, V4L2_CID_PRIVATE_BLEND_SINRHI);
    }

    public boolean setBlendSinr(int fd, int sinrHi) {
        int ret;
        ret = hisi.fmradio.FmReceiverJNI.setControlNative(fd, V4L2_CID_PRIVATE_BLEND_SINRHI, sinrHi);
        if (ret < 0) {
            Log.e(TAG, "Error in setting sinrHi ");
            return false;
        } else {
            return true;
        }
    }

    public byte getBlendRmssi(int fd) {
        return (byte) hisi.fmradio.FmReceiverJNI.getControlNative(fd, V4L2_CID_PRIVATE_BLEND_RMSSIHI);
    }

    public boolean setBlendRmssi(int fd, int rmssiHi) {
        int ret;
        ret = hisi.fmradio.FmReceiverJNI.setControlNative(fd, V4L2_CID_PRIVATE_BLEND_RMSSIHI, rmssiHi);
        if (ret < 0) {
            Log.e(TAG, "Error in setting RmssiHi ");
            return false;
        } else {
            return true;
        }
    }

    /*
     * Set sinr threshold
     */
    public int setSINRThreshold(int fd, int sBuff) {
        int re = hisi.fmradio.FmReceiverJNI.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_SINR_THRESHOLD, sBuff);
        if (re < 0)
            Log.e(TAG, "Failed to set SINR threshold data");
        return re;
    }

    /*
     * Get SINR threshold
     */
    public int getSINRThreshold(int fd) {
        return hisi.fmradio.FmReceiverJNI.getControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_SINR_THRESHOLD);
    }

    /*
     * Get On channel threshold
     */
    public int getOnChannelThreshold(int fd) {
        return hisi.fmradio.FmReceiverJNI.getControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_ON_CHANNEL_THRESHOLD);
    }

    /*
     * Set Off channel threshold
     */
    public int setOffChannelThreshold(int fd, int sBuff) {
        int re = hisi.fmradio.FmReceiverJNI.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_OFF_CHANNEL_THRESHOLD, sBuff);
        if (re < 0)
            Log.e(TAG, "Failed to set Off channel Threshold data");
        return re;
    }

    /*
     * Get Off channel threshold
     */
    public int getOffChannelThreshold(int fd) {
        return hisi.fmradio.FmReceiverJNI.getControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_OFF_CHANNEL_THRESHOLD);
    }


    public int getSearchAlgoType(int fd) {
        return hisi.fmradio.FmReceiverJNI.getControlNative(fd, V4L2_CID_PRIVATE_SRCHALGOTYPE);
    }

    public boolean setSearchAlgoType(int fd, int saerchType) {
        int ret;
        ret = hisi.fmradio.FmReceiverJNI.setControlNative(fd, V4L2_CID_PRIVATE_SRCHALGOTYPE, saerchType);
        if (ret < 0) {
            Log.e(TAG, "Error in setting Search Algo type");
            return false;
        } else {
            return true;
        }
    }

    public int getSinrFirstStage(int fd) {
        return hisi.fmradio.FmReceiverJNI.getControlNative(fd, V4L2_CID_PRIVATE_SINRFIRSTSTAGE);
    }

    public boolean setSinrFirstStage(int fd, int sinr) {
        int ret;
        ret = hisi.fmradio.FmReceiverJNI.setControlNative(fd, V4L2_CID_PRIVATE_SINRFIRSTSTAGE, sinr);
        if (ret < 0) {
            Log.e(TAG, "Error in setting Sinr First Stage Threshold");
            return false;
        } else {
            return true;
        }
    }

    public int getRmssiFirstStage(int fd) {
        return hisi.fmradio.FmReceiverJNI.getControlNative(fd, V4L2_CID_PRIVATE_RMSSIFIRSTSTAGE);
    }

    public boolean setRmssiFirstStage(int fd, int rmssi) {
        int ret;
        ret = hisi.fmradio.FmReceiverJNI.setControlNative(fd, V4L2_CID_PRIVATE_RMSSIFIRSTSTAGE, rmssi);
        if (ret < 0) {
            Log.e(TAG, "Error in setting Rmssi First stage Threshold");
            return false;
        } else {
            return true;
        }
    }

    public int getCFOMeanTh(int fd) {
        return hisi.fmradio.FmReceiverJNI.getControlNative(fd, V4L2_CID_PRIVATE_CF0TH12);
    }

    public boolean setCFOMeanTh(int fd, int th) {
        int ret;
        ret = hisi.fmradio.FmReceiverJNI.setControlNative(fd, V4L2_CID_PRIVATE_CF0TH12, th);
        if (ret < 0) {
            Log.e(TAG, "Error in setting Mean CFO Threshold");
            return false;
        } else {
            return true;
        }
    }

    public int getAFJumpRmssiSamples(int fd) {
        return hisi.fmradio.FmReceiverJNI.getControlNative(fd, V4L2_CID_PRIVATE_AF_RMSSI_SAMPLES);
    }


    public int getGdChRmssiTh(int fd) {
        return hisi.fmradio.FmReceiverJNI.getControlNative(fd, V4L2_CID_PRIVATE_GOOD_CH_RMSSI_TH);
    }

    public boolean setGdChRmssiTh(int fd, int th) {
        int re;
        re = hisi.fmradio.FmReceiverJNI.setControlNative(fd, V4L2_CID_PRIVATE_GOOD_CH_RMSSI_TH, th);
        if (re < 0) {
            Log.e(TAG, "Error in setting Good channel Rmssi Threshold");
            return false;
        } else {
            return true;
        }
    }

    public int getAFJumpRmssiTh(int fd) {
        return hisi.fmradio.FmReceiverJNI.getControlNative(fd, V4L2_CID_PRIVATE_AF_RMSSI_TH);
    }

    public boolean setAFJumpRmssiTh(int fd, int th) {
        int re;
        re = hisi.fmradio.FmReceiverJNI.setControlNative(fd, V4L2_CID_PRIVATE_AF_RMSSI_TH, th);
        if (re < 0) {
            Log.e(TAG, "Error in setting AF jmp Rmssi Threshold");
            return false;
        } else {
            return true;
        }
    }

    public boolean setAFJumpRmssiSamples(int fd, int samples) {
        int re;
        re = hisi.fmradio.FmReceiverJNI.setControlNative(fd, V4L2_CID_PRIVATE_AF_RMSSI_SAMPLES, samples);
        if (re < 0) {
            Log.e(TAG, "Error in setting AF jmp Rmssi Samples");
            return false;
        } else {
            return true;
        }
    }

    /*
     * Set rssi threshold
     */
    public int setRssiThreshold(int fd, int sBuff) {
        int re = hisi.fmradio.FmReceiverJNI.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_SIGNAL_TH, sBuff);
        if (re < 0)
            Log.e(TAG, "Failed to set RSSI threshold data");
        return re;
    }

    /*
     * Get Rssi threshold
     */
    public int getRssiThreshold(int fd) {
        return hisi.fmradio.FmReceiverJNI.getControlNative(fd, V4L2_CID_PRIVATE_RSSI_TH);
    }

    /*
     * Set RDS FIFO count
     */
    public int setRdsFifoCnt(int fd, int sBuff) {
        int re = hisi.fmradio.FmReceiverJNI.setControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_RDSD_BUF, sBuff);
        if (re < 0)
            Log.e(TAG, "Failed to set RDS fifo count data");
        return re;
    }

    /*
     * Get RDS FIFO count
     */
    public int getRdsFifoCnt(int fd) {
        return hisi.fmradio.FmReceiverJNI.getControlNative(fd, V4L2_CID_PRIVATE_TAVARUA_RDSD_BUF);
    }

    /*
     * Get AF jump Rssi threshold
     */
    public int getAfJumpRssiThreshold(int fd) {
        return hisi.fmradio.FmReceiverJNI.getControlNative(fd, V4L2_CID_PRIVATE_AF_JUMP_RSSI_TH);
    }


    /*
     * Set AF jump rssi threshold
     */
    public int setAfJumpRssiThreshold(int fd, int sBuff) {
        int re = hisi.fmradio.FmReceiverJNI.setControlNative(fd, V4L2_CID_PRIVATE_AF_JUMP_RSSI_TH, sBuff);
        if (re < 0)
            Log.e(TAG, "Failed to set AF Jump Rssithreshold data");
        return re;
    }

    public boolean enableSlimbus(int fd, int enable) {
        int ret;
        Log.d(TAG, "enableSlimbus : enable = " + enable);
        ret = FmReceiverJNI.enableSlimbus(fd, enable);
        if (ret == 0)
            return true;
        else
            return false;
    }
    public boolean enableSoftMute(int fd, int enable) {
        int ret;
        Log.d(TAG, "enableSoftMute : enable = " + enable);
        ret = FmReceiverJNI.enableSoftMute(fd, enable);
        if (ret == 0)
            return true;
        else
            return false;
    }
}