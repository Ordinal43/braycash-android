package id.ac.ukdw.braycash.Database;

import java.util.Date;

public class Transaction {
    private Long amount;
    private String phone;
    private String date;

    public Transaction(Long amount, String phone, String date) {
        this.amount = amount;
        this.phone = phone;
        this.date = date;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
