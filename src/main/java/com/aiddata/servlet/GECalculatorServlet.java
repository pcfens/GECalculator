package com.aiddata.servlet;

import com.aiddata.gecalculator.GrantElementCalculator;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

@WebServlet(name = "GECalculatorServlet", urlPatterns = {"/GECalculator/calculate"})
public class GECalculatorServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        genericHandler(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        genericHandler(request, response);
    }

    private void genericHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        if(request.getParameter("loanVal") != null && !request.getParameter("loanVal").equals("") &&
                !request.getParameter("loanVal").equals("0") && request.getParameter("maturity") != null &&
                !request.getParameter("maturity").equals("") && !request.getParameter("maturity").equals("0")){
            double loanVal = Double.valueOf(request.getParameter("loanVal"));
            double maturity = Double.valueOf(request.getParameter("maturity"));

            double interestRate = 0.05, discountRate = 0.1;
            double gracePeriod = 0.0, repaymentsPerYear = 2.0, disbursementSpanInYears = 1.0, disbursementsPerYear = 1.0;
            boolean includeLifecycle = false;

            if(request.getParameter("interestRate") != null && !request.getParameter("interestRate").equals("")) {
                interestRate = Double.valueOf(request.getParameter("interestRate"));
            }
            if(request.getParameter("discountRate") != null && !request.getParameter("discountRate").equals("")){
                discountRate = Double.valueOf(request.getParameter("discountRate"));
            }
            if(request.getParameter("gracePeriod") != null && !request.getParameter("gracePeriod").equals("")) {
                gracePeriod = Double.valueOf(request.getParameter("gracePeriod"));
            }
            if(request.getParameter("repaymentsPerYear") != null && !request.getParameter("repaymentsPerYear").equals("")) {
                repaymentsPerYear = Double.valueOf(request.getParameter("repaymentsPerYear"));
            }
            if(request.getParameter("disbursementSpanInYears") != null && !request.getParameter("disbursementSpanInYears").equals("")) {
                disbursementSpanInYears = Double.valueOf(request.getParameter("disbursementSpanInYears"));
            }
            if(request.getParameter("disbursementsPerYear") != null && !request.getParameter("disbursementsPerYear").equals("")) {
                disbursementsPerYear = Double.valueOf(request.getParameter("disbursementsPerYear"));
            }
            if(request.getParameter("includeLifecycle") != null && !request.getParameter("includeLifecycle").equals("")){
                includeLifecycle = Boolean.valueOf(request.getParameter("includeLifecycle"));
            }
            GrantElementCalculator gec = new GrantElementCalculator(loanVal, maturity, interestRate, discountRate,
                    gracePeriod, repaymentsPerYear, disbursementSpanInYears, disbursementsPerYear, includeLifecycle);
            Hashtable<String, String> results = gec.calculateGrantElement();

            Gson gson = new Gson();
            String json = gson.toJson(results);
            out.println(json);
        } else {
            out.println("ERROR: unable to generate Grant Element without the loanVal and the maturity. " +
                    "These values cannot be null, blank, or 0.");
        }
        out.flush();
        out.close();
    }
}
