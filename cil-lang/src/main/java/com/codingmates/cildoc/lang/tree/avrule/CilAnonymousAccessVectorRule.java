package com.codingmates.cildoc.lang.tree.avrule;

import com.codingmates.cildoc.lang.parser.base.Span;
import com.codingmates.cildoc.lang.tree.CilNode;
import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class CilAnonymousAccessVectorRule extends CilNode {
    private final CilAccessVectorRuleType type;
    private final String source;
    private final String target;
    private final List<String> permissions;
    private final Optional<String> securityClass;
    private final Optional<String> classPermissionSet;

    public CilAnonymousAccessVectorRule(CilAccessVectorRuleType type, String source, String target, String securityClass,
                                        List<String> permissions, Span span) {
        super(span);
        this.type = type;
        this.source = source;
        this.target = target;
        this.securityClass = Optional.of(securityClass);
        this.permissions = permissions;
        this.classPermissionSet = Optional.empty();
    }

    public CilAnonymousAccessVectorRule(CilAccessVectorRuleType type, String source, String target, String classPermissionSet,
                                        Span span) {
        super(span);
        this.type = type;
        this.source = source;
        this.target = target;
        this.classPermissionSet = Optional.of(classPermissionSet);
        this.securityClass = Optional.empty();
        this.permissions = Collections.EMPTY_LIST;
    }

    public Optional<String> getClassPermissionSet() {
        return classPermissionSet;
    }

    public List<String> getPermissions() {
        Preconditions.checkState(securityClass.isPresent(), "Can only call getPermissions() on an " +
            "access-vector which has been created with a security class and list of permissions.");

        return permissions;
    }

    public Optional<String> getSecurityClass() {
        return securityClass;
    }
}
