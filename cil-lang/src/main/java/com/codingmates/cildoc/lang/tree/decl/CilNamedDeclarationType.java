package com.codingmates.cildoc.lang.tree.decl;

public enum CilNamedDeclarationType {
    TYPE,
    TYPEATTRIBUTE,
    TYPEALIAS,
    ROLE,
    ROLEATTRIBUTE,
    USER,
    USERATTRIBUTE,
    SENSITIVITY,
    SENSITIVITYALIAS,
    CATEGORY,
    CATEGORYALIAS,
    SID;

    public static final CilNamedDeclarationType[] VALUES = CilNamedDeclarationType.values();

    public static CilNamedDeclarationType from(String val) {
        for (CilNamedDeclarationType type : VALUES) {
            if (val.equals(type.toString().toLowerCase())) {
                return type;
            }
        }

        throw new IllegalArgumentException("Invalid name declaration type '" + val + "'");
    }
}
