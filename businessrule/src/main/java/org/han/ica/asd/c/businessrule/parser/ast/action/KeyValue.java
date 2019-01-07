package org.han.ica.asd.c.businessrule.parser.ast.action;

class KeyValue<K,V> {
    private K key;
    private V value;

    void put(K key, V value){
        this.key = key;
        this.value = value;
    }

    K getKey(){
        return key;
    }

    V getValue(){
        return value;
    }
}
