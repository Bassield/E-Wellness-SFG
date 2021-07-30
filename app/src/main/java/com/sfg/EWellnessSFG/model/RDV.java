package com.sfg.EWellnessSFG.model;

import java.util.Date;

public class RDV {
    private int noRDV;
    private Date dateRDV;
    private Date datePriseRDV;
    private String docSurname;

    public RDV(int noRDV, Date dateRDV, Date datePriseRDV, String docSurname) {
        this.noRDV = noRDV;
        this.dateRDV = dateRDV;
        this.datePriseRDV = datePriseRDV;
        this.docSurname = docSurname;
    }

    public int getNoRDV() {
        return noRDV;
    }

    public void setNoRDV(int noRDV) {
        this.noRDV = noRDV;
    }

    public Date getDateRDV() {
        return dateRDV;
    }

    public void setDateRDV(Date dateRDV) {
        this.dateRDV = dateRDV;
    }

    public Date getDatePriseRDV() {
        return datePriseRDV;
    }

    public void setDatePriseRDV(Date datePriseRDV) {
        this.datePriseRDV = datePriseRDV;
    }

    public String getDocSurname() {
        return docSurname;
    }

    public void setDocSurname(String docSurname) {
        this.docSurname = docSurname;
    }

    //Show status of RDV
    public void stateRDV(){
        System.out.println("The appointment "+getNoRDV()+" is in progress");
    }
}
