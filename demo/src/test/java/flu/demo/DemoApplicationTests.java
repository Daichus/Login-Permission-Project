package flu.demo;

import login.permission.project.classes.DemoApplication;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {
	static WebDriver driver;

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

	@Test
	void testLogin() throws InterruptedException {
		String url = "http://localhost:5173/"; // 確認伺服器 URL 是否正確
		driver.get(url);

		System.out.println("當前頁面標題：" + driver.getTitle());
		WebElement employeeIdInput = driver.findElement(By.id("employeeId"));
		employeeIdInput.sendKeys("1");

		WebElement passwordInput = driver.findElement(By.id("password"));
		passwordInput.sendKeys("password123");

		WebElement button = driver.findElement(By.cssSelector(".btn.lp-btn-outline"));
		Thread.sleep(5000); // 等待按鈕渲染
		button.click();

		Thread.sleep(10000); // 等待結果顯示
		System.out.println("測試完成！");
	}

}
