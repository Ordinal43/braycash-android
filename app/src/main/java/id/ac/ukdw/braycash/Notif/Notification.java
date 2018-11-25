package id.ac.ukdw.braycash.Notif;

public class Notification {
    public  String tanggal, sumber, nominal;

    public Notification(String tanggal, String sumber, String nominal){
        this.tanggal = tanggal;
        this.sumber = sumber;
        this.nominal = nominal;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getSumber() {
        return sumber;
    }

    public void setSumber(String sumber) {
        this.sumber = sumber;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }
}
