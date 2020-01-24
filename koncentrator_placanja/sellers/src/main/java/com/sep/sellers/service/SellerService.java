package com.sep.sellers.service;

import com.sep.sellers.dto.ApproveDTO;
import com.sep.sellers.dto.PaymentMethodDTO;
import com.sep.sellers.dto.SellerDTO;
import com.sep.sellers.model.PaymentMethod;
import com.sep.sellers.model.Seller;
import com.sep.sellers.model.SellerPaymentMethod;
import com.sep.sellers.repository.PaymentMethodRepository;
import com.sep.sellers.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class SellerService {

    @Autowired
    SellerRepository _sellerRepo;

    @Autowired
    PaymentMethodRepository _paymentRepo;


    public SellerDTO getSeller(long id) {
        return SellerDTO.formDto(_sellerRepo.findById(id).get());
    }

    public SellerDTO initRegistration() {

        Seller s = new Seller();

        return SellerDTO.formDto(_sellerRepo.save(s));
    }

    public SellerDTO postRegistration(SellerDTO sDTO) {



        Seller s = _sellerRepo.findById(sDTO.getId()).get();
        s.setEmail(sDTO.getEmail());
        s.setPassword(hashPass(sDTO.getPassword()));
        s.setName(sDTO.getName());
        s.setOrganization(sDTO.getOrganization());


        s = _sellerRepo.save(s);

        for (PaymentMethodDTO pmDTO : sDTO.getPaymentMethods()) {
            SellerPaymentMethod sellerPM  = new SellerPaymentMethod();

            sellerPM.setSeller(s);
            sellerPM.setPaymentMethod(choosePaymentMethod(pmDTO.getId()));
            sellerPM.setRegistrationSuccess(false);
            s.getPaymentMethods().add(sellerPM);
        }

        s = _sellerRepo.save(s);

        return SellerDTO.formDto(s);
    }





    public void approveRegistration(ApproveDTO approveDTO) throws AccessDeniedException {

        Seller s = _sellerRepo.findById(approveDTO.getId()).get();

        if (!checkPass(approveDTO.getPassword(), s.getPassword())) {
            throw new AccessDeniedException("Password is not valid");
        }


        for (SellerPaymentMethod pm : s.getPaymentMethods()) {
            if (pm.getPaymentMethod().getId() == approveDTO.getMethodId()) {
                pm.setRegistrationSuccess(true);
            }
        }


        _sellerRepo.save(s);
    }


    private PaymentMethod choosePaymentMethod(long id) {
        return _paymentRepo.findById(id).get();
    }

    private String hashPass(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean checkPass(String pw, String hashpw) { return BCrypt.checkpw(pw, hashpw); }

}
