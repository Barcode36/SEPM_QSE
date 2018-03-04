package main.java.ac.at.tuwien.sepm.QSE15.service.creditCardPayment;

import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import main.java.ac.at.tuwien.sepm.QSE15.entity.creditCard.CreditCard;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ervincosic on 11/06/2017.
 */
@Service
public class CreditCardPaymentService {

    Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init(){
        Stripe.apiKey = "sk_test_38p2yhLo9zcpbWy9EwE19aUl";
    }

    /**
     * Use this method to process a credit card payement
     * @param creditCard - Credit card has to have cnr, cvv, expMont, expYear set in order to work
     * @param amountInCents - the amount you want to charge the card for in cents
     * @param description - Description for the charge
     * @throws ServiceException thrown in multiple occasions read the message for better disclosure
     */
    public boolean payAmountWithGivenCreditCard(CreditCard creditCard, long amountInCents, String description)throws ServiceException{

        Token token ;

        if(!isCardValid(creditCard)){
            throw new ServiceException("Credit card object sucks!");
        }else if(description == null || description.length() == 0){
            throw new ServiceException("Description sucks!");
        }else if(amountInCents < 0){
            throw  new ServiceException("The amount is negative.");
        }

        token = generateStripeAPIToken(creditCard);

        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", amountInCents);
        chargeParams.put("currency", "eur");
        chargeParams.put("description", description);
        chargeParams.put("source", token.getId());

        Charge charge = null;

        try {
            charge = Charge.create(chargeParams);
        } catch (AuthenticationException e) {
            throw new ServiceException("Problem with Stripe authentication.");
        } catch (InvalidRequestException e) {
            throw  new ServiceException("Invalid request.");
        } catch (APIConnectionException e) {
            throw new ServiceException("Problem with Stripe server");
        } catch (CardException e) {
            throw new ServiceException("Credit card refused.");
        } catch (APIException e) {
            throw new ServiceException("Problem with stripe server.");
        }

        return true;

    }

    /**
     * This method is used to generate the stripe Token so a card can be charged
     * @param creditCard -  Credit card has to have cnr, cvv, expMont, expYear set in order to work
     * @return - Token object
     */
    private Token generateStripeAPIToken(CreditCard creditCard) throws ServiceException{
        Map<String, Object> tokenParams = new HashMap<>();

        Map<String, Object> cardParams = new HashMap<>();

        if(creditCard == null) {
            throw new ServiceException("The given card is a null pointer.");
        }

        cardParams.put("number", creditCard.getCnr());
        cardParams.put("exp_month", creditCard.getExpMonth());
        cardParams.put("exp_year", creditCard.getExpYear());
        cardParams.put("cvc", creditCard.getCvv());

        tokenParams.put("card", cardParams);

        Token token = null;


        try {
            token = Token.create(tokenParams);
        } catch (AuthenticationException e) {
            throw new ServiceException(e.getMessage());
        } catch (InvalidRequestException e) {
            throw new ServiceException("Problem with the request.");
        } catch (APIConnectionException e) {
            throw new ServiceException("Problem with the API connection.");
        } catch (CardException e) {
            throw new ServiceException("The given card is not valid");
        } catch (APIException e) {
            throw new ServiceException(e.getMessage());
        }


        return token;
    }

    /**
     * checks if the cards params are set
     * @param creditCard
     * @return true or false, duh...
     */
    private boolean isCardValid(CreditCard creditCard){

        if(creditCard == null){
            return false;
        }

        if(creditCard.getCnr() == null){
            return false;
        }else if (creditCard.getCnr().length() == 0){
            return false;
        }else if(creditCard.getCvv() == null){
            return false;
        }else if(creditCard.getCvv().length() != 3){
            return false;
        }else if(creditCard.getExpMonth() < 0 || creditCard.getExpMonth() > 12){
            return false;
        }else if(creditCard.getExpYear() < 0){
            return false;
        }else{
            return true;
        }
    }
}
