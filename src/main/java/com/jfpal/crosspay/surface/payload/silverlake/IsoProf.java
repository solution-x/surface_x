package com.jfpal.crosspay.surface.payload.silverlake;

public class IsoProf {

    public enum SPEC {
        NULL_SPEC,
        UPOS,
        SILVERLAKE;
    }
    class TransDataCom {
        SPEC spec;
        String sMTI;
        String sProcCode;
        long lFields[];

        TransDataCom(SPEC spec, String sMTI, String sProcCode, long lFields[]) {
            this.spec = spec;
            this.sMTI = sMTI;
            this.sProcCode = sProcCode;
            this.lFields = lFields;
        }
    }

    public IsoProf(String sMTI, String sProcCode) {
        transDataCom = null;

        for (int iItr=0; iItr<transComList.length; iItr++) {
            TransDataCom transDataComTemp = transComList[iItr];
            if (sMTI.equals(transDataComTemp.sMTI)==true && sProcCode.equals(transDataComTemp.sProcCode)==true) {
                transDataCom = transComList[iItr];
                break;
            }
        }
    }
    private TransDataCom transDataCom;
    private final TransDataCom[] transComList = new TransDataCom[]{
            /*SALE*/
            new TransDataCom(SPEC.UPOS, "0200", "000000", new long[]{2, 3, 4, 11, 14, 22, 23, 24, 25, 35, 41, 42, 55, 20061, 62 }),
            /*VOID - SALES*/
            new TransDataCom(SPEC.UPOS, "0200", "020000", new long[]{2, 3, 4, 11, 12, 13, 14, 22, 24, 25, 37, 38, 41, 42, 20061, 62, 20063}),
            /*REFUND*/
            new TransDataCom(SPEC.SILVERLAKE, "0200", "200000", new long[]{2, 3, 4, 11, 14, 22, 23, 24, 25, 35, 41, 42}),
            /*VOID - REFUND*/
            new TransDataCom(SPEC.SILVERLAKE, "0200", "220000", new long[]{2, 3, 4, 11, 14, 22, 23, 24, 25, 35, 37, 38, 41, 42}),
            /*OFFLINE UPLOAD*/
            new TransDataCom(SPEC.SILVERLAKE, "0220", "000000", new long[]{2, 3, 4, 11, 12, 13, 14, 22, 23, 24, 25, 35, 38, 41, 42, 62}),
            /*SALES REVERSAL*/
            new TransDataCom(SPEC.SILVERLAKE, "0400", "000000", new long[]{2, 3, 4, 11, 12, 13, 14, 22, 23, 24, 25, 35, 37, 41, 42, 62}),
            /*VOID REVERSAL*/
            new TransDataCom(SPEC.SILVERLAKE, "0400", "020000", new long[]{2, 3, 4, 11, 12, 13, 14, 22, 23, 24, 25, 35, 37, 41, 42, 62}),
            /*REFUND REVERSAL*/
            new TransDataCom(SPEC.SILVERLAKE, "0400", "200000", new long[]{2, 3, 4, 11, 12, 13, 14, 22, 23, 24, 25, 35, 37, 41, 42, 62}),
            /*SETTLEMENT 92*/
            new TransDataCom(SPEC.SILVERLAKE, "0500", "920000", new long[]{3, 11, 24, 41, 42, 50060, 50063}),
            /*SETTLEMENT 96*/
            new TransDataCom(SPEC.SILVERLAKE, "0500", "960000", new long[]{3, 11, 24, 41, 42, 50060, 50063}),
            /*BATCH UPLOAD - SALES*/
            new TransDataCom(SPEC.SILVERLAKE, "0320", "000000", new long[]{2, 3, 4, 11, 12, 13, 14, 22, 24, 25, 37, 38, 41, 42, 32060, 62}),
            /*BATCH UPLOAD - REFUND*/
            new TransDataCom(SPEC.SILVERLAKE, "0320", "200000", new long[]{2, 3, 4, 11, 12, 13, 14, 22, 24, 25, 37, 38, 41, 42, 32060, 62})
    };

    public long[] getFeilds() {
        if (transDataCom==null)
            return null;

        return transDataCom.lFields;
    }

    public SPEC getSPec() {
        if (transDataCom==null)
            return SPEC.NULL_SPEC;

        return transDataCom.spec;
    }
}
