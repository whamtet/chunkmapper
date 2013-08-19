package com.chunkmapper.recaptcha;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;

public class Recaptcha {
	public static void main(String[] args) {
		String publicKey = "6LdY2uUSAAAAAGnRuNuhvIqN2BOmFa5IemMn4nY5",
				privateKey = "6LdY2uUSAAAAAKhwoxRq-1UPerVsXD3MRm37qc0F";
		ReCaptcha reCaptcha = ReCaptchaFactory.newReCaptcha(publicKey, privateKey, false);
		System.out.println(reCaptcha.createRecaptchaHtml(null, null, null));
		
	}

}
