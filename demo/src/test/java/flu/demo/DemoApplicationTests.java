package flu.demo;

import login.permission.project.classes.DemoApplication;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {
	static WebDriver driver;

	/**
	 * setup主要設定是確保伺服器正確啟動以後,才初始化Driver, 並使用selenium進行爬蟲,
	 *  避免出現模擬的前端操作無法與伺服器交互
	 */
	@BeforeAll
	static void setup() throws InterruptedException {
		// 啟動伺服器執行緒
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(() -> {
			System.out.println("啟動 Spring Boot 伺服器...");
			SpringApplication.run(DemoApplication.class); // 啟動伺服器
		});

		// 等待伺服器啟動（根據需要設置等待時間或健康檢查）
		System.out.println("等待伺服器啟動...");
		Thread.sleep(15000); // 等待 15 秒，確保伺服器啟動完成
		System.out.println("伺服器啟動完成，準備測試...");

		// 初始化 WebDriver
		System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
		driver = new ChromeDriver();
	}

	/**
	 * 測試情境：當輸入帳密正確時跳轉頁面
	 * 輸入正確帳密並提交後應跳轉至http://localhost:5173/Home,
	 * 在這裡檢查跳轉後的網址是否正確
	 */
	@Test
	@DisplayName("測試登錄成功後跳轉頁面")
	void testLogin() throws InterruptedException {

		String url = "http://localhost:5173/"; // 確認伺服器 URL 是否正確
		driver.get(url);

		System.out.println("當前頁面標題：" + driver.getTitle());
		WebElement employeeIdInput = driver.findElement(By.id("employeeId"));
		employeeIdInput.sendKeys("1");

		WebElement passwordInput = driver.findElement(By.id("password"));
		passwordInput.sendKeys("password123");

		WebElement button = driver.findElement(By.cssSelector(".btn.lp-btn-outline"));
		Thread.sleep(1000); // 等待按鈕渲染
		button.click();
		Thread.sleep(5000);
		String currentUrl = driver.getCurrentUrl();
		assertEquals("http://localhost:5173/Home", currentUrl);

	}

	/**
	 * 測試情境：當輸入帳密錯誤時的提是
	 * 輸入錯誤帳密並提交後應跳出帳密錯誤提示的div
	 * 獲取該提示內的文本並檢驗是否正確
	 */
	@ParameterizedTest
	@DisplayName("測試登錄失敗的多種情況")
	@CsvSource({
			"1,xxxxxxxxxxx,密碼錯誤",     // 正確帳號，但密碼錯誤
			"999,password123,找不到用戶", // 錯誤帳號，但密碼正確
			"1,,密碼錯誤",               // 正確帳號，但密碼為空
			",password123,找不到用戶",   // 密碼正確，但帳號為空
			",,找不到用戶"               // 帳號和密碼都為空
	})
	void testLoginFail(String employeeId, String password, String expected) throws InterruptedException {
		String url = "http://localhost:5173/";
		driver.get(url);
		WebElement employeeIdInput = driver.findElement(By.id("employeeId"));
		if (employeeId != null) {
			employeeIdInput.clear(); // 確保輸入框為空
			employeeIdInput.sendKeys(employeeId); // 如果有值則輸入
		}
		WebElement passwordInput = driver.findElement(By.id("password"));
		if (password != null) {
			passwordInput.clear(); // 確保輸入框為空
			passwordInput.sendKeys(password); // 如果有值則輸入
		}

		WebElement button = driver.findElement(By.cssSelector(".btn.lp-btn-outline"));
		Thread.sleep(1000);
		button.click();
		Thread.sleep(3000);

		WebElement failMessage = driver.findElement(By.cssSelector(".alert.alert-danger.py-2.text-center.mb-3.fade-in"));

		String errorMessage = failMessage.getText();

		// 驗證
		assertEquals(expected, errorMessage);
	}



}
