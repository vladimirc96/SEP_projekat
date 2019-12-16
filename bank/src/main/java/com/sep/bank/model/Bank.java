package com.sep.bank.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;


    private Set<BankAccount> accounts;


}
