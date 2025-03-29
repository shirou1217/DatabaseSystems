package org.vanilladb.core.storage.tx;

import java.util.Map;

import org.vanilladb.core.sql.Constant;

public class PrimaryKey {

    private String tableName;
    private Map<String, Constant> keyEntryMap;

    public PrimaryKey(String tableName, Map<String, Constant> keyEntryMap) {
        this.tableName = tableName;
        this.keyEntryMap = keyEntryMap;
    }

    public String getTableName() {
        return tableName;
    }

    public Constant getKeyVal(String fld) {
        return this.keyEntryMap.get(fld);
    }

    @Override
    public boolean equals(Object obj) {
        return keyEntryMap == obj;
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode = 31 * hashCode + tableName.hashCode();
        hashCode = 31 * hashCode + keyEntryMap.hashCode();
        return hashCode;
    }

}
