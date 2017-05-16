package com.example.gio.bigproject.data;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Copyright by Gio.
 * Created on 5/16/2017.
 */

public class CarriagePolyline {
    private static final ArrayList<LatLng> mCarriagePoly = new ArrayList<>();

    public static ArrayList<LatLng> getCarriagePoly1() {
        if (mCarriagePoly.size() > 0) {
            mCarriagePoly.clear();
        }

        mCarriagePoly.add(new LatLng(16.056533, 108.172545));
        mCarriagePoly.addAll(decodePoly("}~~`B_nfsSUSGA{D~@wCyDqAqBmBeDq@sA{@uB[[i@cAoAwBw@kA{AqByGgJ}CsEcEkGOSIYc@aAoA}DUq@[mAW_BKcCq@wSSuGCwBReHkDCoCBeBAM@?E?Dq@De@BmBPiCPyF`@s@?iEc@iAIL{EH_CJwDBmHIkD[eF[kDa@sDg@qCsAgGu@sCaA_DoB{EmAgC}AyCwAaCi@u@}BuCuAmB{@}@{B_CoAiAoDwCcEwCp@}@j@o@d@Ol@E`AOd@AdBQrDa@zAOpAS|AQ|AOtAU`Fo@lI_Ar@A|AOpCa@rC]YyCc@eDhHoApEw@lASe@gDk@aEW{BKy@[uBk@mFAsCAuPn@FjEPdNp@xEHlI`@"));
        // Add manually from 270 Tran Phu to 16.054026, 108.220274
        mCarriagePoly.add(new LatLng(16.060653, 108.223184));
        mCarriagePoly.add(new LatLng(16.060416, 108.223028));
        mCarriagePoly.add(new LatLng(16.060161, 108.222736));
        mCarriagePoly.add(new LatLng(16.059947, 108.222387));
        mCarriagePoly.add(new LatLng(16.059295, 108.221566));
        mCarriagePoly.add(new LatLng(16.058914, 108.221243));
        mCarriagePoly.add(new LatLng(16.057008, 108.220342));
        mCarriagePoly.add(new LatLng(16.056711, 108.220286));
        mCarriagePoly.add(new LatLng(16.055599, 108.220274));
        mCarriagePoly.add(new LatLng(16.054719, 108.220137));
        mCarriagePoly.add(new LatLng(16.052901, 108.220416));

        mCarriagePoly.addAll(decodePoly("si~`BqxosShSkA`@?PADBF@NEDOCMCA@K@SIiDCc@DIDMA]GQKIGCK]W_JOyEi@kI_Ck]e@qG[aD[aBk@sAu@iA_A}@e@_@]UQSiDcC?GjGoAxMqCxGsAbB_@~AUbCg@hA[|ImD~DeB`G}BrEgBjBy@h@WhFgB~@[XIvAm@~@e@tDaBhQoHdLaFhLyE~N_GxG_CzEuBxEoB`@QlDwAzKyEvOiGnHyC|B}@lBy@~HyCrNmFjGeCnJ{DpCkAfE_BpBa@nBUzBMjCAvD@h@?bH?hDCzBClAFjH`AbEn@zQjCxWnDpEp@@@DBJBLChGz@nGx@lNnB`NfBxMlB|AP@BHBF@HC@Al@HtANzADlA@nJA`JAvMCtDBrMDbGArNBtF?fGB~GAtE@`KHFFFBPBPCFEBCl@C|BGjF?xW@x`@DdO@fQBrSHBHPLV?NKvBKbA@zCC~@EpAMfB_@lHiC`GwBJEHKtCiAb@[nBaBbBaBxKkLp@u@lAgAz@o@zBqA`EaCvGqDzI{EdLoGzEiCzAeA^a@^e@Ve@Ru@Fq@BcB@oANm@d@s@d@[b@SzAi@hBi@pCcApAe@~DuAlHeCnAc@"));
        // Add manually from 15.909203, 108.271388 to Tram xe buyt so 1
        mCarriagePoly.add(new LatLng(15.909203, 108.271388));
        mCarriagePoly.add(new LatLng(15.908383, 108.271953));
        mCarriagePoly.add(new LatLng(15.907053, 108.273179));
        mCarriagePoly.add(new LatLng(15.906841, 108.273522));
        mCarriagePoly.add(new LatLng(15.906438,108.274889));

        // Add from Tram xe buyt so 1  to 15.902029,108.285930
        mCarriagePoly.addAll(decodePoly("gva`BamzsStEwWrDcSdAkG^sB`@_ANU|@aAlCyAp@c@f@m@R[Rc@"));

        // Add manually from 15.901831, 108.286374  to Nga 4 Thanh Tin
        mCarriagePoly.add(new LatLng(15.901831, 108.286374));
        mCarriagePoly.add(new LatLng(15.899999,108.293203));
        // Add from Nga 4 Thanh Tin QN to bx Hoi An
        mCarriagePoly.addAll(decodePoly("{m``Bo_~sSDWDoAGs@{@qD[wAGg@Ay@BYV_AfDyHjKmV~No]rB_F`L{WrDuI`FqL`@q@b@_@zBiAz@i@VU~AaB`AoAj@}@jBaEnAaDxBeGu@]Js@"));
        return mCarriagePoly;
    }

    private static ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                try {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } catch (Exception ignored) {

                }
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }
}
