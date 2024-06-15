package com.victor.spring_security_project.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
    ACTIVATE_ACCOUNT("activate_account"),

    PAYMENT_ORDER("payment_order"),

    PAYMENT_SUCCESSFUL("payment_successful"),

    PAYMENT_FAILURE("payment_failure");

    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
