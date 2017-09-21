package com.aiddata.gecalculator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class GrantElementCalculator {

    private double loanVal,
            interestRate = 0.05,
            discountRate = 0.1,
            gracePeriod = 0,
            maturity,
            repaymentsPerYear = 2,
            disbursementSpanInYears = 1,
            disbursementsPerYear = 1;
    private boolean includeLifecycle = false;

    public static void main(String[] args) {
        //This main function exists solely for the purpose of easy testing.
        GrantElementCalculator gec = new GrantElementCalculator(17010964.6538428, 24, 0.025,
                01, 4, 2, 1, 1, false);
        Hashtable<String, String> loan = gec.calculateGrantElement();
        for(String key:loan.keySet()){
            System.out.println("The value of " + key + " is: " + loan.get(key));
        }
    }

    /**
     * All encompassing Constructor for the GrantElementCalculator.
     *
     * The required parameters and their descriptions are as follows:
     *
     * @param loanVal - double: This value is required. It is the amount of the loan.
     * @param maturity - double: This value is required. It is the time in years until the loan matures.
     * @param interestRate - double: The default value is 0.05. This is meant to be given as a decimal. ie - 25% = .25
     * @param discountRate - double: The default value is 0.1. This is meant to be given as a decimal. ie 3% = .03
     * @param gracePeriod - double: The default value is 0. Meant to be in years.
     * @param repaymentsPerYear - double: The default value is 2.
     * @param disbursementSpanInYears - double: The default value is 1.
     * @param disbursementsPerYear - double: The default value is 1.
     * @param includeLifecycle - boolean: The default value is false.
     */
    public GrantElementCalculator(double loanVal, double maturity, double interestRate, double discountRate,
                                  double gracePeriod, double repaymentsPerYear, double disbursementSpanInYears,
                                  double disbursementsPerYear, boolean includeLifecycle){

        if(loanVal != 0){
            this.loanVal = loanVal;
        } else {
            throw new RuntimeException("No loan value was provided.");
        }
        if(maturity != 0){
            this.maturity = maturity;
        } else {
            throw new RuntimeException("No maturity value was provided.");
        }

        this.interestRate = interestRate;
        this.discountRate = discountRate;
        this.gracePeriod = gracePeriod;
        this.repaymentsPerYear = repaymentsPerYear;
        this.disbursementSpanInYears = disbursementSpanInYears;
        this.disbursementsPerYear = disbursementsPerYear;
        this.includeLifecycle = includeLifecycle;
    }

    public Hashtable<String, String> calculateGrantElement(){
        double gracePeriods = this.gracePeriod * this.repaymentsPerYear;
        double repaymentPeriods = this.maturity * this.repaymentsPerYear;

        List<Hashtable<String, Double>> loanLifecycle = new ArrayList<Hashtable<String, Double>>();
        Hashtable<String, Double> previousPeriod = new Hashtable<String, Double>();

        for(double p = 0.0; p < repaymentPeriods; p++){
            Hashtable<String, Double> thisPeriod = new Hashtable<String, Double>();
            thisPeriod.put("index", p);
            thisPeriod.put("years", p/this.repaymentsPerYear);
            if(p > 0){
                thisPeriod.put("factor", previousPeriod.get("factor") * (1 + this.discountRate/this.repaymentsPerYear));
            } else {
                thisPeriod.put("factor", 1.0);
            }
            if((p + 1) <= (this.disbursementSpanInYears * this.disbursementsPerYear)){
                thisPeriod.put("disbursement", (this.disbursementSpanInYears/this.disbursementsPerYear) * this.loanVal);
            } else {
                thisPeriod.put("disbursement", 0.0);
            }

            //In order to calculate the amount that is outstanding for this period, we need to obtain some numbers first.
            double outStandingVal = 0.0, outstandingInterest = 0.0, previousDisbursement = 0.0, previousRepayment = 0.0;
            if(previousPeriod.get("outstanding") != null) {
                //What was already outstanding
                outStandingVal = previousPeriod.get("outstanding");
                //Plus Interest on that
                outstandingInterest = outStandingVal * (this.interestRate/this.repaymentsPerYear);
            }
            if(previousPeriod.get("disbursement") != null){
                //Plus disbursements since last time
                previousDisbursement = previousPeriod.get("disbursement");
            }
            if(previousPeriod.get("totalRepayment") != null) {
                //minus whatever was paid in the last period
                previousRepayment = previousPeriod.get("totalRepayment");
            }
            thisPeriod.put("outstanding", outStandingVal + outstandingInterest + previousDisbursement - previousRepayment);

            if(p != 0){
                thisPeriod.put("interestRepayment", thisPeriod.get("outstanding") * (this.interestRate/this.repaymentsPerYear));
            } else {
                thisPeriod.put("interestRepayment", 0.0);
            }

            if(p >= gracePeriods){
                thisPeriod.put("principalRepayment", (this.loanVal/(repaymentPeriods - gracePeriods + 1)));
            } else {
                thisPeriod.put("principalRepayment", 0.0);
            }

            thisPeriod.put("totalRepayment", thisPeriod.get("interestRepayment") + thisPeriod.get("principalRepayment"));
            thisPeriod.put("presentValueOfDisbursement" , thisPeriod.get("disbursement")/thisPeriod.get("factor"));
            thisPeriod.put("presentValueOfRepayment", thisPeriod.get("totalRepayment")/thisPeriod.get("factor"));

            loanLifecycle.add(thisPeriod);
            previousPeriod = thisPeriod;
        }

        double pvRepayment = 0.0, pvDisbursement = 0.0;

        for(Hashtable<String, Double> pd : loanLifecycle){
            pvRepayment += pd.get("presentValueOfRepayment");
            pvDisbursement += pd.get("presentValueOfDisbursement");
        }

        Hashtable<String, String> result = new Hashtable<String, String>();
        DecimalFormat twoDecimals = new DecimalFormat("#");
        twoDecimals.setMaximumFractionDigits(2);
        DecimalFormat fourDecimals = new DecimalFormat("#");
        fourDecimals.setMaximumFractionDigits(4);
        result.put("presentValueOfRepayments", twoDecimals.format(pvRepayment));
        result.put("presentValueOfDisbursements", twoDecimals.format(pvDisbursement));
        result.put("grantElementValue", twoDecimals.format(pvDisbursement - pvRepayment));
        result.put("grantElementPercent", fourDecimals.format(((pvDisbursement - pvRepayment)/pvDisbursement)));
        result.put("interestRate", twoDecimals.format(this.interestRate));
        result.put("discountRate", twoDecimals.format(this.discountRate));
        result.put("maturity", twoDecimals.format(this.maturity));
        result.put("gracePeriod", twoDecimals.format(this.gracePeriod));
        result.put("repaymentsPerYear", twoDecimals.format(this.repaymentsPerYear));

        if(this.includeLifecycle) {
            result.put("lifecycle", loanLifecycle.toString());
        }

        return result;
    }
}
