package com.sep.sellers.service;

import com.sep.sellers.client.NCRegistrationClient;
import com.sep.sellers.dto.ActiveBillingPlanDTO;
import com.sep.sellers.dto.ApproveDTO;
import com.sep.sellers.dto.KPRegistrationDTO;
import com.sep.sellers.dto.SellerDTO;
import com.sep.sellers.model.ActiveBillingPlan;
import com.sep.sellers.model.PaymentMethod;
import com.sep.sellers.model.Seller;
import com.sep.sellers.model.SellerPaymentMethod;
import com.sep.sellers.repository.ActiveBillingPlanRepository;
import com.sep.sellers.repository.PaymentMethodRepository;
import com.sep.sellers.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Base64;
import java.util.List;

@Service
public class SellerService {

    @Autowired
    SellerRepository _sellerRepo;

    @Autowired
    PaymentMethodRepository _paymentRepo;

    @Autowired
    NCRegistrationClient ncRegistrationClient;

    @Autowired
    ActiveBillingPlanRepository planRepo;

    private final String  REG_PAGE_REDIRECT_URL = "https://192.168.43.124:4200/reg/";
    private final String kpAppUrl = "https://192.168.43.124:4200";

    public SellerDTO getSeller(long id) {
        System.out.println("\nID: " + id + "\n");
        return SellerDTO.formDto(_sellerRepo.findById(id).get());
    }

    public KPRegistrationDTO initRegistration(KPRegistrationDTO kprDTO) {

        Seller s = new Seller();
        s.setRegistrationStatusCallbackUrl(kprDTO.getRegistrationStatusCallbackUrl());

        s = _sellerRepo.save(s);

        kprDTO.setSellerId(s.getId());
        kprDTO.setRegistrationPageRedirectUrl(this.REG_PAGE_REDIRECT_URL + s.getId());
        kprDTO.setStatus(false);
        return kprDTO;
    }

    public KPRegistrationDTO reviewRegistration(KPRegistrationDTO kprDTO) {
        Seller s = _sellerRepo.findById(kprDTO.getSellerId()).get();
        kprDTO.setRegistrationPageRedirectUrl(this.REG_PAGE_REDIRECT_URL + s.getId());
        return  kprDTO;
    }

    public SellerDTO postRegistration(SellerDTO sDTO) {



        Seller s = _sellerRepo.findById(sDTO.getId()).get();
        s.setEmail(sDTO.getEmail());
        s.setPassword(hashPass(sDTO.getPassword()));
        s.setName(sDTO.getName());
        s.setOrganization(sDTO.getOrganization());
        s.setBaseWebsiteUrl(sDTO.getBaseWebsiteUrl());

        s = _sellerRepo.save(s);

        List<PaymentMethod> paymentMethods = _paymentRepo.findAll();

        for (PaymentMethod pm: paymentMethods) {
            SellerPaymentMethod sellerPM  = new SellerPaymentMethod();
            sellerPM.setSeller(s);
            sellerPM.setPaymentMethod(pm);
            sellerPM.setRegistrationSuccess(false);
            s.getPaymentMethods().add(sellerPM);
        }

//        for (PaymentMethodDTO pmDTO : sDTO.getPaymentMethods()) {
//            SellerPaymentMethod sellerPM  = new SellerPaymentMethod();
//
//            sellerPM.setSeller(s);
//            sellerPM.setPaymentMethod(choosePaymentMethod(pmDTO.getId()));
//            sellerPM.setRegistrationSuccess(false);
//            s.getPaymentMethods().add(sellerPM);
//        }

        s = _sellerRepo.save(s);

        return SellerDTO.formDto(s);
    }

    public String createPlan(ActiveBillingPlanDTO dto) {
        Seller seller = _sellerRepo.findById(dto.getSellerId()).get();
        boolean imaPP = false;
        for(SellerPaymentMethod spm : seller.getPaymentMethods()) {
            if(spm.getPaymentMethod().getName().equals("PayPal") && spm.isRegistrationSuccess()) {
                imaPP = true;
                break;
            }
        }
        if(imaPP) {
            String retUrl = this.kpAppUrl + "/paypal/plan/";
            ActiveBillingPlan plan = new ActiveBillingPlan(dto);
            plan = planRepo.save(plan);
            String temp = retUrl + plan.getId();
            return retUrl + plan.getId();
        }
        return "noPP";
    }

    public String getWebsiteURL(long sellerID) {
        Seller seller = _sellerRepo.findById(sellerID).get();
        return seller.getBaseWebsiteUrl();
    }

    public ActiveBillingPlanDTO getActivePlan(long id) {
        ActiveBillingPlan a = planRepo.findOneById(id);
        return new ActiveBillingPlanDTO(a);
    }

    public String removeActivePlan(long id) {
        ActiveBillingPlan a = planRepo.findOneById(id);
        planRepo.delete(a);
        return "done";
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

        KPRegistrationDTO kprDTO = new KPRegistrationDTO();
        kprDTO.setSellerId(s.getId());
        kprDTO.setRegistrationStatusCallbackUrl(s.getRegistrationStatusCallbackUrl());
        kprDTO.setStatus(true);

        ncRegistrationClient.informRegistrationStatus(kprDTO);
    }


    private PaymentMethod choosePaymentMethod(long id) {
        return _paymentRepo.findById(id).get();
    }

    private String hashPass(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean checkPass(String pw, String hashpw) { return BCrypt.checkpw(pw, hashpw); }


    public void registerNewPaymentService(PaymentMethod pm) {

        List<Seller> sellers = _sellerRepo.findAll();

        for (Seller s: sellers) {
            SellerPaymentMethod spm = new SellerPaymentMethod();
            spm.setRegistrationSuccess(false);
            spm.setPaymentMethod(pm);
            spm.setSeller(s);
            s.getPaymentMethods().add(spm);
            _sellerRepo.save(s);
        }
    }
}
