package org.han.ica.asd.c.businessrule.parser.alternatives;

import com.googlecode.concurrenttrees.common.Iterables;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.googlecode.concurrenttrees.suffix.ConcurrentSuffixTree;
import com.googlecode.concurrenttrees.suffix.SuffixTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AlternativeFinder {
    private SuffixTree<Integer> suffixTree;
    private static final int BIG_CHUNK_SIZE = 4;
    private static final int SMALL_CHUNK_SIZE = 3;
    private static final int MAX_WORD_LENGTH_FOR_SMALL_CHUNK = 6;
    private static final int MAX_ALTERNATIVES_SIZE = 2;

    public AlternativeFinder() {
        this.suffixTree = new ConcurrentSuffixTree<>(new DefaultCharArrayNodeFactory());
        this.setupSuffixTree();
    }

    private void setupSuffixTree(){
        List<String> possibleBusinessRuleValues = new ArrayList<>();

        possibleBusinessRuleValues.add("then");
        possibleBusinessRuleValues.add("default");
        possibleBusinessRuleValues.add("order");
        possibleBusinessRuleValues.add("deliver");
        possibleBusinessRuleValues.add("round");
        possibleBusinessRuleValues.add("from");
        possibleBusinessRuleValues.add("where");
        possibleBusinessRuleValues.add("factory");
        possibleBusinessRuleValues.add("warehouse");
        possibleBusinessRuleValues.add("wholesaler");
        possibleBusinessRuleValues.add("retailer");
        possibleBusinessRuleValues.add("inventory");
        possibleBusinessRuleValues.add("stock");
        possibleBusinessRuleValues.add("backlog");
        possibleBusinessRuleValues.add("incoming order");
        possibleBusinessRuleValues.add("back orders");
        possibleBusinessRuleValues.add("lowest");
        possibleBusinessRuleValues.add("smallest");
        possibleBusinessRuleValues.add("highest");
        possibleBusinessRuleValues.add("biggest");
        possibleBusinessRuleValues.add("equal");
        possibleBusinessRuleValues.add("greater");
        possibleBusinessRuleValues.add("higher");
        possibleBusinessRuleValues.add("than");
        possibleBusinessRuleValues.add("less");
        possibleBusinessRuleValues.add("lower");
        possibleBusinessRuleValues.add("plus");
        possibleBusinessRuleValues.add("minus");
        possibleBusinessRuleValues.add("times");
        possibleBusinessRuleValues.add("divided");

        int index = 0;

        for (String businessRuleValue: possibleBusinessRuleValues) {
            this.suffixTree.put(businessRuleValue, index++);
        }
    }

    public String findAlternative(String word){
        Map<CharSequence, Integer> foundMatches = new HashMap<>();
        int length = word.length();
        int chunckSize = length > MAX_WORD_LENGTH_FOR_SMALL_CHUNK ? BIG_CHUNK_SIZE : SMALL_CHUNK_SIZE;


        for (int i = 0; i < length && i + chunckSize <= length; i++) {
            String part = word.substring(i, i + chunckSize - 1);
            List<CharSequence> matches = Iterables.toList(suffixTree.getKeysContaining(part));

            for (CharSequence sequence : matches) {
                if (foundMatches.get(sequence) != null){
                    Integer value = foundMatches.get(sequence);
                    foundMatches.put(sequence, value + 1);
                } else {
                    foundMatches.put(sequence, 1);
                }
            }
        }

        Map<CharSequence, Integer> sorted = foundMatches
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(MAX_ALTERNATIVES_SIZE)
                .collect(
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));

        return String.join(", ", new ArrayList<>(sorted.keySet()));
    }
}
