package com.luan.desafioconcrete.dto;

import com.luan.desafioconcrete.domain.Phone;
import java.util.ArrayList;
import java.util.List;

public class PhoneDTO {

    private int ddd;
    private int number;

    public PhoneDTO(int ddd, int number) {
        this.ddd = ddd;
        this.number = number;
    }

    public PhoneDTO(Phone phone) {
        this.ddd = phone.getDdd();
        this.number = phone.getNumber();
    }

    public PhoneDTO() {
    }

    public int getDdd() {
        return ddd;
    }

    public void setDdd(int ddd) {
        this.ddd = ddd;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public static List<PhoneDTO> convertList(List<Phone> phones){

        List<PhoneDTO> phoneDTOS =  new ArrayList<>();
        for(Phone phone : phones){
            phoneDTOS.add(new PhoneDTO(phone));
        }
        return phoneDTOS;
    }
}
