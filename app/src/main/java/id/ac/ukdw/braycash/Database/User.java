package id.ac.ukdw.braycash.Database;

public class User {
    private String name;
    private String phone;
    private Long saldo;

    public User() {

    }

    public User(String name, String phone, Long saldo) {
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

    public Long getSaldo() {
        return saldo;
    }

    public void setSaldo(Long saldo) {
        this.saldo = saldo;
    }
}
