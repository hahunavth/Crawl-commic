from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from time import sleep
import requests

# NETTRUYEN
# domain = "https://nettruyenpro.com"
# commic_selector = "jtip"
# # commic page
# title_selector = "title-detail"
# author_selector = ".author > .col-xs-8"
# status_selector = ".status > .col-xs-8"
# kind_selector = ".kind > .col-xs-8"
# view_selector = ".kind + .row .col-xs-8"
# star_selector = "star"
# follow_selector = ".follow > span > b"
# details_selector = ".detail-content > p"
# listchapter_selector = ".list-chapter > nav > ul > .row > div > a"
# chapter

# Saytruyen
domain = "https://saytruyen.net/"
commic_selector = ".lst_story > li > .fed-lazy"
title_selector = ".wrap-content-info > .title"
author_selector = ".list-info :first-child"
status_selector = ":is(.contiep, .xong)"
kind_selector = ".list01"
view_selector = ".real-view"
# star_selector = "star"
follow_selector = ".theodoitruyen > span > b"
details_selector = "p.shortened"
listchapter_selector = ".chap-item > a"
commic_img_selector = ".wrap-content-image > img"
# CHAPTER
chapter_title_selector = "h1.tentruyen > a"
chapter_name_selector = "h2.tentruyen > a"
image_selector = "#lst_content > img"

driver = webdriver.Chrome()

# list
urllist = []
chapterurllist = []


class comic:
	def __init__(self, name, another_name, status, genres, views, rate, subcribe, description, chapters, commic_img, author):
		self.another_name = another_name
		self.chapters = chapters
		self.subcribe = subcribe
		self.rate = rate
		self.status = status
		self.genres = genres
		self.name = name
		self.description = description
		self.views = views
		self.commic_img = commic_img
		self.author = author

	def __init__(self):
		self.another_name = ""
		self.chapters = []
		self.subcribe = 0
		self.rate = 0
		self.status = ""
		self.genres = []
		self.name = ""
		self.description = ""
		self.views = 0
		self.commic_img = ""
		self.author = ""

	def print(self):
		print(self.name)
		print(self.status)
		print(self.genres)
		print(self.chapters)

class chapter:
	def __init__(self, chapterName, chapterNumber, chapterImgs, views, _url):
		self.chapterName = chapterName
		self.chapterNumber = chapterNumber
		self.chapterImgs = chapterImgs
		self.views = views
		self._url = _url

	def __init__(self):
		self.chapterNumber = 0
		self.chapterName = ""
		self.chapterImgs = []
		self.views = 0
		self._url = ""

	def __init__(self, url):
		self._url = _url

	def print(self):
		print(self.chapterName)
		print(self.chapterNumber)
		print(self.chapterImgs)
		print(self.views)

new_commic = comic()
chapterList = []

def get_comic_links(domain):
	driver.get(domain)
	driver.implicitly_wait(10)
	urllist = []
	commicLink = driver.find_elements_by_css_selector(commic_selector)
	for link in commicLink:
		url = link.get_attribute("href")
		urllist.append(url)
	return urllist

def get_comic_images(url, comic_id):
	imagelist = []
	# new tab
	sleep(1)
	driver.get(url)
	driver.implicitly_wait(10)
	# select
	chapterimages = driver.find_elements_by_css_selector(image_selector)
	comic_title = driver.find_element_by_css_selector(chapter_title_selector).text
	comic_name = driver.find_element_by_css_selector(chapter_name_selector).text
	# print(comic_name)

	# get links
	for i in chapterimages:
		imagelist.append(i.get_attribute("src"))

	print(type("http://localhost:3002/api/v1/chapters/"))
	print(type(comic_id))
	chapter_url = "http://localhost:3002/api/v1/chapters/" + comic_id
	y = requests.post(chapter_url, data={
		'chapterName': comic_name ,
		'chapterNumber': 0,
		'chapterImgs': imagelist
		})
	print(y.content)

	return imagelist

def get_commic_details(url):
	# open new tab
	sleep(1)
	driver.get(url)
	driver.implicitly_wait(10)

	# select elements
	title = driver.find_element_by_css_selector(title_selector).text
	author = driver.find_element_by_css_selector(author_selector).text
	status = driver.find_element_by_css_selector(status_selector).text
	kind = driver.find_element_by_css_selector(kind_selector).text
	view = driver.find_element_by_css_selector(view_selector).text
	# rating = driver.find_element_by_class_name(star_selector).get_attribute("data-rating")
	follow = driver.find_element_by_css_selector(follow_selector).text
	# description = driver.find_element_by_css_selector(details_selector).text
	commic_img = driver.find_element_by_css_selector(commic_img_selector).get_attribute("src")
	listchapter = driver.find_elements_by_css_selector(listchapter_selector)

	# chapterurllist.clear()
	# get chapter link

	new_commic.author = author[9:]
	new_commic.name = title
	new_commic.status = status
	new_commic.genres = kind.split()
	new_commic.views = float(view[:-1])
	new_commic.subcribe = int(follow)
	new_commic.commic_img = commic_img

	x = requests.post("http://localhost:3002/api/v1/comics", data={
	'name': new_commic.name,
	'another_name': new_commic.another_name,
	'author': new_commic.author,
	'subcribe': new_commic.subcribe,
	'status': new_commic.status,
	'genres': new_commic.genres,
	'views': new_commic.views,
	'rate': new_commic.rate,
	'description': new_commic.description,
	'chapters': new_commic.chapters,
	'comicImg': new_commic.commic_img
	}).json()

	comic_id = x["comic"]["_id"]
	print(type(comic_id))

	links = []
	for i in listchapter:
		# sleep(1)
		print(i.get_attribute("href"))
		links.append(i.get_attribute("href"))

	for i in links:
		imgs = get_comic_images(i, comic_id)
		# print(imgs)



def main():
	try:
		urllist = get_comic_links(domain)

		# print(len(urllist))

		for link in urllist:
			get_commic_details(link)

			# print(x.status_code)

			# new_commic.print()
			# for i in chapterurllist:
				# get_comic_images(i)
				# print(imagelist)


		driver.close()
	finally:
		driver.quit()
		print("END")

main()
# response = requests.get("http://api.open-notify.org/astros.json")
# print(response.json())

 