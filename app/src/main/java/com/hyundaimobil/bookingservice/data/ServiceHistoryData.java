package com.hyundaimobil.bookingservice.data;

/**
 * Created by User HMI on 12/20/2017.
 */

public class ServiceHistoryData {
    private String customer_code, chasis, engine, police, kd_rep, kd_coy, kd_cab, kd_notranp, kd_partno, kd_nokwb, kd_nowo,
            no, wo, wo_date, repair_code, description;

    public ServiceHistoryData() {}

    public ServiceHistoryData(String customer_code, String chasis, String engine, String police, String kd_rep, String kd_coy,
                              String kd_cab, String kd_notranp, String kd_partno, String kd_nokwb, String kd_nowo,
                              String no, String wo, String wo_date, String repair_code, String description) {
        this.customer_code  = customer_code;
        this.chasis         = chasis;
        this.engine         = engine;
        this.police         = police;
        this.kd_rep         = kd_rep;
        this.kd_coy         = kd_coy;
        this.kd_cab         = kd_cab;
        this.kd_nowo        = kd_nowo;

        this.kd_notranp     = kd_notranp;
        this.kd_partno      = kd_partno;
        this.kd_nokwb       = kd_nokwb;

        this.no             = no;
        this.wo             = wo;
        this.wo_date        = wo_date;
        this.repair_code    = repair_code;
        this.description    = description;
    }

    public String getCustomerCode() {
        return customer_code;
    }
    public void setCustomerCode(String customer_code) {
        this.customer_code = customer_code;
    }

    public String getChasis() {
        return chasis;
    }
    public void setChasis(String chasis) {
        this.chasis = chasis;
    }

    public String getEngine() {
        return engine;
    }
    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getPolice() {
        return police;
    }
    public void setPolice(String police) {
        this.police = police;
    }

    public String getKdRep() {
        return kd_rep;
    }
    public void setKdRep(String kd_rep) {
        this.kd_rep = kd_rep;
    }

    public String getKdCoy() {
        return kd_coy;
    }
    public void setKdCoy(String kd_coy) {
        this.kd_coy = kd_coy;
    }

    public String getKdCab() {
        return kd_cab;
    }
    public void setKdCab(String kd_cab) {
        this.kd_cab = kd_cab;
    }

    public String getKdNoWo() {
        return kd_nowo;
    }
    public void setKdNoWo(String kd_nowo) {
        this.kd_nowo = kd_nowo;
    }




    public String getKdNoTranp() {
        return kd_notranp;
    }
    public void setKdNoTranp(String kd_notranp) {
        this.kd_notranp = kd_notranp;
    }

    public String getKdPartNo() {
        return kd_partno;
    }
    public void setKdPartNo(String kd_partno) {
        this.kd_partno = kd_partno;
    }

    public String getKdNoKwb() {
        return kd_nokwb;
    }
    public void setKdNoKwb(String kd_nokwb) {
        this.kd_nokwb = kd_nokwb;
    }



    public String getNo() {
        return no;
    }
    public void setNo(String no) {
        this.no = no;
    }

    public String getWo() {
        return wo;
    }
    public void setWo(String wo) {
        this.wo = wo;
    }

    public String getWoDate() {
        return wo_date;
    }
    public void setWoDate(String wo_date) {
        this.wo_date = wo_date;
    }

    public String getRepairCode() {
        return repair_code;
    }
    public void setRepairCode(String repair_code) {
        this.repair_code = repair_code;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}
