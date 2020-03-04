package org.haivo_charity.model.support;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

public class RegExp {
    private static final String phoneReg = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$";
    private static final String emailReg = "^[a-z][a-z0-9_\\.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$";
    private static final String numberReg = "^[-+]?[0-9]*\\.?[0-9]+$";
    public static final int phone_number = 1;
    public static final int email = 2;
    public static final int text = 3;

    public static int Reg(String textCheck){
        if (checkEmail(textCheck))
            return phone_number;
        if (checkPhone(textCheck))
            return email;
        return text;
    }

    public static boolean checkEmail(String email){
        return email.matches(emailReg);
    }
    public static boolean checkPhone(String phone){
        return phone.matches(phoneReg);
    }
    public static boolean checkNumber(String number){
        return number.matches(numberReg);
    }

    public static String removeHTML(String text){
        return Jsoup.clean(text, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
    }
}
