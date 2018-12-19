package com.webill.domain;

import java.io.Serializable;
import javax.persistence.*;

public class ExtraUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "phone")
    private String phone;
    @Column(name = "address")
    private String address;

    @OneToOne
    @MapsId
    private User user;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}