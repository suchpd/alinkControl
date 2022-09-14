package com.alink.control.enumeration;

public enum ClientType {
    BaseStation("基站","bid"),
    AssetTag("资产标签","dev"),
    Other("其他","");

    public String name;
    public String idName;

    ClientType(String name,String idName){
        this.name = name;
        this.idName = idName;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
