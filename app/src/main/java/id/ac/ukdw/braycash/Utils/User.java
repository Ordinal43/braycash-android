package id.ac.ukdw.braycash.Utils;

public class User {
    private String name;
    private String phone;
    private int saldo;

    public User() {

    }

    public User(String name, String phone, int saldo) {
        this.name = name;
        this.phone = phone;
        this.saldo = saldo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }
}
