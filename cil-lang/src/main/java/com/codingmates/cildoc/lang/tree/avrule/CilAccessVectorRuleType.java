package com.codingmates.cildoc.lang.tree.avrule;

public enum CilAccessVectorRuleType {
    ALLOW,
    ALLOWX,
    AUDITALLOW,
    DONTAUDIT,
    NEVERALLOW;

    public static final CilAccessVectorRuleType[] VALUES = CilAccessVectorRuleType.values();

    public static CilAccessVectorRuleType from(String val) {
        for (CilAccessVectorRuleType type : VALUES) {
            if (val.equals(type.toString().toLowerCase())) {
                return type;
            }
        }

        throw new IllegalArgumentException("Invalid access vector rule type" + val + "'");
    }
}
