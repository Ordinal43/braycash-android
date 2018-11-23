package id.ac.ukdw.braycash.Notif;

public class Notification {
    public  String tanggal, nomor_tujuan, nominal;

    public Notification(String tanggal, String nomor_sumber, String nominal){
        this.tanggal = tanggal;
        this.nomor_tujuan = nomor_sumber;
        this.nominal = nominal;
    }
}
