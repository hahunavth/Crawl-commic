from selenium import webdriver

driver = webdriver.Chrome()

driver.get("https://nettruyenpro.com")

driver.implicitly_wait(10)

card_elem = driver.find_element_by_class_name("item")
card_elem.click()