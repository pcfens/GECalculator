<html>
<body>
<h2>Grant Element Calculator</h2>
<p>Using this page will generate a string of JSON that contains the calculations for the Present Value of Repayments,
    the Present Value of Disbursements, the Grant Element Value, and the Grant Element Percentage for a given loan.</p>
<p>The only required fields are the <b>Loan Amount</b> and the <b>Maturity</b>. All of the other fields have default
    values, as you can see below. If you leave a field blank, it will revert back to it's default value when you click
    the <b>Calculate Grant Element</b>
    button.</p>
<p>You can also use this API simply by passing in a URL of this form to your browser:</p>

<p>http://keystone.aiddata.wm.edu:8080/GECalculator/calculate?loanVal=10000&maturity=10&interestRate=0.05&
    discountRate=0.1&gracePeriod=0&repaymentsPerYear=2&disbursementSpanInYears=1&disbursementsPerYear=1&
    includeLifecycle=false</p>

<p>Again, the only required fields are the loanVal and maturity fields. If you don't fill out any others,
    the default values will be used. In other words, a URL of this form will work as well:</p>

<p>http://keystone.aiddata.wm.edu:8080/GECalculator/calculate?loanVal=10000&maturity=10</p>

<p>Happy Calculating!</p>

<form action="calculate"  method="get">
    Loan Amount: <input type="number" name="loanVal" step="0.01"><br>
    Maturity: <input type="number" name="maturity"><br>
    Interest Rate (as decimal): <input type="number" name="interestRate" value="0.05" step="0.01"><br>
    Discount Rate (as decimal): <input type="number" name="discountRate" value="0.1" step="0.01"><br>
    Grace Period: <input type="number" name="gracePeriod" value="0"><br>
    RepaymentsPerYear: <input type="number" name="repaymentsPerYear" value="2"><br>
    Disbursement Space In Years: <input type="number" name="disbursementSpanInYears" value="1"><br>
    Disbursements Per Year: <input type="number" name="disbursementsPerYear" value="1"><br>
    Include Lifecycle: <input type="checkbox" name="includeLifecycle" value="true"><br>
    <input type="submit" name="submit" value="Calculate Grant Element"><br>
</form>
</body>
</html>
