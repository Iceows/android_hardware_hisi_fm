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

public class FmRxEvCallbacksAdaptor implements FmRxEvCallbacks {
    @Override // android.os.FmRxEvCallbacks
    public void FmRxEvEnableReceiver() {
    }

    @Override // android.os.FmRxEvCallbacks
    public void FmRxEvDisableReceiver() {
    }

    @Override // android.os.FmRxEvCallbacks
    public void FmRxEvRadioReset() {
    }

    @Override // android.os.FmRxEvCallbacks
    public void FmRxEvRadioTuneStatus(int freq) {
    }

    @Override // android.os.FmRxEvCallbacks
    public void FmRxEvRdsLockStatus(boolean rdsAvail) {
    }

    @Override // android.os.FmRxEvCallbacks
    public void FmRxEvStereoStatus(boolean stereo) {
    }

    @Override // android.os.FmRxEvCallbacks
    public void FmRxEvServiceAvailable(boolean service) {
    }

    @Override // android.os.FmRxEvCallbacks
    public void FmRxEvSearchInProgress() {
    }

    @Override // android.os.FmRxEvCallbacks
    public void FmRxEvSearchComplete(int freq) {
    }

    @Override // android.os.FmRxEvCallbacks
    public void FmRxEvSearchListComplete() {
    }

    @Override // android.os.FmRxEvCallbacks
    public void FmRxEvRdsGroupData() {
    }

    @Override // android.os.FmRxEvCallbacks
    public void FmRxEvRdsPsInfo() {
    }

    @Override // android.os.FmRxEvCallbacks
    public void FmRxEvRdsRtInfo() {
    }

    @Override // android.os.FmRxEvCallbacks
    public void FmRxEvRdsAfInfo() {
    }

    @Override // android.os.FmRxEvCallbacks
    public void FmRxEvSignalUpdate() {
    }
}