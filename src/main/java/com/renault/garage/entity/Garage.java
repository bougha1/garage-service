package com.renault.garage.entity;

import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.util.*;

@Entity
@Table(name = "garages")
public class Garage {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String telephone;

    @Column(nullable = false)
    private String email;

    public List<OpeningTime> getOpeningTimes() {
        return openingTimes;
    }

    public void setOpeningTimes(List<OpeningTime> openingTimes) {
        this.openingTimes = openingTimes;
    }

    @ElementCollection
    @CollectionTable(
            name = "garage_opening_times",
            joinColumns = @JoinColumn(name = "garage_id")
    )
    @OneToMany(
            mappedBy = "garage",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OpeningTime> openingTimes = new ArrayList<>();

    @OneToMany(
            mappedBy = "garage",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Vehicle> vehicles = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
