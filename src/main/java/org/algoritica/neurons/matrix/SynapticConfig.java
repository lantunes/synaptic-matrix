package org.algoritica.neurons.matrix;

/*
 * based on the equation: weight = b + c + kN^-1
 */
public class SynapticConfig {
    private final long b;
    private final long c;
    private final long k;
    private final int dendrodendriticContributionLevel;

    public SynapticConfig(long b, long c, long k) {
        this(b, c, k, 0);
    }

    public SynapticConfig(long b, long c, long k, int dendrodendriticContributionLevel) {
        this.b = b;
        this.c = c;
        this.k = k;
        this.dendrodendriticContributionLevel = dendrodendriticContributionLevel;
    }

    public long b() {
        return b;
    }

    public long c() {
        return c;
    }

    public long k() {
        return k;
    }

    public int dendrodendriticContributionLevel() {
        return dendrodendriticContributionLevel;
    }
}
