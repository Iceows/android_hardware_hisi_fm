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

import static hisi.fmradio.FmTransmitter.ERROR;

import android.content.Context;
import android.util.Log;

import java.nio.charset.Charset;

import hisi.fmradio.FmReceiverJNI;


public class FmReceiver extends FmTransceiver {
    public static final int FM_RX_AUDIO_MODE_MONO = 1;
    public static final int FM_RX_AUDIO_MODE_STEREO = 0;
    public static final int FM_RX_DWELL_PERIOD_1S = 1;
    public static final int FM_RX_DWELL_PERIOD_2S = 2;
    public static final int FM_RX_DWELL_PERIOD_3S = 3;
    public static final int FM_RX_DWELL_PERIOD_4S = 4;
    public static final int FM_RX_DWELL_PERIOD_5S = 5;
    public static final int FM_RX_DWELL_PERIOD_6S = 6;
    public static final int FM_RX_DWELL_PERIOD_7S = 7;
    public static final int FM_RX_LOW_POWER_MODE = 1;
    public static final int FM_RX_MUTE = 1;
    public static final int FM_RX_NORMAL_POWER_MODE = 0;
    public static final int FM_RX_RDS_GRP_RT_EBL = 1;
    public static final int FM_RX_RDS_GRP_PS_EBL = 2;
    public static final int FM_RX_RDS_GRP_AF_EBL = 4;
    public static final int FM_RX_RDS_GRP_PS_SIMPLE_EBL = 16;


    public static final int FM_RX_RDS_GRP_ECC_EBL        =32;
    public static final int FM_RX_RDS_GRP_PTYN_EBL       =64;
    public static final int FM_RX_RDS_GRP_RT_PLUS_EBL    =128;
    private static final int FM_RX_RSSI_LEVEL_STRONG = -96;
    private static final int FM_RX_RSSI_LEVEL_VERY_STRONG = -90;
    private static final int FM_RX_RSSI_LEVEL_VERY_WEAK = -105;
    private static final int FM_RX_RSSI_LEVEL_WEAK = -100;
    public static final int FM_RX_SCREEN_OFF_MODE = 0;
    public static final int FM_RX_SCREEN_ON_MODE = 1;
    public static final int FM_RX_SEARCHDIR_DOWN = 0;
    public static final int FM_RX_SEARCHDIR_UP = 1;
    public static final int FM_RX_SIGNAL_STRENGTH_STRONG = 2;
    public static final int FM_RX_SIGNAL_STRENGTH_VERY_STRONG = 3;
    public static final int FM_RX_SIGNAL_STRENGTH_VERY_WEAK = 0;
    public static final int FM_RX_SIGNAL_STRENGTH_WEAK = 1;
    public static final int FM_RX_SRCHLIST_MAX_STATIONS = 12;
    public static final int FM_RX_SRCHLIST_MODE_STRONG = 2;
    public static final int FM_RX_SRCHLIST_MODE_STRONGEST = 8;
    public static final int FM_RX_SRCHLIST_MODE_WEAK = 3;
    public static final int FM_RX_SRCHLIST_MODE_WEAKEST = 9;
    public static final int FM_RX_SRCHRDS_MODE_SCAN_PTY = 5;
    public static final int FM_RX_SRCHRDS_MODE_SEEK_AF = 7;
    public static final int FM_RX_SRCHRDS_MODE_SEEK_PI = 6;
    public static final int FM_RX_SRCHRDS_MODE_SEEK_PTY = 4;
    public static final int FM_RX_SRCH_MODE_SCAN = 1;
    public static final int FM_RX_SRCH_MODE_SEEK = 0;
    public static final int FM_RX_UNMUTE = 0;
    static final int STD_BUF_SIZE = 128;
    private static final String TAG = "FMRadio";
    private static final int TAVARUA_BUF_AF_LIST = 5;
    private static final int TAVARUA_BUF_EVENTS = 1;
    private static final int TAVARUA_BUF_MAX = 6;
    private static final int TAVARUA_BUF_PS_RDS = 3;
    private static final int TAVARUA_BUF_RAW_RDS = 4;
    private static final int TAVARUA_BUF_RT_RDS = 2;
    private static final int TAVARUA_BUF_SRCH_LIST = 0;
    private static final int V4L2_CID_PRIVATE_BASE = 134217728;
    private static final int V4L2_CID_PRIVATE_TAVARUA_ANTENNA = 134217746;
    private static final int V4L2_CID_PRIVATE_TAVARUA_SIGNAL_TH = 134217736;
    public static int mSearchState = 0;


    /**
     * BUF_TYPE
     */
    private static final int BUF_ERT = 12;
    private static final int BUF_RTPLUS = 11;

    private static final int LEN_IND = 0;
    private static final int RT_OR_ERT_IND = 1;
    private static final int ENCODE_TYPE_IND = 1;
    private static final int ERT_DIR_IND = 2;
    /**
     * Search Algo type
     */
    private static final int SEARCH_MPXDCC = 0;
    private static final int SEARCH_SINR_INT = 1;

    private FmRxEvCallbacksAdaptor mCallback;


    /**
     * Constructor for the receiver Object
     */
    public FmReceiver(){
        mControl = new FmRxControls();
        mRdsData = new FmRxRdsData (sFd);
        mRxEvents = new FmRxEventListner();
    }

    /**
     *    Constructor for the receiver Object that takes path to
     *    radio and event callbacks.
     *    <p>
     *    @param devicePath FM Device path String.
     *    @param callback the callbacks to handle the events
     *                               events from the FM receiver.
     *
     */
    public FmReceiver(String devicePath,
                      hisi.fmradio.FmRxEvCallbacksAdaptor callback) throws InstantiationException {
        mControl = new hisi.fmradio.FmRxControls();
        mRxEvents = new hisi.fmradio.FmRxEventListner();

        Log.e(TAG, "FmReceiver constructor");

        //registerClient(callback);
        mCallback = callback;
        // TODO iceows
        if (isCherokeeChip()) {
            //FmReceiverJNI mFmReceiverJNI = new FmReceiverJNI(mCallback);
        }
    }


    // TODO Iceows
    public static boolean isCherokeeChip() {
        return false;
    }

    @Override // android.os.FmTransceiver
    public boolean registerClient(FmRxEvCallbacks callback) {
        boolean status = super.registerClient(callback);
        return status;
    }

    @Override // android.os.FmTransceiver
    public boolean unregisterClient() {
        boolean status = super.unregisterClient();
        return status;
    }

    // TODO Iceows
    public boolean enable(FmConfig configSettings) {
        boolean status = super.enable(configSettings, 1);
        if (status) {
            boolean status2 = registerClient(this.mCallback);
            this.mRdsData = new FmRxRdsData(sFd);
            return status2;
        }
        return false;
    }

    public boolean reset() {
        int state = getFMState();
        if (state == 0) {
            Log.d(TAG, "FM already turned Off.");
            return false;
        }
        setFMPowerState(0);
        Log.v(TAG, "reset: NEW-STATE : FMState_Turned_Off");
        boolean status = unregisterClient();
        release("/dev/radio0");
        return status;
    }

    // TODO Iceows
    @Override // hisi.fmradio.FmTransceiver
    public boolean disable(){
        unregisterClient();
        super.disable();
        return true;
    }

    public boolean searchStations(int mode, int dwellPeriod, int direction) {
        boolean bStatus = true;
        Log.d(TAG, "Basic search...");
        if (mode != 0 && mode != 1) {
            Log.d(TAG, "Invalid search mode: " + mode);
            bStatus = false;
        }
        if (dwellPeriod < 1 || dwellPeriod > 7) {
            Log.d(TAG, "Invalid dwelling time: " + dwellPeriod);
            bStatus = false;
        }
        if (direction != 0 && direction != 1) {
            Log.d(TAG, "Invalid search direction: " + direction);
            bStatus = false;
        }
        if (bStatus) {
            Log.d(TAG, "searchStations: mode " + mode + "direction:  " + direction);
            this.mControl.searchStations(sFd, mode, dwellPeriod, direction, 0, 0);
        }
        return true;
    }

    public boolean searchStations(int mode, int dwellPeriod, int direction, int pty, int pi) {
        boolean bStatus = true;
        Log.d(TAG, "RDS search...");
        if (mode != 4 && mode != 5 && mode != 6 && mode != 7) {
            Log.d(TAG, "Invalid search mode: " + mode);
            bStatus = false;
        }
        if (dwellPeriod < 1 || dwellPeriod > 7) {
            Log.d(TAG, "Invalid dwelling time: " + dwellPeriod);
            bStatus = false;
        }
        if (direction != 0 && direction != 1) {
            Log.d(TAG, "Invalid search direction: " + direction);
            bStatus = false;
        }
        if (bStatus) {
            Log.d(TAG, "searchStations: mode " + mode);
            Log.d(TAG, "searchStations: dwellPeriod " + dwellPeriod);
            Log.d(TAG, "searchStations: direction " + direction);
            Log.d(TAG, "searchStations: pty " + pty);
            Log.d(TAG, "searchStations: pi " + pi);
            this.mControl.searchStations(sFd, mode, dwellPeriod, direction, pty, pi);
        }
        return true;
    }

    public boolean searchStationList(int mode, int direction, int maximumStations, int pty) {
        boolean bStatus = true;
        int re = 0;
        Log.d(TAG, "searchStations: mode " + mode);
        Log.d(TAG, "searchStations: direction " + direction);
        Log.d(TAG, "searchStations: maximumStations " + maximumStations);
        Log.d(TAG, "searchStations: pty " + pty);
        if (mode != 2 && mode != 3 && mode != 8 && mode != 9) {
            bStatus = false;
        }
        bStatus = (maximumStations < 0 || maximumStations > 12) ? false : false;
        if (direction != 0 && direction != 1) {
            bStatus = false;
        }
        if (bStatus) {
            if (mode == 8 || mode == 9) {
                re = this.mControl.searchStationList(sFd, mode, 0, direction, pty);
            } else {
                re = this.mControl.searchStationList(sFd, mode, maximumStations, direction, pty);
            }
        }
        if (re == 0) {
            return true;
        }
        return false;
    }

    public boolean cancelSearch() {
        this.mControl.cancelSearch(sFd);
        return true;
    }

    public boolean setMuteMode(int mode) {
        switch (mode) {
            case 0:
                this.mControl.muteControl(sFd, false);
                break;
            case 1:
                this.mControl.muteControl(sFd, true);
                break;
        }
        return true;
    }

    public boolean setStereoMode(boolean stereoEnable) {
        int re = this.mControl.stereoControl(sFd, stereoEnable);
        return re == 0;
    }

    public boolean setSignalThreshold(int threshold) {
        int rssiLev;
        Log.d(TAG, "Signal Threshhold input: " + threshold);
        switch (threshold) {
            case 0:
                rssiLev = FM_RX_RSSI_LEVEL_VERY_WEAK;
                break;
            case 1:
                rssiLev = FM_RX_RSSI_LEVEL_WEAK;
                break;
            case 2:
                rssiLev = FM_RX_RSSI_LEVEL_STRONG;
                break;
            case 3:
                rssiLev = FM_RX_RSSI_LEVEL_VERY_STRONG;
                break;
            default:
                Log.d(TAG, "Invalid threshold: " + threshold);
                return false;
        }
        int re = FmReceiverWrapper.setControlNative(sFd, V4L2_CID_PRIVATE_TAVARUA_SIGNAL_TH, rssiLev);
        if (re == 0) {
            return true;
        }
        return false;
    }

    public int getTunedFrequency() {
        int frequency = FmReceiverWrapper.getFreqNative(sFd);
        Log.d(TAG, "getFrequency: " + frequency);
        return frequency;
    }

    public FmRxRdsData getPSInfo() {
        byte[] buff = new byte[STD_BUF_SIZE];
        FmReceiverWrapper.getBufferNative(sFd, buff, 3);
        int piLower = buff[3] & 255;
        int piHigher = buff[2] & 255;
        int pi = (piHigher << 8) | piLower;
        this.mRdsData.setPrgmId(pi);
        this.mRdsData.setPrgmType(buff[1] & 31);
        int numOfPs = buff[0] & 15;
        try {
            String rdsStr = new String(buff, 5, numOfPs * 8, Charset.forName("UTF-8"));
            this.mRdsData.setPrgmServices(rdsStr);
        } catch (StringIndexOutOfBoundsException e) {
            Log.d(TAG, "Number of PS names " + numOfPs);
        }
        return this.mRdsData;
    }

    public FmRxRdsData getRTInfo() {
        byte[] buff = new byte[STD_BUF_SIZE];
        FmReceiverWrapper.getBufferNative(sFd, buff, 2);
        String rdsStr = new String(buff, Charset.forName("UTF-8"));
        int piLower = buff[3] & 255;
        int piHigher = buff[2] & 255;
        int pi = (piHigher << 8) | piLower;
        this.mRdsData.setPrgmId(pi);
        this.mRdsData.setPrgmType(buff[1] & 31);
        try {
            this.mRdsData.setRadioText(rdsStr.substring(5, buff[0] + 5));
        } catch (StringIndexOutOfBoundsException e) {
            Log.d(TAG, "StringIndexOutOfBoundsException ...");
        }
        return this.mRdsData;
    }

    public int[] getAFInfo() {
        byte[] buff = new byte[STD_BUF_SIZE];
        int[] AfList = new int[40];
        FmReceiverWrapper.getBufferNative(sFd, buff, 5);
        if (buff[4] <= 0 || buff[4] > 25) {
            return null;
        }
        int lowerBand = FmReceiverWrapper.getLowerBandNative(sFd);
        Log.d(TAG, "Low band " + lowerBand);
        Log.d(TAG, "AF_buff 0: " + (buff[0] & 255));
        Log.d(TAG, "AF_buff 1: " + (buff[1] & 255));
        Log.d(TAG, "AF_buff 2: " + (buff[2] & 255));
        Log.d(TAG, "AF_buff 3: " + (buff[3] & 255));
        Log.d(TAG, "AF_buff 4: " + (buff[4] & 255));
        for (int i = 0; i < buff[4]; i++) {
            AfList[i] = ((buff[i + 4] & 255) * 1000) + lowerBand;
            Log.d(TAG, "AF : " + AfList[i]);
        }
        return AfList;
    }

    public boolean setPowerMode(int powerMode) {
        int re;
        if (powerMode == 1) {
            re = this.mControl.setLowPwrMode(sFd, true);
        } else {
            re = this.mControl.setLowPwrMode(sFd, false);
        }
        return re == 0;
    }

    public int getPowerMode() {
        return this.mControl.getPwrMode(sFd);
    }

    public int[] getRssiLimit() {
        int[] rssiLimits = {0, 100};
        return rssiLimits;
    }

    public int getSignalThreshold() {
        int signalStrength;
        int rmssiThreshold = FmReceiverWrapper.getControlNative(sFd, V4L2_CID_PRIVATE_TAVARUA_SIGNAL_TH);
        Log.d(TAG, "Signal Threshhold: " + rmssiThreshold);
        if (FM_RX_RSSI_LEVEL_VERY_WEAK < rmssiThreshold && rmssiThreshold <= FM_RX_RSSI_LEVEL_WEAK) {
            signalStrength = FM_RX_RSSI_LEVEL_WEAK;
        } else if (FM_RX_RSSI_LEVEL_WEAK < rmssiThreshold && rmssiThreshold <= FM_RX_RSSI_LEVEL_STRONG) {
            signalStrength = FM_RX_RSSI_LEVEL_STRONG;
        } else if (FM_RX_RSSI_LEVEL_STRONG < rmssiThreshold) {
            signalStrength = FM_RX_RSSI_LEVEL_VERY_STRONG;
        } else {
            signalStrength = FM_RX_RSSI_LEVEL_VERY_WEAK;
        }
        switch (signalStrength) {
            case FM_RX_RSSI_LEVEL_VERY_WEAK /* -105 */:
                return 0;
            case FM_RX_RSSI_LEVEL_WEAK /* -100 */:
                return 1;
            case FM_RX_RSSI_LEVEL_STRONG /* -96 */:
                return 2;
            case FM_RX_RSSI_LEVEL_VERY_STRONG /* -90 */:
                return 3;
            default:
                return 0;
        }
    }

    public boolean setRdsGroupOptions(int enRdsGrpsMask, int rdsBuffSize, boolean enRdsChangeFilter) {
        int re = this.mRdsData.rdsOn(true);
        if (re != 0) {
            return false;
        }
        int re2 = this.mRdsData.rdsGrpOptions(enRdsGrpsMask, rdsBuffSize, enRdsChangeFilter);
        return re2 == 0;
    }

    public boolean registerRdsGroupProcessing(int fmGrpsToProc) {
        int re = this.mRdsData.rdsOn(true);
        if (re != 0) {
            return false;
        }
        int re2 = this.mRdsData.rdsOptions(fmGrpsToProc);
        return re2 == 0;
    }

    public boolean enableAFjump(boolean enable) {
        int re = this.mRdsData.rdsOn(true);
        if (re != 0) {
            return false;
        }
        this.mRdsData.enableAFjump(enable);
        return true;
    }

    public int[] getStationList() {
        return this.mControl.stationList(sFd);
    }

    public int getRssi() {
        int rssi = FmReceiverWrapper.getRSSINative(sFd);
        return rssi;
    }

    public boolean getInternalAntenna() {
        int re = FmReceiverWrapper.getControlNative(sFd, V4L2_CID_PRIVATE_TAVARUA_ANTENNA);
        return re == 1;
    }

    public boolean setInternalAntenna(boolean intAnt) {
        int iAntenna;
        if (intAnt) {
            iAntenna = 1;
        } else {
            iAntenna = 0;
        }
        int re = FmReceiverWrapper.setControlNative(sFd, V4L2_CID_PRIVATE_TAVARUA_ANTENNA, iAntenna);
        return re == 0;
    }

    public byte[] getRawRDS(int numBlocks) {
        if (numBlocks <= 0) {
            return null;
        }
        byte[] rawRds = new byte[numBlocks * 3];
        int re = FmReceiverWrapper.getRawRdsNative(sFd, rawRds, numBlocks * 3);
        if (re == numBlocks * 3) {
            return rawRds;
        }
        if (re <= 0) {
            return null;
        }
        byte[] buff = new byte[re];
        System.arraycopy(rawRds, 0, buff, 0, re);
        return buff;
    }

    public int getFMState() {
        int currFMState = FmTransceiver.getFMPowerState();
        return currFMState;
    }

    public int getAudioQuilty(int value) {
        int ret = FmReceiverWrapper.getAudioQuiltyNative(sFd, value);
        return ret;
    }

    public int setFmSnrThresh(int value) {
        int ret = FmReceiverWrapper.setFmSnrThreshNative(sFd, value);
        return ret;
    }

    public int setFmRssiThresh(int value) {
        int ret = FmReceiverWrapper.setFmRssiThreshNative(sFd, value);
        return ret;
    }

    public void setFmDeviceConnectionState(int state) {
        FmReceiverWrapper.setFmDeviceConnectionState(state);
    }

    /*****************************************************************
     *
     * All this functions after don't exist in EMUI8 framework
     * TODO Iceows
     *
     *  */
    public void EnableSlimbus(int enable) {
        Log.d(TAG, "EnableSlimbus :enable =" + enable);
        mControl.enableSlimbus(sFd, enable);
    }
    public boolean setRawRdsGrpMask()
    {
        return false;
    }

    public FmRxRdsData getECCInfo() {
        byte [] raw_ecc = new byte[STD_BUF_SIZE];
        int ecc_code =0;
        int bytes_read;

        raw_ecc = FmReceiverJNI.getPsBuffer(raw_ecc);
        bytes_read = raw_ecc[0];
        Log.d (TAG, "bytes_read = " + bytes_read);
        if (bytes_read > 0) {
            ecc_code =  raw_ecc[9] & 0xFF;
            mRdsData.setECountryCode(ecc_code);
            Log.d(TAG, "ECC code: " + ecc_code );
        }
        return mRdsData;
    }
    public FmRxRdsData getERTInfo() {
        byte [] raw_ert = new byte[STD_BUF_SIZE];
        byte [] ert_text;
        int i;
        String s = "";
        String encoding_type = "UCS-2";
        int bytes_read;

        if(isCherokeeChip())
        {
            raw_ert = FmReceiverJNI.getPsBuffer(raw_ert);
        }
        else
        {
            bytes_read = FmReceiverJNI.getBufferNative(sFd, raw_ert, BUF_ERT);
        }
        bytes_read = raw_ert[0];
        if (bytes_read > 0) {
            ert_text = new byte[raw_ert[LEN_IND]];
            for(i = 3; (i - 3) < raw_ert[LEN_IND]; i++) {
                ert_text[i - 3] = raw_ert[i];
            }
            if (raw_ert[ENCODE_TYPE_IND] == 1)
                encoding_type = "UTF-8";
            try {
                s = new String (ert_text, encoding_type);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mRdsData.setERadioText(s);
            if (raw_ert[ERT_DIR_IND] == 0)
                mRdsData.setFormatDir(false);
            else
                mRdsData.setFormatDir(true);
            Log.d(TAG, "eRT: " + s + "dir: " +raw_ert[ERT_DIR_IND]);
        }
        return mRdsData;
    }


    public boolean setPSRxRepeatCount(int count) {
        int state = getFMState();
        /* Check current state of FM device */
        if (state == FMState_Turned_Off){
            Log.d(TAG, "setRxRepeatcount failed");
            return false;
        }
        return mControl.setPSRxRepeatCount(sFd, count);
    }

    public boolean getPSRxRepeatCount() {
        int state = getFMState();
        /* Check current state of FM device */
        if (state == FMState_Turned_Off){
            Log.d(TAG, "setRxRepeatcount failed");
            return false;
        }
        return mControl.getPSRxRepeatCount(sFd);
    }

    public byte getBlendSinr() {
        int state = getFMState();
        if ((state == FMState_Turned_Off) || (state == FMState_Srch_InProg)) {
            Log.d(TAG, "getBlendSinr: Device currently busy in executing another command.");
            return Byte.MAX_VALUE;
        }
        return mControl.getBlendSinr(sFd);
    }

    public boolean setBlendSinr(int sinrHi) {
        int state = getFMState();
        if ((state == FMState_Turned_Off) || (state == FMState_Srch_InProg)) {
            Log.d(TAG, "setBlendSinr: Device currently busy in executing another command.");
            return false;
        }
        return mControl.setBlendSinr(sFd, sinrHi);
    }

    public byte getBlendRmssi() {
        int state = getFMState();
        if ((state == FMState_Turned_Off) || (state == FMState_Srch_InProg)) {
            Log.d(TAG, "getBlendRmssi: Device currently busy in executing another command.");
            return Byte.MAX_VALUE;
        }
        return mControl.getBlendRmssi(sFd);
    }

    public boolean setBlendRmssi(int rmssiHi) {
        int state = getFMState();
        if ((state == FMState_Turned_Off) || (state == FMState_Srch_InProg)) {
            Log.d(TAG, "setBlendRmssi: Device currently busy in executing another command.");
            return false;
        }
        return mControl.setBlendRmssi(sFd, rmssiHi);
    }

/*===============================================================
   FUNCTION:  getSINR
   ==============================================================*/
    /**
     *    Gets the SINR value of currently tuned station
     *
     *    <p>
     *    This method gets the SINR value for  currently tuned station.
     *
     *    <p>
     */
    public int getSINR()
    {
        int re =  mControl.getSINR(sFd);
        Log.d (TAG, "The value of SINR is " + re);
        return re;
    }

     /*==============================================================
   FUNCTION:  getIoverc
   ==============================================================*/
    /**
     *    Returns the Estimated Interference Over Carrier of the currently tuned station
     *
     *    <p>
     *    This method returns the Estimated Interference Over Carrier of the currently
     *    tuned station.
     *
     *    <p>
     *    @return    IOVERC of currently tuned station on Success.
     *           -1 on failure to retrieve the current IoverC.
     */
    public int getIoverc()
    {
        int re;
        re = mControl.IovercControl(sFd);
        return re;
    }

   /*==============================================================
   FUNCTION:  getIntDet
   ==============================================================*/
    /**
     *    Returns the IntDet the currently tuned station
     *
     *    <p>
     *    This method returns the IntDet of the currently
     *    tuned station.
     *
     *    <p>
     *    @return    IntDet of currently tuned station.
     *           -1 on failure to retrieve the current IntDet
     */
    public int getIntDet()
    {
        int re;

        re = mControl.IntDet(sFd);
        return re;
    }


   /*==============================================================
   FUNCTION:  getMpxDcc
   ==============================================================*/
    /**
     *    Returns the MPX_DCC of the currently tuned station
     *
     *    <p>
     *    This method returns the MPX_DCC of the currently
     *    tuned station.
     *
     *    <p>
     *    @return    MPX_DCC value of currently tuned station.
     *               -1 on failure to retrieve the current MPX_DCC
     */
    public int getMpxDcc()
    {
        int re;

        re = mControl.Mpx_Dcc(sFd);
        return re;
    }

/*==============================================================
   FUNCTION:  setHiLoInj
   ==============================================================*/
    /**
     *    Sets the Hi-Lo injection
     *
     *    <p>
     *    This method sets the hi-low injection.
     *
     *    <p>
     */
    public void setHiLoInj(int inj)
    {
        int re =  mControl.setHiLoInj(sFd, inj);
    }

/*==============================================================
   FUNCTION:  setSINRsamples
   ==============================================================*/
    /**
     *    Sets the SINR samples
     *
     *    <p>
     *    This method sets the number of SINR samples to calculate the SINR value.
     *
     *    <p>
     */
    public boolean setSINRsamples(int data)
    {
        int re =  mControl.setSINRsamples(sFd, data);
        if (re < 0)
            return false;
        else
            return true;
    }

/*==============================================================
   FUNCTION:  getSINRsamples
   ==============================================================*/
    /**
     *    Gets the SINR samples value
     *
     *    <p>
     *    This method gets the number of currently set SINR samples.
     *
     *    <p>
     */
    public int getSINRsamples()
    {
        return mControl.getSINRsamples(sFd);
    }


/*==============================================================
   FUNCTION:  setSINRThreshold
   ==============================================================*/
    /**
     *    Sets the SINR threshold value
     *
     *    <p>
     *    This method sets the SINR threshold value.
     *
     *    <p>
     */
    public boolean setSINRThreshold(int data)
    {
        int re =  mControl.setSINRThreshold(sFd, data);
        if (re < 0)
            return false;
        else
            return true;
    }

/*==============================================================
   FUNCTION:  getSINRThreshold
   ==============================================================*/
    /**
     *    Gets the SINR threshold value
     *
     *    <p>
     *    This method gets the currently set SINR threshold value.
     *
     *    <p>
     */
    public int getSINRThreshold()
    {
        return mControl.getSINRThreshold(sFd);
    }


/*==============================================================
   FUNCTION:  setOnChannelThreshold
   ==============================================================*/
    /**
     *    Sets the On channel threshold value
     *
     *    <p>
     *    This method sets the On channel threshold value.
     *
     *    <p>
     */
    public boolean setOnChannelThreshold(int data)
    {
        int re =  mControl.setOnChannelThreshold(sFd, data);
        if (re < 0)
            return false;
        else
            return true;
    }

/*==============================================================
   FUNCTION:  getOnChannelThreshold
   ==============================================================*/
    /**
     *    Gets the On channel threshold value
     *
     *    <p>
     *    This method gets the currently set On channel threshold value.
     *
     *    <p>
     */
    public boolean getOnChannelThreshold()
    {
        int re = mControl.getOnChannelThreshold(sFd);

        if (re != 0)
            return false;
        else
            return true;
    }


/*==============================================================
   FUNCTION:  setOffChannelThreshold
   ==============================================================*/
    /**
     *    Sets the Off channel threshold value
     *
     *    <p>
     *    This method sets the Off channel threshold value.
     *
     *    <p>
     */
    public boolean setOffChannelThreshold(int data)
    {
        int re =  mControl.setOffChannelThreshold(sFd, data);
        if (re < 0)
            return false;
        else
            return true;
    }
/*==============================================================
   FUNCTION:  getOffChannelThreshold
   ==============================================================*/
    /**
     *    Gets the Off channel threshold value
     *
     *    <p>
     *    This method gets the currently set Off channel threshold value.
     *
     *    <p>
     */
    public boolean  getOffChannelThreshold()
    {
        int re = mControl.getOffChannelThreshold(sFd);

        if (re != 0)
            return false;
        else
            return true;
    }


    public int getSearchAlgoType() {
        int state = getFMState();
        if ((state == FMState_Turned_Off) || (state == FMState_Srch_InProg)) {
            Log.d(TAG, "getSearchAlgoType: Device currently busy in executing another command.");
            return Integer.MAX_VALUE;
        }
        return mControl.getSearchAlgoType(sFd);
    }

    public boolean setSearchAlgoType(int searchType) {
        int state = getFMState();
        if ((state == FMState_Turned_Off) || (state == FMState_Srch_InProg)) {
            Log.d(TAG, "setSearchAlgoType: Device currently busy in executing another command.");
            return false;
        }
        if((searchType != SEARCH_MPXDCC) && (searchType != SEARCH_SINR_INT)) {
            Log.d(TAG, "Search Algo is invalid");
            return false;
        }else {
            return mControl.setSearchAlgoType(sFd, searchType);
        }
    }


    public int getSinrFirstStage() {
        int state = getFMState();
        if ((state == FMState_Turned_Off) || (state == FMState_Srch_InProg)) {
            Log.d(TAG, "getSinrFirstStage: Device currently busy in executing another command.");
            return Integer.MAX_VALUE;
        }
        return mControl.getSinrFirstStage(sFd);
    }

    public boolean setSinrFirstStage(int sinr) {
        int state = getFMState();
        if ((state == FMState_Turned_Off) || (state == FMState_Srch_InProg)) {
            Log.d(TAG, "setSinrFirstStage: Device currently busy in executing another command.");
            return false;
        }
        return mControl.setSinrFirstStage(sFd, sinr);
    }


    public int getRmssiFirstStage() {
        int state = getFMState();
        if ((state == FMState_Turned_Off) || (state == FMState_Srch_InProg)) {
            Log.d(TAG, "getRmssiFirstStage: Device currently busy in executing another command.");
            return Integer.MAX_VALUE;
        }
        return mControl.getRmssiFirstStage(sFd);
    }

    public boolean setRmssiFirstStage(int rmssi) {
        int state = getFMState();
        if ((state == FMState_Turned_Off) || (state == FMState_Srch_InProg)) {
            Log.d(TAG, "setRmssiFirstStage: Device currently busy in executing another command.");
            return false;
        }
        return mControl.setRmssiFirstStage(sFd, rmssi);
    }

    public int getCFOMeanTh() {
        int state = getFMState();
        if ((state == FMState_Turned_Off) || (state == FMState_Srch_InProg)) {
            Log.d(TAG, "getCF0Th12: Device currently busy in executing another command.");
            return Integer.MAX_VALUE;
        }
        return mControl.getCFOMeanTh(sFd);
    }

    public boolean setCFOMeanTh(int th) {
        int state = getFMState();
        if ((state == FMState_Turned_Off) || (state == FMState_Srch_InProg)) {
            Log.d(TAG, "setRmssiFirstStage: Device currently busy in executing another command.");
            return false;
        }
        return mControl.setCFOMeanTh(sFd, th);
    }


    public boolean setAFJumpRmssiTh(int th) {
        int state = getFMState();
        /* Check current state of FM device */
        if ((state == FMState_Turned_Off) || (state == FMState_Srch_InProg)) {
            Log.d(TAG, "setAFJumpThreshold: Device currently busy in executing another command.");
            return false;
        }
        return mControl.setAFJumpRmssiTh(sFd, th);
    }

    public int getAFJumpRmssiSamples() {
        int state = getFMState();
        /* Check current state of FM device */
        if ((state == FMState_Turned_Off) || (state == FMState_Srch_InProg)) {
            Log.d(TAG, "getAFJumpRmssiSamples: Device currently busy in executing another command.");
            return ERROR;
        }
        return mControl.getAFJumpRmssiSamples(sFd);
    }

    public int getAFJumpRmssiTh() {
        int state = getFMState();
        /* Check current state of FM device */
        if ((state == FMState_Turned_Off) || (state == FMState_Srch_InProg)) {
            Log.d(TAG, "getAFJumpThreshold: Device currently busy in executing another command.");
            return ERROR;
        }
        return mControl.getAFJumpRmssiTh(sFd);
    }


    public int getGdChRmssiTh() {
        int state = getFMState();
        /* Check current state of FM device */
        if ((state == FMState_Turned_Off) || (state == FMState_Srch_InProg)) {
            Log.d(TAG, "getGdChRmssiTh: Device currently busy in executing another command.");
            return ERROR;
        }
        return mControl.getGdChRmssiTh(sFd);
    }

    public boolean setGdChRmssiTh(int th) {
        int state = getFMState();
        /* Check current state of FM device */
        if ((state == FMState_Turned_Off) || (state == FMState_Srch_InProg)) {
            Log.d(TAG, "setGdChRmssiTh: Device currently busy in executing another command.");
            return false;
        }
        return mControl.setGdChRmssiTh(sFd, th);
    }

    public boolean setAFJumpRmssiSamples(int samples) {
        int state = getFMState();
        /* Check current state of FM device */
        if ((state == FMState_Turned_Off) || (state == FMState_Srch_InProg)) {
            Log.d(TAG, "setAFJumpRmssiSamples: Device currently busy in executing another command.");
            return false;
        }
        return mControl.setAFJumpRmssiSamples(sFd, samples);
    }


/*==============================================================
   FUNCTION:  setRssiThreshold
   ==============================================================*/
    /**
     *    Sets the RSSI threshold value
     *
     *    <p>
     *    This method sets the RSSI threshold value.
     *
     *    <p>
     */
    public boolean setRssiThreshold(int data)
    {
        int re =  mControl.setRssiThreshold(sFd, data);
        if (re < 0)
            return false;
        else
            return true;
    }

/*==============================================================
   FUNCTION:  getRssiThreshold
   ==============================================================*/
    /**
     *    Gets the Rssi threshold value
     *
     *    <p>
     *    This method gets the currently set Rssi threshold value.
     *
     *    <p>
     */
    public int getRssiThreshold()
    {
        return mControl.getRssiThreshold(sFd);
    }



/*==============================================================
   FUNCTION:  setAfJumpRssiThreshold
   ==============================================================*/
    /**
     *    Sets the Af jump RSSI threshold value
     *
     *    <p>
     *    This method sets the AF jump RSSI threshold value.
     *
     *    <p>
     */
    public boolean setAfJumpRssiThreshold(int data)
    {
        int re =  mControl.setAfJumpRssiThreshold(sFd, data);
        if (re < 0)
            return false;
        else
            return true;
    }

/*==============================================================
   FUNCTION:  getAfJumpRssiThreshold
   ==============================================================*/
    /**
     *    Gets the Af jump RSSI threshold value
     *
     *    <p>
     *    This method gets the currently set AF jump RSSI threshold value.
     *
     *    <p>
     */
    public int getAfJumpRssiThreshold()
    {
        return mControl.getAfJumpRssiThreshold(sFd);
    }


/*==============================================================
   FUNCTION: setRdsFifoCnt
   ==============================================================*/
    /**
     *    Sets the RDS FIFO count value
     *
     *    <p>
     *    This method sets the RDS FIFO count value.
     *
     *    <p>
     */
    public boolean setRdsFifoCnt(int data)
    {
        int re =  mControl.setRdsFifoCnt(sFd, data);
        if (re < 0)
            return false;
        else
            return true;
    }

/*==============================================================
   FUNCTION:  getRdsFifoCnt
   ==============================================================*/
    /**
     *    Gets the RDS FIFO count value
     *
     *    <p>
     *    This method gets the currently set RDS FIFO count value.
     *
     *    <p>
     */
    public int getRdsFifoCnt()
    {
        return mControl.getRdsFifoCnt(sFd);
    }

    public FmRxRdsData getRTPlusInfo() {
        byte []rt_plus = new byte[STD_BUF_SIZE];
        int bytes_read;
        String rt = "";
        int rt_len;
        int i, count, avail_tag_num = 0;
        byte tag_code, tag_len, tag_start_pos;
        if (isCherokeeChip()) {
            rt_plus = FmReceiverJNI.getPsBuffer(rt_plus);
        }
        else
        {
            bytes_read = FmReceiverJNI.getBufferNative(sFd, rt_plus, BUF_RTPLUS);
        }
        bytes_read = rt_plus[0];
        if (bytes_read > 0) {
            if (rt_plus[RT_OR_ERT_IND] == 0)
                rt = mRdsData.getRadioText();
            else
                rt = mRdsData.getERadioText();
            if ((rt != "") && (rt != null)) {
                rt_len = rt.length();
                mRdsData.setTagNums(0);
                avail_tag_num = (rt_plus[LEN_IND] - 2)/3;
                if (avail_tag_num > 2) {
                    avail_tag_num = 2;
                }
                count = 1;
                for (i = 0; i < avail_tag_num; i++) {
                    tag_code = rt_plus[2+3*i];
                    tag_start_pos = rt_plus[3+3*i];
                    tag_len = rt_plus[4+3*i];
                    if (((tag_len + tag_start_pos) <= rt_len) && (tag_code > 0)) {
                        mRdsData.setTagValue(rt.substring(tag_start_pos,
                                (tag_len + tag_start_pos)), count);
                        mRdsData.setTagCode(tag_code, count);
                        count++;
                    }
                }
            } else {
                mRdsData.setTagNums(0);
            }
        } else {
            mRdsData.setTagNums(0);
        }
        return mRdsData;
    }

}
