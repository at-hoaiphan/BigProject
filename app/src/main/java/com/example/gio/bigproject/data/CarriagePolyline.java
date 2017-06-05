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
        mCarriagePoly.addAll(decodePoly("me_aBykfsSDAOYwCyDqAqBmBeDq@sA{@uB[[i@cAoAwBsAkBwC_EeDuEqIeMk@}@OSIYc@aAq@sBs@{B[mAW_BKcC[aKi@kPCwBReHkDCo@?eE@M@?E?Dq@De@BmBPcKr@s@?iEc@iAIL{EH_CJwDBmHScG]aFq@kGg@qCu@kD]{Au@sCaA_Dw@oBuA_Do@sA}AyCwAaCi@u@}BuCuAmB{@}@{B_CkC_CoEiDgBoAp@}@j@o@d@Ol@E`AOd@AdBQ~Fo@NApAS|AQ|AOtAU`Fo@lI_Ar@A|AOpCa@rC]YyCc@eDhHoApEw@lASe@gDk@aEc@uD[uB_@oDK}@AeA?mAAuPn@FbCLbHZ"));
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

    public static ArrayList<LatLng> getCarriagePoly2() {
        if (mCarriagePoly.size() > 0) {
            mCarriagePoly.clear();
        }

        mCarriagePoly.add(new LatLng(16.135482, 108.119566));
        mCarriagePoly.addAll(decodePoly("ymnaBub|rS|GJrEJ|GJlB?~AFhHLpJF`AAj@Gf@In@U|@_@fGgD|D_CrCyAvByAdEoCxDwB|KoH|A{@^]b@a@ZONEn@IzA?l@Kt@WvAaAxDwC~BkBjEgD`BmAt@e@pAm@jAc@pAYrDiA"));
        mCarriagePoly.add(new LatLng(16.109421, 108.130187));
        mCarriagePoly.add(new LatLng(16.108989, 108.130436));
        // From 16.108989, 108.130436 to 16.056906, 108.167141
        mCarriagePoly.addAll(decodePoly("ghiaBkf~rSrCeB`DiBrAw@pDkBxGmCvQiH|LgFxGkCpDuApAk@~C_B~B{AfCaCtCeDhG{HvCiDt@u@tAaAl@WxBy@tLmDzEwAvIqCtGwBdA[xNgEfOyElDcA~FgBhO{EtXmI`F}AhBs@dAk@r@i@r@m@~@_Ap@aAfByCpBqDb@aApAuBbKgQrE{HpBoDbDmFp@mAhAiBhD}F|@uArAiB`BkBz@_A|AmB"));
        mCarriagePoly.add(new LatLng(16.056602, 108.167615));
        mCarriagePoly.addAll(decodePoly("m__aBitesSO|@e@~Ag@dARJFK`@}@^sAP_ATgBDY?g@CgAGaAKgAMo@Sy@i@yAw@}AkAeBgDsEqAqBmBeDiAcCc@eA[[uAgC_AyA{@kAoD{E}CmEsIiMe@s@]{@k@}Ag@aBc@qAg@}BO_Bq@kTScGGuCAq@FeCLmFJuLJwCTcO?sCU}IKeGC{AOJOBMAQKKOAQ@QHONIHAH?EcDIkEM{CIq@Ag@E{@Be@C_@KaA@?BA@A@G?ECECAA?@yAC_BQoD_@gEe@iEe@iESgBMqAw@sHc@aDi@yDqAaJc@{DK}@Oy@O_BWuBEo@C_M?oHCcD"));
        mCarriagePoly.add(new LatLng(16.068002, 108.225021));

        return  mCarriagePoly;
    }

    public static ArrayList<LatLng> getCarriagePoly3() {
        if (mCarriagePoly.size() > 0) {
            mCarriagePoly.clear();
        }

        mCarriagePoly.add(new LatLng(16.056535, 108.172550));
        // From 16.057307, 108.172317 to 16.066879, 108.188341
        mCarriagePoly.addAll(decodePoly("ge_aB{kfsSgDsEqAqBmBeDiAcCc@eA[[uAgC_AyA{@kAoD{E}CmEsIiMe@s@]{@k@}Ag@aBc@qAg@}BO_Bq@kTE}@e@Bg@@uA^aAN"));
        mCarriagePoly.add(new LatLng(16.067381, 108.188133));
        // From 16.067381, 108.188133 to 16.078196, 108.211910
        mCarriagePoly.addAll(decodePoly("edaaBynisSqA^iDh@iAL{Et@wDj@S?mAUPsANaBBsDLyFByA?mDkD_@iAIPqGReH@iGCqAGoB[yEe@gF]qCgAkF_AgEiBeGmAwCi@iAuAyC_DsFqAeBuAeBuAmBmBqBmCiCgAaAoEiDgBoAp@}@j@o@d@Ol@E`AOd@AdBQd@G"));
        mCarriagePoly.add(new LatLng(16.070262, 108.213500));
        mCarriagePoly.add(new LatLng(16.070926, 108.217007));
        mCarriagePoly.add(new LatLng(16.068033, 108.217635));
        mCarriagePoly.add(new LatLng(16.067625, 108.215863));
        mCarriagePoly.addAll(decodePoly("ueaaBc|nsShC]~B_@|GgAdEY|Fi@t@?dBGnBMlAM?@@BDFPBNI^GVEfA@pA@lAKbECtA?vD?vBKbGc@tD]nHo@"));
        mCarriagePoly.add(new LatLng(16.054726, 108.220131));

        mCarriagePoly.addAll(decodePoly("au~`ByvosSjC]vGa@tCQdMo@h@EDDF@LCHKAQEEA?D]OkEBCDI@G@CTMlF]|M}@`SiAzE[zTqAnGa@hGYdHg@"));
        mCarriagePoly.addAll(decodePoly("{pz`BkppsSDAr@A@DFJFHXJR?RIJMDG@En@?l@?dAJbDt@dFdBtBp@dJ|Cr@Zz@h@~@x@j@v@x@rBdG|NbMbX|@xAjAbBhChDdBdCpAvB`@t@b@n@^h@^|@p@pB|@dDZfAl@|AnE|Hh@fAd@pAj@bCb@tBdA`Fn@vB|@nBlE~HbB|Dd@nAd@vAJj@PvABhAKvBc@rIGvFK`KBtAPfBXrA`@|Ar@bBx@zAxAvB"));

        mCarriagePoly.add(new LatLng(16.012229, 108.188047));
        // From 16.011595, 108.186250 to 15.998530, 108.138986
        mCarriagePoly.addAll(decodePoly("mgv`BacisSJr@FhA?bAE`@Mz@a@zAc@dAw@|Ai@lASp@WpAGf@IdBKfD?xCDnAFx@ZtBThA`@dBT|ANxCBtBAzDFtBp@zEx@jFPbBRbBBpAE`AYrB_C|JGh@C`BLzAVrApAjEhAbDl@jBdC|HlB|FzAzEHh@ZvBFvADnAAdBMzEMdDC`B@|BFzEFjB^nDPdAt@xCdAhCj@lAr@fAnA`B|@|@bChBhE|BpBl@rAZbAXd@VLNt@NvABv@Jr@PNDj@@|@T@HV^h@f@rA~@XZ`BpApA`Ax@dAFTDr@EhGlBdHn@lBn@bCf@tEJ|A"));
//        mCarriagePoly.add(new LatLng(15.991799, 108.137394));
        mCarriagePoly.addAll(decodePoly("yus`Bs{_sSVlBTh@TZz@p@vD|BtAl@vDfAvA`@JBj@@pDCx@?fB?z@E`AOLC"));
        mCarriagePoly.add(new LatLng(15.992198, 108.136744));
        mCarriagePoly.add(new LatLng(15.991966, 108.137051));
        mCarriagePoly.addAll(decodePoly("ykr`Bwq_sSrBgJtE`@ZFtAt@lE`DrCkE@?BAHEFI@KAErEsGHOf@Z~AfAtDnCtChBhAf@fATtAHfBEvCSvOaBfIw@lDUdAC|D?`G@`BAlDDzC@pA?pNF`GAdEBlL@vBBpE?xLFlBFvDNtFZvHf@fGf@|CRrLx@bEV|Ir@jKp@zBF~AArNw@`EUpE?pAJ`APxHhCnQfGjEnBpG`Ev@b@lDdCpBfA`MdF~NpFlW|JbG~BlEtBpFdCrBz@p@PpHdArFj@vYbCdDVn@HxFhAjAXLFf@`@hAzAhAnBVp@f@pBXbAXj@r@p@vA~@~@f@n@TrGfB"));
        mCarriagePoly.addAll(decodePoly("uoa`B_i|rSbC|@`DbBbC|AdI~ExJxFhOrId@^h@j@lAfBlBhDfBlDfD|H")); // Finish at 15.894432, 108.111077

        mCarriagePoly.addAll(decodePoly("ek_`BgmzrSHLXTf@Ll@D|@AdEMvFSlAHpF|@jDb@h@?t@U~CoA`AUbAMp@Ed@?lCPrAHnDHtBFr@EnAK"));
        mCarriagePoly.add(new LatLng(15.883088, 108.110508));
        mCarriagePoly.add(new LatLng(15.883151, 108.110501));

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
