package org.vanilladb.core.query.algebra;

public class ExplainScan implements Scan {
    private ExplainPlan explainPlan;
    private boolean hasNext = true;

    public ExplainScan(ExplainPlan explainPlan) {
        this.explainPlan = explainPlan;
    }

    @Override
    public boolean beforeFirst() {
        return true;
    }

    @Override
    public boolean next() {
        if (hasNext) {
            hasNext = false;
            return true;
        }
        return false;
    }

    @Override
    public Constant getVal(String fldName) {
        if (fldName.equals("query-plan")) {
            return new VarcharConstant(explainPlan.generateExplainOutput());
        }
        return null;
    }

    // Implement other methods of Scan interface as needed
}