package com.sep.paypalservice.dto;

import java.util.List;

public class AgreementListDTO {

    private List<AgreementDTO> agreements;

    public AgreementListDTO() {

    }
    public AgreementListDTO(List<AgreementDTO> lista) {
        this.agreements = lista;
    }

    public List<AgreementDTO> getAgreements() {
        return agreements;
    }

    public void setAgreements(List<AgreementDTO> agreements) {
        this.agreements = agreements;
    }
}
