package steps;

import base.BaseTest;
import com.thoughtworks.gauge.Step;
import model.ElementInfo;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseSteps extends BaseTest {


    public static int DEFAULT_MAX_ITERATION_COUNT = 150;
    public static int DEFAULT_MILLISECOND_WAIT_AMOUNT = 100;

    private static String SAVED_ATTRIBUTE;

    private String compareText;

    public BaseSteps() {
        initMap(getFileList());
    }

    WebElement findElement(String key) {
        By infoParam = getElementInfoToBy(findElementInfoByKey(key));
        WebDriverWait webDriverWait = new WebDriverWait(driver, 60);
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }

    List<WebElement> findElements(String key) {
        return driver.findElements(getElementInfoToBy(findElementInfoByKey(key)));
    }

    public By getElementInfoToBy(ElementInfo elementInfo) {
        By by = null;
        if (elementInfo.getType().equals("css")) {
            by = By.cssSelector(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("name"))) {
            by = By.name(elementInfo.getValue());
        } else if (elementInfo.getType().equals("id")) {
            by = By.id(elementInfo.getValue());
        } else if (elementInfo.getType().equals("xpath")) {
            by = By.xpath(elementInfo.getValue());
        } else if (elementInfo.getType().equals("linkText")) {
            by = By.linkText(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("partialLinkText"))) {
            by = By.partialLinkText(elementInfo.getValue());
        }
        return by;
    }

    private void clickElement(WebElement element) {
        element.click();
    }

    private void clickElementBy(String key) {
        findElement(key).click();
    }

    private void hoverElement(WebElement element) {
        actions.moveToElement(element).build().perform();
    }

    private void hoverElementBy(String key) {
        WebElement webElement = findElement(key);
        actions.moveToElement(webElement).build().perform();
    }

    private void sendKeyESC(String key) {
        findElement(key).sendKeys(Keys.ESCAPE);

    }

    private boolean isDisplayed(WebElement element) {
        return element.isDisplayed();
    }

    private boolean isDisplayedBy(By by) {
        return driver.findElement(by).isDisplayed();
    }

    private String getPageSource() {
        return driver.switchTo().alert().getText();
    }

    public static String getSavedAttribute() {
        return SAVED_ATTRIBUTE;
    }

    public String randomString(int stringLength) {

        Random random = new Random();
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUWVXYZabcdefghijklmnopqrstuwvxyz0123456789".toCharArray();
        String stringRandom = "";
        for (int i = 0; i < stringLength; i++) {

            stringRandom = stringRandom + chars[random.nextInt(chars.length)];
        }

        return stringRandom;
    }

    public WebElement findElementWithKey(String key) {
        return findElement(key);
    }

    public String getElementText(String key) {
        return findElement(key).getText();
    }

    public String getElementAttributeValue(String key, String attribute) {
        return findElement(key).getAttribute(attribute);
    }

    @Step("Print page source")
    public void printPageSource() {
        System.out.println(getPageSource());
    }

    public void javaScriptClicker(WebDriver driver, WebElement element) {

        JavascriptExecutor jse = ((JavascriptExecutor) driver);
        jse.executeScript("var evt = document.createEvent('MouseEvents');"
                + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
                + "arguments[0].dispatchEvent(evt);", element);
    }

    public void javascriptclicker(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    @Step({"Wait <value> seconds",
            "<int> saniye bekle"})
    public void waitBySeconds(int seconds) {
        try {
            logger.info(seconds + " saniye bekleniyor.");
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step({"Wait <value> milliseconds",
            "<long> milisaniye bekle"})
    public void waitByMilliSeconds(long milliseconds) {
        try {
            logger.info(milliseconds + " milisaniye bekleniyor.");
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    WebElement findElementByClickable(String key) {
        By infoParam = getElementInfoToBy(findElementInfoByKey(key));
        WebDriverWait webDriverWait = new WebDriverWait(driver, 60);
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.elementToBeClickable(infoParam));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        System.out.println("element bulundu");
        return webElement;
    }
    @Step({"Click to element <key>",
            "Elementine tıkla <key>"})
    public void clickElement(String key) {
        By infoParam = getElementInfoToBy(findElementInfoByKey(key));

        WebDriverWait webDriverWait = new WebDriverWait(driver, 60);
        if (!key.isEmpty()) {
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(infoParam));
            clickElement(findElementByClickable(key));
            logger.info(key + " elementine tıklandı.");
        }
    }


    @Step("<key> elementin üstünde bekle")
    public void hover(String key) {
        hoverElement(findElement(key));
    }

    @Step({"Click to element <key> with focus",
            "<key> elementine focus ile tıkla"})
    public void clickElementWithFocus(String key) {
        actions.moveToElement(findElement(key));
        actions.click();
        actions.build().perform();
        logger.info(key + " elementine focus ile tıklandı.");
    }


    @Step({"<key> elementini kontrol et", "check <key> element is exist"})
    public void checkElement(String key) {
        assertTrue(findElement(key).isDisplayed(), "Aranan element bulunamadı");
    }

    @Step("<key> elementini kontrol et yoksa <key2> devam et")
    public void checkElementAndContinue(String key, String key2) {
        logger.info("checkElementAndContinue");
        try {
            findElement(key).click();
        } catch (Exception e) {
            logger.info("Catch");
            assertTrue(findElement(key2).isDisplayed(), "Aranan element bulunamadı");
        }

    }

    @Step({"Go to <url> address",
            "<url> adresine git"})
    public void goToUrl(String url) {
        driver.get(url);
        logger.info(url + " adresine gidiliyor.");
    }

    @Step({"Wait for element to load with css <css>",
            "Elementin yüklenmesini bekle css <css>"})
    public void waitElementLoadWithCss(String css) {
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (driver.findElements(By.cssSelector(css)).size() > 0) {
                logger.info(css + " elementi bulundu.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element: '" + css + "' doesn't exist.");
    }

    @Step({"Wait for element to load with xpath <xpath>",
            "Elementinin yüklenmesini bekle xpath <xpath>"})
    public void waitElementLoadWithXpath(String xpath) {
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (driver.findElements(By.xpath(xpath)).size() > 0) {
                logger.info(xpath + " elementi bulundu.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element: '" + xpath + "' doesn't exist.");
    }

    @Step({"Check if element <key> exists else print message <message>",
            "Element <key> var mı kontrol et yoksa hata mesajı ver <message>"})
    public void getElementWithKeyIfExistsWithMessage(String key, String message) {
        ElementInfo elementInfo = findElementInfoByKey(key);
        By by = getElementInfoToBy(elementInfo);

        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (driver.findElements(by).size() > 0) {
                logger.info(key + " elementi bulundu.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail(message);
    }

    @Step({"Check if element <key> not exists",
            "Element yok mu kontrol et <key>"})
    public void checkElementNotExists(String key) {
        ElementInfo elementInfo = findElementInfoByKey(key);
        By by = getElementInfoToBy(elementInfo);

        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (driver.findElements(by).size() == 0) {
                logger.info(key + " elementinin olmadığı kontrol edildi.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element '" + key + "' still exist.");
    }

    @Step({"Upload file in project <path> to element <key>",
            "Proje içindeki <path> dosyayı <key> elemente upload et"})
    public void uploadFile(String path, String key) {
        String pathString = System.getProperty("user.dir") + "/";
        pathString = pathString + path;
        findElement(key).sendKeys(pathString);
        logger.info(path + " dosyası " + key + " elementine yüklendi.");
    }

    @Step({"Write value <text> to element <key>",
            "<text> textini <key> elemente yaz"})
    public void ssendKeys(String text, String key) {
        if (!key.equals("")) {
            findElement(key).sendKeys(text);
            logger.info(key + " elementine " + text + " texti yazıldı.");
        }
    }

    @Step({"Click with javascript to css <css>",
            "Javascript ile css tıkla <css>"})
    public void javascriptClickerWithCss(String css) {
        assertTrue(isDisplayedBy(By.cssSelector(css)), "Element bulunamadı");
        javaScriptClicker(driver, driver.findElement(By.cssSelector(css)));
        logger.info("Javascript ile " + css + " tıklandı.");
    }

    @Step({"Click with javascript to xpath <xpath>",
            "Javascript ile xpath tıkla <xpath>"})
    public void javascriptClickerWithXpath(String xpath) {
        assertTrue(isDisplayedBy(By.xpath(xpath)), "Element bulunamadı");
        javaScriptClicker(driver, driver.findElement(By.xpath(xpath)));
        logger.info("Javascript ile " + xpath + " tıklandı.");
    }

    @Step({"Check if current URL contains the value <expectedURL>",
            "Şuanki URL <url> değerini içeriyor mu kontrol et"})
    public void checkURLContainsRepeat(String expectedURL) {
        int loopCount = 0;
        String actualURL = "";
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            actualURL = driver.getCurrentUrl();

            if (actualURL != null && actualURL.contains(expectedURL)) {
                logger.info("Şuanki URL" + expectedURL + " değerini içeriyor.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail(
                "Actual URL doesn't match the expected." + "Expected: " + expectedURL + ", Actual: "
                        + actualURL);
    }

    @Step({"Send TAB key to element <key>",
            "Elemente TAB keyi yolla <key>"})
    public void sendKeyToElementTAB(String key) {
        findElement(key).sendKeys(Keys.TAB);
        logger.info(key + " elementine TAB keyi yollandı.");
    }

    @Step({"Send BACKSPACE key to element <key>",
            "Elemente BACKSPACE keyi yolla <key>"})
    public void sendKeyToElementBACKSPACE(String key) {
        findElement(key).sendKeys(Keys.BACK_SPACE);
        logger.info(key + " elementine BACKSPACE keyi yollandı.");
    }
    @Step("Send down key to element with <key>")

    public void sendDownKey(String key) {
        WebElement element = findElement(key);
        element.sendKeys(Keys.DOWN);
        logger.info("Sent DOWN key to element: " + key);
    }

    @Step("Send down key to the screen")
    public void sendDownKeyToScreen() {
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.DOWN).perform();
        logger.info("Sent DOWN key to the screen.");
    }

    @Step("Send down key to the screen 40 times")
    public void sendDownKey40ToScreen() {
        Actions actions = new Actions(driver);
        for (int i = 0; i < 40; i++) {
            actions.sendKeys(Keys.DOWN).perform();
        }
        logger.info("Sent DOWN key to the screen 40 times.");
    }

    @Step("Send down key to the screen 4 times")
    public void sendDownKey4ToScreen() {
        Actions actions = new Actions(driver);
        for (int i = 0; i < 4; i++) {
            actions.sendKeys(Keys.DOWN).perform();
        }
        logger.info("Sent DOWN key to the screen 40 times.");
    }



    @Step({"Send ESCAPE key to element <key>",
            "Elemente ESCAPE keyi yolla <key>"})
    public void sendKeyToElementESCAPE(String key) {
        findElement(key).sendKeys(Keys.ESCAPE);
        logger.info(key + " elementine ESCAPE keyi yollandı.");
    }

    @Step({"Check if element <key> has attribute <attribute>",
            "<key> elementi <attribute> niteliğine sahip mi"})
    public void checkElementAttributeExists(String key, String attribute) {
        WebElement element = findElement(key);
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (element.getAttribute(attribute) != null) {
                logger.info(key + " elementi " + attribute + " niteliğine sahip.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element DOESN't have the attribute: '" + attribute + "'");
    }

    @Step({"Check if element <key> not have attribute <attribute>",
            "<key> elementi <attribute> niteliğine sahip değil mi"})
    public void checkElementAttributeNotExists(String key, String attribute) {
        WebElement element = findElement(key);

        int loopCount = 0;

        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (element.getAttribute(attribute) == null) {
                logger.info(key + " elementi " + attribute + " niteliğine sahip olmadığı kontrol edildi.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element STILL have the attribute: '" + attribute + "'");
    }

    @Step({"Check if <key> element's attribute <attribute> equals to the value <expectedValue>",
            "<key> elementinin <attribute> niteliği <value> değerine sahip mi"})
    public void checkElementAttributeEquals(String key, String attribute, String expectedValue) {
        WebElement element = findElement(key);

        String actualValue;
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            actualValue = element.getAttribute(attribute).trim();
            if (actualValue.equals(expectedValue)) {
                logger.info(
                        key + " elementinin " + attribute + " niteliği " + expectedValue + " değerine sahip.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element's attribute value doesn't match expected value");
    }

    @Step({"Check if <key> element's attribute <attribute> contains the value <expectedValue>",
            "<key> elementinin <attribute> niteliği <value> değerini içeriyor mu"})
    public void checkElementAttributeContains(String key, String attribute, String expectedValue) {
        WebElement element = findElement(key);

        String actualValue;
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            actualValue = element.getAttribute(attribute).trim();
            if (actualValue.contains(expectedValue)) {
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element's attribute value doesn't contain expected value");
    }

    @Step({"Write <value> to <attributeName> of element <key>",
            "<value> değerini <attribute> niteliğine <key> elementi için yaz"})
    public void setElementAttribute(String value, String attributeName, String key) {
        String attributeValue = findElement(key).getAttribute(attributeName);
        findElement(key).sendKeys(attributeValue, value);
    }

    @Step({"Write <value> to <attributeName> of element <key> with Js",
            "<value> değerini <attribute> niteliğine <key> elementi için JS ile yaz"})
    public void setElementAttributeWithJs(String value, String attributeName, String key) {
        WebElement webElement = findElement(key);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('" + attributeName + "', '" + value + "')",
                webElement);
    }

    @Step({"Clear text of element <key>",
            "<key> elementinin text alanını temizle"})
    public void clearInputArea(String key) {
        findElement(key).clear();
    }

    @Step("<text> pathi hafızada tut")
    public void keepPathInMemory(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    @Step("Yapıştır ve gönder")
    public void pasteAndSendEnter() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }

    @Step({"Clear text of element <key> with BACKSPACE",
            "<key> elementinin text alanını BACKSPACE ile temizle"})
    public void clearInputAreaWithBackspace(String key) {
        WebElement element = findElement(key);
        element.clear();
        element.sendKeys("a");
        actions.sendKeys(Keys.BACK_SPACE).build().perform();
    }

    @Step({"Save attribute <attribute> value of element <key>",
            "<attribute> niteliğini sakla <key> elementi için"})
    public void saveAttributeValueOfElement(String attribute, String key) {
        SAVED_ATTRIBUTE = findElement(key).getAttribute(attribute);
        System.out.println("Saved attribute value is: " + SAVED_ATTRIBUTE);
    }

    @Step({"Write saved attribute value to element <key>",
            "Kaydedilmiş niteliği <key> elementine yaz"})
    public void writeSavedAttributeToElement(String key) {
        findElement(key).sendKeys(SAVED_ATTRIBUTE);
    }

    @Step({"Check if element <key> contains text <expectedText>",
            "<key> elementi <text> değerini içeriyor mu kontrol et"})
    public void checkElementContainsText(String key, String expectedText) {

        Boolean containsText = findElement(key).getText().contains(expectedText);
        assertTrue(containsText, "Expected text is not contained");
        logger.info(key + " elementi" + expectedText + "değerini içeriyor.");
    }

    @Step({"Write random value to element <key>",
            "<key> elementine random değer yaz"})
    public void writeRandomValueToElement(String key) {
        findElement(key).sendKeys(randomString(15));
    }

    @Step({"Write random value to element <key> starting with <text>",
            "<key> elementine <text> değeri ile başlayan random değer yaz"})
    public void writeRandomValueToElement(String key, String startingText) {
        String randomText = startingText + randomString(15);
        findElement(key).sendKeys(randomText);
    }

    @Step({"Print element text by css <css>",
            "Elementin text değerini yazdır css <css>"})
    public void printElementText(String css) {
        System.out.println(driver.findElement(By.cssSelector(css)).getText());
    }

    @Step({"Write value <string> to element <key> with focus",
            "<string> değerini <key> elementine focus ile yaz"})
    public void sendKeysWithFocus(String text, String key) {
        actions.moveToElement(findElement(key));
        actions.click();
        actions.sendKeys(text);
        actions.build().perform();
        logger.info(key + " elementine " + text + " değeri focus ile yazıldı.");
    }

    @Step({"Refresh page",
            "Sayfayı yenile"})
    public void refreshPage() {
        driver.navigate().refresh();
    }


    @Step({"Change page zoom to <value>%",
            "Sayfanın zoom değerini değiştir <value>%"})
    public void chromeZoomOut(String value) {
        JavascriptExecutor jsExec = (JavascriptExecutor) driver;
        jsExec.executeScript("document.body.style.zoom = '" + value + "%'");
    }

    @Step({"Open new tab",
            "Yeni sekme aç"})
    public void chromeOpenNewTab() {
        ((JavascriptExecutor) driver).executeScript("window.open()");
    }

    @Step({"Focus on tab number <number>",
            "<number> numaralı sekmeye odaklan"})//Starting from 1
    public void chromeFocusTabWithNumber(int number) {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(number - 1));
    }

    @Step("popupa gec")
    public void switchTo() {
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
    }

    @Step({"Focus on last tab",
            "Son sekmeye odaklan"})
    public void chromeFocusLastTab() {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));
    }

    @Step({"Focus on frame with <key>",
            "Frame'e odaklan <key>"})
    public void chromeFocusFrameWithNumber(String key) {
        WebElement webElement = findElement(key);
        driver.switchTo().frame(webElement);
    }

    @Step({"Accept Chrome alert popup",
            "Chrome uyarı popup'ını kabul et"})
    public void acceptChromeAlertPopup() {
        driver.switchTo().alert().accept();
    }


    //----------------------SONRADAN YAZILANLAR-----------------------------------\\


    // Key değeri alınan listeden rasgele element seçme amacıyla yazılmıştır. @Mehmetİnan
    public void randomPick(String key) {
        List<WebElement> elements = findElements(key);
        Random random = new Random();
        int index = random.nextInt(elements.size());
        elements.get(index).click();
    }

    //Javascript driverın başlatılması
    private JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) driver;
    }

    //Javascript scriptlerinin çalışması için gerekli fonksiyon
    private Object executeJS(String script, boolean wait) {
        return wait ? getJSExecutor().executeScript(script, "") : getJSExecutor().executeAsyncScript(script, "");
    }

    //Belirli bir locasyona sayfanın kaydırılması
    private void scrollTo(int x, int y) {
        String script = String.format("window.scrollTo(%d, %d);", x, y);
        executeJS(script, true);
    }

    //"Belirli bir elementin <key> olduğu web sayfasının kaydırılması"
    public WebElement scrollToElementToBeVisible(String key) {
        ElementInfo elementInfo = findElementInfoByKey(key);
        WebElement webElement = driver.findElement(getElementInfoToBy(elementInfo));
        if (webElement != null) {
            scrollTo(webElement.getLocation().getX(), webElement.getLocation().getY() - 100);
        }
        return webElement;
    }


    @Step({"<key> alanına kaydır"})
    public void scrollToElement(String key) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 60);
        By infoParam = getElementInfoToBy(findElementInfoByKey(key));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(infoParam));
        scrollToElementToBeVisible(key);
        logger.info(key + " elementinin olduğu alana kaydırıldı");

    }


    @Step({"<key> alanına js ile kaydır"})
    public void scrollToElementWithJs(String key) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 60);
        By infoParam = getElementInfoToBy(findElementInfoByKey(key));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(infoParam));

        ElementInfo elementInfo = findElementInfoByKey(key);
        WebElement element = driver.findElement(getElementInfoToBy(elementInfo));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }


    @Step({"<length> uzunlugunda random bir kelime üret ve <saveKey> olarak sakla"})
    public void createRandomString(int length, String saveKey) {
        saveValue(saveKey, randomString(length));

    }

    @Step({"<key> li elementi bul ve değerini <saveKey> saklanan degeri yazdir",
            "Find element by <key> and compare saved key <saveKey>"})
    public void equalsSendTextByKey(String key, String saveKey) throws InterruptedException {
        WebElement element;
        int waitVar = 0;
        element = findElementWithKey(key);
        while (true) {
            if (element.isDisplayed()) {
                logger.info("WebElement is found at: " + waitVar + " second.");
                element.clear();
                getValue(saveKey);
                element.sendKeys(getValue(saveKey));

                break;
            } else {
                waitVar = waitVar + 1;
                Thread.sleep(1000);
                if (waitVar == 20) {
                    throw new NullPointerException(String.format("by = %s Web element list not found"));
                } else {
                }
            }
        }
    }

    //Zaman bilgisinin alınması
    private Long getTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return (timestamp.getTime());
    }

    @Step({"<key> li elementi bul, temizle ve rasgele  email değerini yaz",
            "Find element by <key> clear and send keys  random email"})
    public void RandomMail(String key) {
        Long timestamp = getTimestamp();
        WebElement webElement = findElementWithKey(key);
        webElement.clear();
        webElement.sendKeys("testotomasyon" + timestamp + "@sahabt.com");

    }

    @Step("<key> olarak <text> seçersem")
    public void choosingTextFromList(String key, String text) throws InterruptedException {
        List<WebElement> comboBoxElement = findElements(key);
        for (int i = 0; i < comboBoxElement.size(); i++) {
            String texts = comboBoxElement.get(i).getText();
            String textim = text;
            if (texts.contains(textim)) {
                comboBoxElement.get(i).click();
                break;
            }
        }
        logger.info(key + " comboboxından " + text + " değeri seçildi");


    }

    @Step("<key> olarak comboboxdan bir değer seçilir")
    public void comboBoxRandom(String key) throws InterruptedException {

        List<WebElement> comboBoxElement = findElements(key);
        int randomIndex = new Random().nextInt(comboBoxElement.size());
        Thread.sleep(2000);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", comboBoxElement.get(randomIndex));
        logger.info(key + " comboboxından herhangi bir değer seçildi");

    }


    @Step("<key> elementine javascript ile tıkla")
    public void clickToElementWithJavaScript(String key) {
        WebElement element = findElement(key);
        javascriptclicker(element);
        logger.info(key + " elementine javascript ile tıklandı");
    }


    @Step("Belirli bir <key> değerinin olduğu locasyona websayfasının kaydırılması")
    public void scrollToElementToBeVisiblest(WebElement webElement) {
        if (webElement != null) {
            scrollTo(webElement.getLocation().getX(), webElement.getLocation().getY() - 100);
        }
    }


    //Çift tıklama fonksiyonu
    public void doubleclick(WebElement elementLocator) {
        Actions actions = new Actions(driver);
        actions.doubleClick(elementLocator).perform();
    }

    @Step("<key> alanını javascript ile temizle")
    public void clearWithJS(String key) {
        WebElement element = findElement(key);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value ='';", element);
    }


    @Step("<key> elementleri arasından <text> kayıtlı değişkene tıkla")
    public void clickParticularElement(String key, String text) {
        List<WebElement> anchors = findElements(key);
        Iterator<WebElement> i = anchors.iterator();
        while (i.hasNext()) {
            WebElement anchor = i.next();
            if (anchor.getText().contains(getValue(text))) {
                scrollToElementToBeVisiblest(anchor);
                doubleclick(anchor);
                break;
            }
        }
    }

    @Step("<key> menu listesinden rasgele seç")
    public void chooseRandomElementFromList(String key) {
        for (int i = 0; i < 3; i++)
            randomPick(key);
    }


    @Step("<key> olarak <index> indexi seçersem")
    public void choosingIndexFromDemandNo(String key, String index) {

        try {
            TimeUnit.SECONDS.sleep(3);

            List<WebElement> anchors = findElements(key);
            WebElement anchor = anchors.get(Integer.parseInt(index));
            anchor.click();
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Step("Siparis durmununu <kartDurumu> elementinden bul")
    public void findOrderStatus(String kartDurumu) throws InterruptedException {
        WebElement webElement = findElement(kartDurumu);
        logger.info(" webelement bulundu");
        compareText = webElement.getText();
        logger.info(compareText + " texti bulundu");
    }

    @Step("<key> elementiyle karsilastir")
    public void compareOrderStatus(String key) throws InterruptedException {
        WebElement cardDetail = findElement(key);
        String supplyDetailStatus = cardDetail.getText();
        logger.info(supplyDetailStatus + " texti bulundu");
        assertTrue(compareText.equals(supplyDetailStatus));
        logger.info(compareText + " textiyle " + supplyDetailStatus + " texti karşılaştırıldı.");
    }

    @Step("<text> textini <key> elemente tek tek yaz")
    public void sendKeyOneByOne(String text, String key) throws InterruptedException {

        WebElement field = findElement(key);
        field.clear();
        if (!key.equals("")) {
            for (char ch : text.toCharArray())
                findElement(key).sendKeys(Character.toString(ch));
            Thread.sleep(10);
            logger.info(key + " elementine " + text + " texti karakterler tek tek girlilerek yazıldı.");
        }
    }

    @Step("<key> elementine <text> değerini js ile yaz")
    public void writeToKeyWithJavaScript(String key, String text) {
        WebElement element = findElement(key);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value=arguments[1]", element, text);
        logger.info(key + " elementine " + text + " değeri js ile yazıldı.");
    }


    //Bugünün Tarihinin seçilmesi
    public String chooseDate() {
        Calendar now = Calendar.getInstance();
        int tarih = now.get(Calendar.DATE) + 2;
        return String.valueOf(tarih);
    }

    @Step("<key> tarihinden 2 gün sonraya al")
    public void chooseTwoDaysFromNow(String key) {
        List<WebElement> elements = findElements(key);
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getText().equals(chooseDate())) {
                elements.get(i).click();
            }
        }
    }

    @Step("<variable> değişkenini <key> elementine yaz")
    public void sendKeysVariable(String variable, String key) {
        if (!key.equals("")) {
            clearInputArea(key);
            findElement(key).sendKeys(getValue(variable));
            logger.info(key + " elementine " + getValue(variable) + " texti yazıldı.");
        }
    }


    @Step("<key> olarak comboboxtan <text> seçimini yap")
    public void selectDropDown(String key, String text) {
        Select drpCountry = new Select(findElement(key));
        drpCountry.selectByVisibleText(text);
    }

    @Step("<key> olarak seçimini yap")
    public void randomPick2(String key) {
        List<WebElement> elements = findElements(key);
        Random random = new Random();
        int index = random.nextInt(elements.size());
        elements.get(index).click();
    }


    @Step("Rastgele Tarih ve Gun Sec")
    public void selectRandomDateSlot() {
        Random random = new Random();

        // Select a random day
        List<WebElement> days = driver.findElements(By.xpath("//div[contains(@class, 'date-item')]"));
        WebElement randomDay = days.get(random.nextInt(days.size()));
        randomDay.click();

        // Wait for the time slots to load (if necessary)
        try {
            Thread.sleep(2000); // Adjust the sleep time according to your application's loading time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Select a random time slot
        List<WebElement> timeSlots = driver.findElements(By.xpath("//button[contains(@class, 'slot-active')]"));
        WebElement randomTimeSlot = timeSlots.get(random.nextInt(timeSlots.size()));
        randomTimeSlot.click();
    }

    @Step("Rastgele tarih sec")
    public void selectRandomDateAndTimeSlot() {
        Random random = new Random();
        List<WebElement> days = driver.findElements(By.xpath("//div[contains(@class, 'date-item') and not(contains(@class, 'slot-passive'))]"));

        // Ensure we start from the next available day (assuming the first day is the current day)
        int startIndex = 1;
        for (int i = startIndex; i < days.size(); i++) {
            WebElement day = days.get(i);
            day.click();

            // Wait for the time slots to load (if necessary)
            try {
                Thread.sleep(2000); // Adjust the sleep time according to your application's loading time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Check if "Aktif Slot Bulunmamaktadır" message is displayed
            List<WebElement> noActiveSlotMessage = driver.findElements(By.xpath("//h6[@class='text-xl text-neutral-600' and contains(text(), 'Aktif Slot Bulunmamaktadır')]"));
            if (noActiveSlotMessage.isEmpty()) {
                // Select a random active time slot
                List<WebElement> timeSlots = driver.findElements(By.xpath("//button[contains(@class, 'slot-active')]"));
                if (!timeSlots.isEmpty()) {
                    WebElement randomTimeSlot = timeSlots.get(random.nextInt(timeSlots.size()));
                    randomTimeSlot.click();
                    return; // Exit the method after selecting a slot
                }
            }
        }
        throw new RuntimeException("No available days or time slots to select.");
    }
    @Step("Hayır secenegini temizle ve tekrar sec")
    public void clearAndSelectNoOption() {
        // XPath ifadesi ile elemanı bul
        WebElement noOption = driver.findElement(By.xpath("//ul[contains(@class, 'options')]//li[text()='Hayır']\n"));

        // Hayır seçeneği seçili mi kontrol et
        if (noOption.getAttribute("class").contains("option selected")) {
            // Seçimi kaldırmak için tıklayın
            noOption.click();
        }

        // Yeniden Hayır seçeneğini seçmek için tıklayın
        noOption.click();

        // Loglama
        logger.info("Hayır secenegi temizlendi ve tekrar secildi.");
    }


    @Step("Hayır seçeneği zaten seçili değilse seç")
    public void hayirSeceneginiSec() {
        // 'Hayır' seçeneğini XPath kullanarak bulun
        WebElement hayirSecenegi = driver.findElement(By.xpath("//li[@class='option selected']"));

        // 'Hayır' seçeneğinin zaten seçili olup olmadığını kontrol edin
        boolean seciliMi = hayirSecenegi.getAttribute("class").contains("selected");

        // Eğer seçili değilse, seçeneğe tıklayın
        if (!seciliMi) {
            hayirSecenegi.click();
        }

        // Seçeneğin tıklamadan sonra seçili olduğunu doğrulayın
        logger.info("Hayır seçeneği seçilmiş olmalıdır");
    }

    @Step("Kilo alanını temizle ve 70 değerini gir")
    public void enterWeightValue() {
        // XPath ifadesi ile elemanı bul
        WebElement weightInput = driver.findElement(By.xpath("//input[@type='number' and @value='165']"));

        // Alanı temizle
        weightInput.clear();

        // Yeni değeri gir
        weightInput.sendKeys("70");

        // Loglama
        logger.info("Kilo alanı temizlendi ve 70 değeri girildi.");
    }

    @Step({"Gorunur olmasini bekle <key>"})
    public void gorunurBekle(String key){
        By infoParam = getElementInfoToBy(findElementInfoByKey(key));
        WebDriverWait webDriverWait = new WebDriverWait(driver, 60);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(infoParam));
    }

    
    @Step("Rastgele bir ilaç kullanım saati seç")
    public void rastgeleSaatSec() {
        // 'İlaç Kullanım Saati' select control alanını bul
        WebElement saatDropdown = driver.findElement(By.xpath("(//div[contains(@class, 'select__dropdown-indicator')])[2]"));

        // Dropdown menüsüne tıklayarak seçenekleri görünür hale getir
        saatDropdown.click();

        // Tüm saat seçeneklerini bul
        List<WebElement> saatSecenekleri = driver.findElements(By.xpath("//div[contains(@class, 'select__menu')]//div[contains(@class, 'select__option')]"));

        // Random sınıfını kullanarak rastgele bir seçenek seç
        Random random = new Random();
        int rastgeleIndex = random.nextInt(saatSecenekleri.size());

        // Rastgele seçilen saati tıkla
        saatSecenekleri.get(rastgeleIndex).click();
    }

    @Step("Rastgele bir ilk tarih sec")
    public void rastgeleTarihSec() {
        // Temmuz ayını seç
        WebElement monthDropdown = driver.findElement(By.xpath("//select[@class='flatpickr-monthDropdown-months']"));
        monthDropdown.click();
        WebElement julyOption = driver.findElement(By.xpath("//option[@value='6']"));
        julyOption.click();

        // Yılı seç
        WebElement yearInput = driver.findElement(By.xpath("//input[@class='numInput cur-year']"));
        yearInput.clear();
        yearInput.sendKeys("2024");

        // Günleri seç
        List<WebElement> dayElements = driver.findElements(By.xpath("/html/body/div[2]/div[2]/div/div[2]/div"));

        // Rastgele bir gün seç
        Random rand = new Random();
        int randomDayIndex = rand.nextInt(dayElements.size());
        WebElement randomDay = dayElements.get(randomDayIndex);
        randomDay.click();
    }


    @Step("Scroll down and Download")
    public void scrollAndClickIndirButton() {
        // Locate the scroll bar element
        WebElement scrollBar = driver.findElement(By.cssSelector(".ps__thumb-y"));

        // Set the 'top' and 'height' values
        int top = 142;
        int height = 47;

        // Calculate the scroll distance
        int scrollDistance = top + height;

        // Scroll using JavaScript
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].scrollTop = " + scrollDistance, scrollBar);

        // Wait for the scrolling to finish
        try {
            Thread.sleep(1000); // Simple wait for 1 second, you can replace it with explicit wait if necessary
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Locate the Indir button
        WebElement indirButton = driver.findElement(By.xpath("//a[@class='mx-auto mt-auto btn btn-gradient-primary']"));

        // Click the Indir button
        indirButton.click();
    }


    @Step("Click on the X button to close screen")
    public void clickCloseButton() {
        // Locate the close (X) button
        WebElement closeButton = driver.findElement(By.cssSelector("button.btn-close"));

        // Click the close button
        closeButton.click();
    }
    @Step("Click on the X button to close frame")
    public void clickClosexButton() {
        // Locate the close (X) button
        WebElement closeButton = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div/div[2]/div[2]/div[3]/div[2]/div[1]/button/svg"));
        // Click the close button
        closeButton.click();
    }

    @Step("Scroll to the bottom of the page")
    public void scrollToBottom() {
        // Scroll to the bottom using JavaScript
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");

        // Wait for the scrolling to finish
        try {
            Thread.sleep(1000); // Simple wait for 1 second, you can replace it with an explicit wait if necessary
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step("Filtreleme alanında 'Amerikan Hastanesi'ni sec")
    public void selectAmericanHospitalFromDropdown() throws InterruptedException {
        // Locate the hospital dropdown container
        WebElement hospitalDropdownContainer = driver.findElement(By.xpath("//div[contains(@class, 'select__value-container')]"));

        // Click the dropdown to display options
        hospitalDropdownContainer.click();

        // Wait for options to be visible
        Thread.sleep(2000); // 2 seconds wait

        // Locate the option 'Amerikan Hastanesi' and click it
        WebElement americanHospitalOption = driver.findElement(By.xpath("//div[contains(@class, 'select__option') and text()='Amerikan Hastanesi']"));
        americanHospitalOption.click();

        // Verification (Optional)
        String selectedOption = hospitalDropdownContainer.getText();
        if (selectedOption.equals("Amerikan Hastanesi")) {
            System.out.println("Amerikan Hastanesi has been selected successfully.");
        } else {
            System.err.println("Failed to select Amerikan Hastanesi.");
        }
    }

    @Step("Filtreleme alanında Secim yapmadan devam et sec")
    public void selectHospitalInsuranceFromDropdown() throws InterruptedException {
        // Locate the hospital dropdown container
        WebElement hospitalInsuranceDropdownContainer = driver.findElement(By.xpath("//div[contains(@class, 'select__value-container')]"));

        // Click the dropdown to display options
        hospitalInsuranceDropdownContainer.click();

        // Wait for options to be visible
        Thread.sleep(2000); // 2 seconds wait

        // Locate the option 'Seçim Yapmadan Devam Et' and click it
        WebElement americanInsuranceHospitalOption = driver.findElement(By.xpath("//div[contains(@class, 'select__option') and text()='Seçim Yapmadan Devam Et']"));
        americanInsuranceHospitalOption.click();

        // Verification (Optional)
        String selectedOption = hospitalInsuranceDropdownContainer.getText();
        if (selectedOption.equals("Seçim Yapmadan Devam Et")) {
            System.out.println("Seçim Yapmadan Devam Et has been selected successfully.");
        } else {
            System.err.println("Failed to select Seçim Yapmadan Devam Et.");
        }
    }


    @Step("Close the currently opened tab")
    public void closeCurrentTab() {
        String originalHandle = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (!handle.equals(originalHandle)) {
                driver.close();
                logger.info("Closed the newly opened tab.");
                break;
            }
        }
        driver.switchTo().window(originalHandle);
    }
    @Step("Close the focused tab")
    public void closeFocusedTab() {
        String currentHandle = driver.getWindowHandle();
        driver.close();
        logger.info("Closed the focused tab.");
        // Switch to the first tab if there are any other tabs left open
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        if (!tabs.isEmpty()) {
            driver.switchTo().window(tabs.get(0));
        }
    }
}





