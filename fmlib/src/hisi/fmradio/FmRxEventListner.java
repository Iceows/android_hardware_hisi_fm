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

import hisi.fmradio.IFmEventCallback;

class FmRxEventListner {
    private static final String TAG = "FMRadio";
    FmRxEvCallbacks mCallback = null;

    private  hisi.fmradio.IFmEventCallback mEventCallback = new IFmEventCallback.Stub() {

        public void onEventCallback(int event, int param1, int param2) {
            switch (event) {
                case 0:
                    Log.d(FmRxEventListner.TAG, "Got READY_EVENT");
                    FmRxEventListner.this.mCallback.FmRxEvEnableReceiver();
                    return;
                case 1:
                    Log.d(FmRxEventListner.TAG, "Got TUNE_EVENT");
                    FmRxEventListner.this.mCallback.FmRxEvRadioTuneStatus(param1);
                    return;
                case 2:
                    Log.d(FmRxEventListner.TAG, "Got SEEK_COMPLETE_EVENT");
                    FmRxEventListner.this.mCallback.FmRxEvSearchComplete(param1);
                    return;
                case 3:
                    Log.d(FmRxEventListner.TAG, "Got SCAN_NEXT_EVENT");
                    FmRxEventListner.this.mCallback.FmRxEvSearchInProgress();
                    return;
                case 4:
                    Log.d(FmRxEventListner.TAG, "Got RAW_RDS_EVENT");
                    FmRxEventListner.this.mCallback.FmRxEvRdsGroupData();
                    return;
                case 5:
                    Log.d(FmRxEventListner.TAG, "Got RT_EVENT");
                    FmRxEventListner.this.mCallback.FmRxEvRdsRtInfo();
                    return;
                case 6:
                    Log.d(FmRxEventListner.TAG, "Got PS_EVENT");
                    FmRxEventListner.this.mCallback.FmRxEvRdsPsInfo();
                    return;
                case 7:
                    Log.d(FmRxEventListner.TAG, "Got ERROR_EVENT");
                    return;
                case FmReceiver.FM_RX_SRCHLIST_MODE_STRONGEST /* 8 */:
                    Log.d(FmRxEventListner.TAG, "Got BELOW_TH_EVENT");
                    FmRxEventListner.this.mCallback.FmRxEvServiceAvailable(false);
                    return;
                case FmReceiver.FM_RX_SRCHLIST_MODE_WEAKEST /* 9 */:
                    Log.d(FmRxEventListner.TAG, "Got ABOVE_TH_EVENT");
                    FmRxEventListner.this.mCallback.FmRxEvServiceAvailable(true);
                    return;
                case 10:
                    Log.d(FmRxEventListner.TAG, "Got STEREO_EVENT");
                    FmRxEventListner.this.mCallback.FmRxEvStereoStatus(true);
                    return;
                case 11:
                    Log.d(FmRxEventListner.TAG, "Got MONO_EVENT");
                    FmRxEventListner.this.mCallback.FmRxEvStereoStatus(false);
                    return;
                case FmReceiver.FM_RX_SRCHLIST_MAX_STATIONS /* 12 */:
                    Log.d(FmRxEventListner.TAG, "Got RDS_AVAL_EVENT");
                    FmRxEventListner.this.mCallback.FmRxEvRdsLockStatus(true);
                    return;
                case 13:
                    Log.d(FmRxEventListner.TAG, "Got RDS_NOT_AVAL_EVENT");
                    FmRxEventListner.this.mCallback.FmRxEvRdsLockStatus(false);
                    return;
                case 14:
                    Log.d(FmRxEventListner.TAG, "Got NEW_SRCH_LIST");
                    FmRxEventListner.this.mCallback.FmRxEvSearchListComplete();
                    return;
                case 15:
                    Log.d(FmRxEventListner.TAG, "Got NEW_AF_LIST");
                    FmRxEventListner.this.mCallback.FmRxEvRdsAfInfo();
                    return;
                case 16:
                    Log.d(FmRxEventListner.TAG, "Got SIGNAL_UPDATE_EVENT");
                    FmRxEventListner.this.mCallback.FmRxEvSignalUpdate();
                    return;
                default:
                    Log.d(FmRxEventListner.TAG, "Unknown event");
                    return;
            }
        }
    };

    private enum FmRxEvents {
        READY_EVENT,
        TUNE_EVENT,
        SEEK_COMPLETE_EVENT,
        SCAN_NEXT_EVENT,
        RAW_RDS_EVENT,
        RT_EVENT,
        PS_EVENT,
        ERROR_EVENT,
        BELOW_TH_EVENT,
        ABOVE_TH_EVENT,
        STEREO_EVENT,
        MONO_EVENT,
        RDS_AVAL_EVENT,
        RDS_NOT_AVAL_EVENT,
        TAVARUA_EVT_NEW_SRCH_LIST,
        TAVARUA_EVT_NEW_AF_LIST,
        SIGNAL_UPDATE_EVENT;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static FmRxEvents[] valuesCustom() {
            return values();
        }
    }

    public void startListner(int fd, FmRxEvCallbacks cb) {
        this.mCallback = cb;
        FmReceiverWrapper.startListner(fd, this.mEventCallback);
    }

    public void stopListener() {
        FmReceiverWrapper.stopListner();
    }
}