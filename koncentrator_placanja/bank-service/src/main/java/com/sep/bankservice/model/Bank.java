package com.sep.bankservice.model;

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
