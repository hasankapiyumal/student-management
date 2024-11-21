package com.student.management.studentmanagement.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ContextSignleTon {
    private static InitialContext ctx;

    private ContextSignleTon() {
    }

    public static InitialContext getContext() {
        if (ctx == null) {
            synchronized (ContextSignleTon.class) {
                if (ctx == null) {
                    try {
                        ctx = new InitialContext();
                    } catch (NamingException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return ctx;
    }
}
