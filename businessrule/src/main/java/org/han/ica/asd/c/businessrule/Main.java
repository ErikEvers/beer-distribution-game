package org.han.ica.asd.c.businessrule;


public class Main {
    public static void main(String[] args) {
        Pipeline pipeline = new Pipeline();
        pipeline.parseString("if round is inventory then order 40\n" +
                             "default order 10");

        // For testing purposes
        System.out.println("---- The given input ----");
        System.out.println(pipeline.getBusinessRulesInput());
        System.out.println("---- The generated Businessrules Map ----");
        System.out.println(pipeline.getBusinessRulesMap());
    }
}
