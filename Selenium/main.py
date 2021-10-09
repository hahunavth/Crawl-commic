import os
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

# os.environ["PATH"] += '/usr/local/bin/chromedriver'

driver = webdriver.Chrome()
driver.get("https://www.seleniumeasy.com/test/jquery-download-progress-bar-demo.html")

driver.implicitly_wait(10)

downloadBtn = driver.find_element_by_id("downloadButton")
downloadBtn.click()

# downloadLabel = driver.find_element_by_class_name("progress-label")
# print(f"{downloadLabel.text}")

WebDriverWait(driver, 30).until(
    EC.text_to_be_present_in_element(
        (By.CLASS_NAME, 'progress-label'),
        "Complete!"
    )
)


# closeBtn = driver.find_element_by_css_selector('button[type="button"]')
# closeBtn.click()
