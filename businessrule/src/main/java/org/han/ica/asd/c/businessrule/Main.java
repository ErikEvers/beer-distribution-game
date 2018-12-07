package org.han.ica.asd.c.businessrule;


import org.han.ica.asd.c.businessrule.parser.ParserPipeline;

public class Main {
    public static void main(String[] args) {
        ParserPipeline parserPipeline = new ParserPipeline();
        parserPipeline.parseString("if inventory is 20 then order 40");

//         For testing purposes
        System.out.println("---- The given input ----");
        System.out.println(parserPipeline.getBusinessRulesInput());
        System.out.println("---- The generated Businessrules Map ----");
        System.out.println(parserPipeline.getBusinessRulesMap());
    }

}
