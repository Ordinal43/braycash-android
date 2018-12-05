package id.ac.ukdw.braycash.Model;

public class User {
    private String name;
    private String phone;
    private String pin;
    private Long saldo;
    private String profilePhoto;

    public User(String name, String phone, String pin, Long saldo, String profilePhoto) {
        this.name = name;
        this.phone = phone;
        this.pin = pin;
        this.saldo = saldo;
        this.profilePhoto = profilePhoto;
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

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Long getSaldo() {
        return saldo;
    }

    public void setSaldo(Long saldo) {
        this.saldo = saldo;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
