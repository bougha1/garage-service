package com.renault.garage.dto.garage;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public class GarageCreateDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotBlank
    private String telephone;

    @Email
    @NotBlank
    private String email;

    @NotNull
    private Map<DayOfWeek, List<OpeningTimeDTO>> openingTimes;

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

    public Map<DayOfWeek, List<OpeningTimeDTO>> getOpeningTimes() {
        return openingTimes;
    }

    public void setOpeningTimes(Map<DayOfWeek, List<OpeningTimeDTO>> openingTimes) {
        this.openingTimes = openingTimes;
    }
}