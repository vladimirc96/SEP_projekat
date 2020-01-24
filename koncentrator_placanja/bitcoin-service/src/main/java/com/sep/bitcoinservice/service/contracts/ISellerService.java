package com.sep.bitcoinservice.service.contracts;

import com.sep.bitcoinservice.dto.SellerDTO;
import com.sep.bitcoinservice.model.Seller;

import java.nio.file.AccessDeniedException;

public interface ISellerService {

    SellerDTO registerSeller(SellerDTO sDTO) throws AccessDeniedException;


}
