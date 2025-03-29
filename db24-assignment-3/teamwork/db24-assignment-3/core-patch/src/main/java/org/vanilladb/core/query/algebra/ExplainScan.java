package org.vanilladb.core.query.algebra;

import org.vanilladb.core.sql.Constant;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.VarcharConstant;

public class ExplainScan implements Scan {
    private String result;
    private int numRecs;
    private Schema schema;
    private boolean isBeforeFirsted;

    public ExplainScan(Scan s, Schema schema, String explain) {
        this.schema = schema;
        s.beforeFirst();

        while (s.next()) {
            ++this.numRecs;
        }

        s.close();
        this.result = "\n" + explain + "\nActual #recs: " + this.numRecs;
        this.isBeforeFirsted = true;
    }

    public Constant getVal(String fldName) {
        if (fldName.equals("query-plan")) {
            return new VarcharConstant(this.result);
        } else {
            throw new RuntimeException("field " + fldName + " not found.");
        }
    }

    public void beforeFirst() {
        this.isBeforeFirsted = true;
    }

    public boolean next() {
        if (this.isBeforeFirsted) {
            this.isBeforeFirsted = false;
            return true;
        } else {
            return false;
        }
    }

    public void close() {
    }

    public boolean hasField(String fldname) {
        return this.schema.hasField(fldname);
    }
}
