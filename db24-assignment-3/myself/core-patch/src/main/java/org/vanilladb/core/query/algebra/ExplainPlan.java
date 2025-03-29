package org.vanilladb.core.query.algebra;
import java.util.List;
import org.vanilladb.core.query.algebra.Plan;
/**
 * The {@link Plan} class corresponding to the <em>product</em> relational
 * algebra operator.
 */
public class ExplainPlan implements Plan {
    private List<Plan> subPlans;

    public ExplainPlan(List<Plan> subPlans) {
        this.subPlans = subPlans;
    }

    public String generateExplainOutput() {
        StringBuilder sb = new StringBuilder();
        for (Plan plan : subPlans) {
            generatePlanOutput(plan, sb, 0);
        }
        return sb.toString();
    }

    private void generatePlanOutput(Plan plan, StringBuilder sb, int level) {
        for (int i = 0; i < level; i++) {
            sb.append("\t");
        }
        sb.append("->").append(plan.getClass().getSimpleName()).append(" (#blks=")
                .append(plan.blocksAccessed()).append(", #recs=").append(plan.recordsOutput()).append(")\n");
        List<Plan> subPlans = plan.dumpPlan();
        if (subPlans != null) {
            for (Plan subPlan : subPlans) {
                generatePlanOutput(subPlan, sb, level + 1);
            }
        }
    }
}
