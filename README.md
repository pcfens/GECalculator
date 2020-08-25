# GECalculator

## Using the calculator

Currently deployed at http://keystone.aiddata.wm.edu:8080/GECalculator/

### Running in Docker Locally

To build the container, use
`docker build -t gecalculator .`

To run the container on your local machine after use
`docker run --rm -it -p8080:8080 gecalculator`

### Making a request

There are two ways to use this Calculator. You can either go to http://keystone.aiddata.wm.edu:8080/GECalculator/ and follow the instructions/fill out the form provided on that page, or...

You can GET or POST  data to `/calculate`, for example:

http://keystone.aiddata.wm.edu:8080/GECalculator/calculate?loanVal=10000&maturity=10&interestRate=0.05& discountRate=0.1&gracePeriod=0&repaymentsPerYear=2&disbursementSpanInYears=1&disbursementsPerYear=1& includeLifecycle=false

The only required fields are the loanVal and maturity fields. If you don't fill out any others, then default values will be used. In other words, a URL of this form will work as well:

http://keystone.aiddata.wm.edu:8080/GECalculator/calculate?loanVal=10000&maturity=10

Your request __must__ include:

- loanVal 
- maturity (in years)

Your request __may__ include:

- gracePeriod (in years, defaults to 0)
- interestRate (provide as a decimal, eg `0.05` for 5%, or defaults to 5% )
- discountRate (provide as a decimal, eg `0.1` for 10%, or defaults to 10%)
- repaymentsPerYear (defaults to 2, ie, semi-annual)
- disbursementSpanInYears (defaults to 1, ie, lump sum)
- disbursementsPerYear (defaults to 1, ie, lump sum)
- includeLifecycle (pass "true" to see the calculated repayment steps )

### The Response
The JSON response includes the grant element value and grant element percent, as well as all values used in calculating the grant element.

If you request the loan lifecycle, it is also returned.

## Grant Element

Grant Element (Value) = (Present Value of Future Disbursements) - (Present Value of Future Repayments)
Grant Element (Percent) = Grant Element Value / (Present Value of Future Disbursements)

__Face value__ of the loan = declared amount
__Present value__ of the loan = Present value of future disbursements
