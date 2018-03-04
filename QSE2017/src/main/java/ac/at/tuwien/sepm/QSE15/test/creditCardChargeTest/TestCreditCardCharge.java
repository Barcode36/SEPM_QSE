package main.java.ac.at.tuwien.sepm.QSE15.test.creditCardChargeTest;

import main.java.ac.at.tuwien.sepm.QSE15.entity.creditCard.CreditCard;
import main.java.ac.at.tuwien.sepm.QSE15.entity.service.Service;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.creditCardPayment.CreditCardPaymentService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by ervincosic on 11/06/2017.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class TestCreditCardCharge {

    CreditCardPaymentService creditCardPaymentService;

    AnnotationConfigApplicationContext context;

    CreditCard testCard;

    @Before
    public void setup(){
        context = new AnnotationConfigApplicationContext(this.getClass());

        creditCardPaymentService = context.getBean(CreditCardPaymentService.class);

        testCard = new CreditCard();

        testCard.setCnr("4242424242424242");
        testCard.setExpMonth(6);
        testCard.setExpYear(2018);
        testCard.setCvv("314");

    }

    @Test
    public void testValidPayment()throws Exception{
        boolean paymeny = creditCardPaymentService.payAmountWithGivenCreditCard(testCard, 550, "desc");
        Assert.assertTrue(paymeny);
    }

    @Test (expected = ServiceException.class)
    public void testInvalidCreditCardPayment()throws Exception{
        testCard.setCnr("4242354242424242");
        creditCardPaymentService.payAmountWithGivenCreditCard(testCard, 1000, "Test invalid payment.");
    }
    @Test (expected = ServiceException.class)
    public void testNullCreditCard()throws Exception{
        creditCardPaymentService.payAmountWithGivenCreditCard(null, 6000, "Test null credit card");
    }


}
