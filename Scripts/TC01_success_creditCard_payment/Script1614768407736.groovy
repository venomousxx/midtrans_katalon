import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as Web

//def variable
def card_number = '4811 1111 1111 1114'
def cvv = '123'
def exp_date = '02/25'
def otp = '112233'

//open browser
Web.openBrowser('https://demo.midtrans.com/')

Web.maximizeWindow()

Web.click(findTestObject('Object Repository/btn_buy'))

//get data input
total_amount = Web.getText(findTestObject('Object Repository/label_total_amount'))

int total_amount = Integer.parseInt(total_amount.replaceAll("[-+^]*,",""))

name = Web.getAttribute(findTestObject('Object Repository/label_name'), 'value')

email = Web.getAttribute(findTestObject('Object Repository/label_email'), 'value')

phone_number = Web.getAttribute(findTestObject('Object Repository/label_phone_no'), 'value')

String phone_number = phone_number.replaceFirst("0", "+62");

city = Web.getAttribute(findTestObject('Object Repository/label_city'), 'value')

address = Web.getText(findTestObject('Object Repository/label_address'))

postal_code = Web.getAttribute(findTestObject('Object Repository/label_postal_code'), 'value')

	//print data input (untuk membuat nilai perbandingan jika di butuhkan)
	KeywordUtil.logInfo('total amount =' + total_amount)
	
	KeywordUtil.logInfo('nama = ' + name)
	
	KeywordUtil.logInfo('email = ' + email)
	
	KeywordUtil.logInfo('phone_number = ' + phone_number)
	
	KeywordUtil.logInfo('city = ' + city)
	
	KeywordUtil.logInfo('address = ' + address)
	
	KeywordUtil.logInfo('postal_code = ' + postal_code)

//checkout	
Web.click(findTestObject('Object Repository/btn_checkout'))

//handle iframe
Web.switchToFrame(findTestObject('Object Repository/iframe'), 30)

//next to payment method
Web.click(findTestObject('Object Repository/btn_continue'))

//choose debit or credit card
Web.click(findTestObject('Object Repository/btn_credit_debit_card'))

//input information card, exp date, cvv
Web.setText(findTestObject('Object Repository/txt_input_card'), card_number)

Web.setText(findTestObject('Object Repository/txt_input_ExpiryDate'), exp_date)

Web.setText(findTestObject('Object Repository/txt_input_cvv'), cvv)

final_amount = Web.getText(findTestObject('Object Repository/label_amount'))

int final_amount = Integer.parseInt(final_amount.replaceAll("[-+^]*,",""))

disc = Web.getText(findTestObject('Object Repository/label_disc')).substring(5)

int disc = Integer.parseInt(disc.replaceAll("[-+^]*,",""))

	KeywordUtil.logInfo('total amount = ' + final_amount)
	
	KeywordUtil.logInfo('discount = ' + disc)
	
expected_amount = (total_amount - disc)

KeywordUtil.logInfo('hasil = ' + expected_amount)

//verify amount after get discount
Web.verifyEqual(final_amount, expected_amount)

Web.scrollToElement(findTestObject('Object Repository/label_email_fromCard'), 10)
	
//verify value input
email_card = Web.verifyElementAttributeValue(findTestObject('Object Repository/label_email_fromCard'), 'placeholder', email, 10)

//verify phone number 
if (Web.verifyElementAttributeValue(findTestObject('Object Repository/label_phone_fromCard'), 'placeholder', phone_number, 10, FailureHandling.OPTIONAL) == false ){
	
	KeywordUtil.logInfo('data phone number yang tampil saat input card (value yg terdapat pada placeholder), tidak sesuai dengan data inputan phone number di awal')

} else {

	KeywordUtil.logInfo('data sesuai!')

}

//click paynow button
Web.click(findTestObject('Object Repository/btn_PayNow')) 

//handle second iframe
Web.switchToFrame(findTestObject('Object Repository/iframe_2'), 30)

//input otp
Web.sendKeys(findTestObject('Object Repository/txt_otp'), otp)

//click btn ok to make transaction
Web.click(findTestObject('Object Repository/btn_ok'))

//switch to default content
Web.switchToDefaultContent()

//verify success message on homepage
Web.waitForElementVisible(findTestObject('Object Repository/btn_buy'), 60)

Web.verifyElementPresent(findTestObject('label_success_payment'), 30)

