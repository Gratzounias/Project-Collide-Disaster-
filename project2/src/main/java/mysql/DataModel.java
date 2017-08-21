package mysql;

import javafx.beans.property.SimpleStringProperty;

public class DataModel { //klasi tou Datamodel pou xrisimopoioume
                                               //perilambanei ta :
    private final SimpleStringProperty source;
    private final SimpleStringProperty accx;
    private final SimpleStringProperty accy ;
    private final SimpleStringProperty accz;
    private final SimpleStringProperty prox;
    private final SimpleStringProperty lat;
    private final SimpleStringProperty ln;
    private final SimpleStringProperty date;
    private final SimpleStringProperty collide;

    public DataModel(String source, String accx ,String accy ,String accz ,String prox ,String lat ,String ln ,String date ,String colide) {
        this.source = new SimpleStringProperty(source);//dimiourgia twn antikeimenwn kai arxikopoiisi
        this.accx =  new SimpleStringProperty(accx);
        this.accy = new SimpleStringProperty(accy);
        this.accz = new SimpleStringProperty(accz);
        this.prox = new SimpleStringProperty(prox);
        this.lat = new SimpleStringProperty(lat);
        this.ln = new SimpleStringProperty(ln);
        this.date = new SimpleStringProperty(date);
        this.collide = new SimpleStringProperty(colide);
    }


    //Sinartisis Accessors-Getters pou epistrefoun tis times tou ka8e pediou tou DataModel
    public String getSource() {
        return source.get();
    }
    public String getAccx() {
        return accx.get();
    }
    public String getAccy() {
        return accy.get();
    }
    public String getAccz() {
        return accz.get();
    }
    public String getProx() {
        return prox.get();
    }
    public String getLat() {
        return lat.get();
    }
    public String getLn() {
        return ln.get();
    }
    public String getDate() {
        return date.get();
    }
    public String getCollide() {
        return collide.get();
    }


    //Sinartiseis Setters gia apodosi timis sto antistoixo pedio
    public void setSource(String source) {
        this.source.set(source);
    }
    public void setAccx(String accx) {
        this.accx.set(accx);
    }
    public void setAccy(String accy) {
        this.accy.set(accy);
    }
    public void setAccz(String accz) {
        this.accz.set(accz);
    }
    public void setProx(String prox) {
        this.prox.set(prox);
    }
    public void setLat(String lat) {
        this.lat.set(lat);
    }
    public void setLn(String ln) {
        this.ln.set(ln);
    }
    public void setDate(String date) {
        this.date.set(date);
    }
    public void setCollide(String colide) {
        this.collide.set(colide);
    }

}
