# Group 1: The Commanders

# Group 2: The Visitors

# Group 3: The Strategists

## Refactoring Aggressive Conditionals to the Strategy Pattern

### Problem: Pricing engine with dozens of business rules (real fintech nightmare)

In banking/fintech apps you often need to calculate a final price/rate that depends on customer type, product, campaign, jurisdiction, risk, etc. Without Strategy, you end up with massive if/else chains like this:

```java
// BAD: Classic conditional explosion — impossible to maintain
public PricingResult calculate(PricingRequest request) {
    Customer c = request.getCustomer();
    Product p = request.getProduct();
    double rate = p.getBaseRate();
    List<Fee> fees = new ArrayList<>();

    if (c.isPremium()) {
        rate -= 0.5;
        if (p.isMortgage() && c.getCreditScore() > 750) rate -= 0.25;
    }
    if ("SUMMER2025".equals(request.getCampaign())) {
        rate -= 1.0;
        fees.add(new Fee("Summer Bonus", -250));
    } else if ("PARTNER_X".equals(request.getCampaign())) rate -= 0.75;

    if (request.getJurisdiction() == EU) {
        rate = Math.min(rate, 4.0);
        fees.add(new Fee("EU Fee", 49));
    }
    if (request.isHighRisk()) rate += 2.0;

    // ... another 200+ lines for corporate, A/B tests, referrals, etc.
    return new PricingResult(rate, fees);
}
```

### Solution: Strategy Pattern + Composite

```java
// 1. The Strategy interface
interface PricingStrategy {
    PricingResult apply(PricingContext context);
}
```

### Why Strategy (+ Composite) is justified here:

1. You have many independent business rules (premium, campaigns, regulations, risk, A/B tests…)

2. You have multiple pricing contexts (retail, corporate, experiments)
3. Adding a new rule → just add one small strategy class
4. Changing order or enabling/disabling rules → just reorder the list
5. No more 500-line if/else monsters
6. Open/Closed Principle fully respected
