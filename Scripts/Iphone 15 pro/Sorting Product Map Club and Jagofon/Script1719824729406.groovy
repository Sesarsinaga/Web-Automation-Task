import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import models.Product

import groovy.json.JsonSlurper
import java.util.ArrayList

"========================================= Browse Product Jagofon ============================================="
'Open Browser Jagofon Web Application'
WebUI.openBrowser("https://jagofon.com/", FailureHandling.STOP_ON_FAILURE)

'Maximize Browser'
WebUI.maximizeWindow(FailureHandling.STOP_ON_FAILURE)

'Verify Jagofon Image is Appear'
WebUI.verifyElementPresent(findTestObject('Object Repository/Jagofon/Image_Jago_fon'), 0, FailureHandling.STOP_ON_FAILURE)

'Search For Product Iphone 15 Pro'
WebUI.setText(findTestObject('Object Repository/Jagofon/Browse_Product_Jagofon'), "Iphone 15 Pro", FailureHandling.STOP_ON_FAILURE)

'Click Dropdown Searching for Iphone 15 Pro'
WebUI.click(findTestObject('Object Repository/Jagofon/iPhone 15 Pro'), FailureHandling.STOP_ON_FAILURE)

'Verify Header Product Searched'
WebUI.verifyElementText(findTestObject('Object Repository/Jagofon/Header_Iphone15Pro_Product_Searched'), "iPhone 15 Pro", FailureHandling.STOP_ON_FAILURE)

browseJagofon = findTestObject('Object Repository/Jagofon/Get List Product Jagofon')

'Request Body Auth'
request_body = (browseJagofon.getHttpBody() as String)
WS.comment('request body ' + request_body)

'Response Body Auth'
response_body = WS.sendRequest(browseJagofon)
WS.comment('response body ' + response_body.getResponseText())

'Validate Status Code'
 WS.verifyResponseStatusCode(response_body, 200)

'Get Parsing Data'
JsonSlurper slurper = new JsonSlurper()
Map parsedJson = slurper.parseText(response_body.getResponseText())

def productsJagofon = parsedJson.data
def productList = [] //MODEL RESULT FOR JAGOFON AND MAP CLUB

productsJagofon.take(5).each { product ->
	def productName = product.model.name
	def salePriceLabel = product.parent.price
	def pageUrl = "https://jagofon.com/product/" + product.id
	
	def newProduct = new Product(
		productName,
		salePriceLabel,
		pageUrl,
		"JAGOFON"
	)
	productList.add(newProduct)
}

WebUI.closeBrowser()


"========================================= Browse Product Map Club ============================================="
'Open Browser Map Club Web Application'
WebUI.openBrowser("https://www.mapclub.com/", FailureHandling.STOP_ON_FAILURE)

'Maximize Browser'
WebUI.maximizeWindow(FailureHandling.STOP_ON_FAILURE)

'Verify Map Club Image is Appear'
WebUI.verifyElementPresent(findTestObject('Object Repository/Map Club/Image_Map_Club'), 0, FailureHandling.STOP_ON_FAILURE)

'Search For Product Iphone 15 Pro'
WebUI.setText(findTestObject('Object Repository/Map Club/Browse_Product'), "Iphone 15 Pro", FailureHandling.STOP_ON_FAILURE)

'Press Enter'
WebUI.sendKeys(findTestObject('Object Repository/Map Club/Browse_Product'), Keys.chord(Keys.ENTER))

'Verify Header Product Searched'
WebUI.verifyElementText(findTestObject('Object Repository/Map Club/Header_Product_Searched'), "Hasil Pencarian untuk “Iphone 15 Pro”", FailureHandling.STOP_ON_FAILURE)

browseMapClub = findTestObject('Object Repository/Map Club/Get List Product Map Club')

'Response Body Auth'
response_body = WS.sendRequest(browseMapClub)
WS.comment('response body ' + response_body.getResponseText())

'Validate Status Code'
 WS.verifyResponseStatusCode(response_body, 200)

'Get Parsing Data'
//JsonSlurper slurper = new JsonSlurper()
parsedJson = slurper.parseText(response_body.getResponseText())

def productsMapClub = parsedJson.data[0].products

productsMapClub.take(5).each { product ->
	def productName = product.productName
	def salePriceLabel = Integer.parseInt(CustomKeywords.'mapClub.Regex.extractInt'(product.salePriceLabel))
	def pageUrl = product.pageUrl
	
	def newProduct = new Product(
		productName,
		salePriceLabel,
		pageUrl,
		"MAP CLUB"
	)
	productList.add(newProduct)
}

"========================================= Sorting Ascending All Product ============================================="
// Sort the productList by salePrice in ascending order
productList = productList.sort { a, b -> a.salePrice <=> b.salePrice }

// Output or further processing
productList.each { println it }

WebUI.closeBrowser()