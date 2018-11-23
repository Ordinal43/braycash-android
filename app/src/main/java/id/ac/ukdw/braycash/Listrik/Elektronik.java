package id.ac.ukdw.braycash.Listrik;

public class Elektronik {
    private String id, nama, daya, status;

    public Elektronik(String id, String nama, String daya, String status) {
        this.id = id;
        this.nama = nama;
        this.daya = daya;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getDaya() {
        return daya;
    }

    public String getStatus() {
        return status;
    }
}
