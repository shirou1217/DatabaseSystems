package org.vanilladb.core.query.algebra;

import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.storage.metadata.statistics.Histogram;

public class ExplainPlan implements Plan {
    private Plan p;

    public ExplainPlan(Plan p) {
        this.p = p;
    }

    public Scan open() {
        return new ExplainScan(this.p.open(), this.schema(), toString(0));
    }

    public String toString(int level) {
        return p.toString(0);
    }

    public long blocksAccessed() {
        return this.p.blocksAccessed();
    }

    public Schema schema() {
        Schema schema = new Schema();
        schema.addField("query-plan", Type.VARCHAR(500));
        return schema;
    }

    public Histogram histogram() {
        return this.p.histogram();
    }

    public long recordsOutput() {
        return 1L;
    }
}