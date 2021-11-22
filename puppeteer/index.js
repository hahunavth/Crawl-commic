const puppeteer = require("puppeteer");
const axios = require("axios");

// # Saytruyen
const DOMAIN = "https://saytruyen.net/";
const HOME_PAGE = {
  commic_selector: ".lst_story > li > .fed-lazy",
};
const COMIC_PAGE = {
  title_selector: ".wrap-content-info > .title",
  //   title_selector: "h1",
  author_selector: ".list-info :first-child",
  status_selector: ":is(.contiep, .xong)",
  kind_selector: ".list01",
  view_selector: ".real-view",
  // # star_selector = "star"
  follow_selector: ".theodoitruyen > span > b",
  details_selector: "p.shortened",
  listchapter_selector: ".chap-item > a",
  commic_img_selector: ".wrap-content-image > img",
};
// CHAPTER
const CHAPTER_PAGE = {
  chapter_title_selector: "h1.tentruyen > a",
  chapter_name_selector: "h2.tentruyen > a",
  image_selector: "#lst_content > img",
};
const agent = [
  "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 Safari/537.36",
  "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36",
  "Mozilla/5.0 (iPad; CPU OS 13_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/79.0.3945.73 Mobile/15E148 Safari/604.1",
];
let request_count = 0;

// =========== START =========== //
const start = async () => {
  // INITIAL
  const browser = await puppeteer.launch({ headless: true, devtools: false });
  const page = await browser.newPage();
  //   Fix headless site protected by cloudflare
  await page.setUserAgent(
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36"
  );

  // HOME PAGE
  await page.goto(DOMAIN, { waitUntil: "networkidle2" });
  const chapter_urls = await page.$$eval(HOME_PAGE.commic_selector, (els) => {
    return els.map((el) => el.getAttribute("href"));
  });
  await page.waitForTimeout(1000);

  // COMIC PAGE
  async function comic_loop(browser, chapter_urls, callback) {
    try {
      for (id in chapter_urls) {
        console.log(`${id}: ${DOMAIN}${chapter_urls[id]}`);
        const page2 = await browser.newPage();
        await page2.setUserAgent(agent[request_count++ % 3]);
        await page2.goto(DOMAIN + chapter_urls[id], {
          waitUntil: "networkidle2",
        });

        // get data
        async function get_comic() {
          try {
            const title = await page2.$eval(
              COMIC_PAGE.title_selector,
              (el) => el.innerText
            );
            // console.log(`Title: ${title}`);
            const author = await page2.$eval(
              COMIC_PAGE.author_selector,
              (el) => el.innerText
            );
            // console.log(`Author: ${author}`);
            const comic_img = await page2.$eval(
              COMIC_PAGE.commic_img_selector,
              (el) => el.getAttribute("src")
            );
            const follow = await page2.$eval(
              COMIC_PAGE.follow_selector,
              (el) => el.innerText
            );
            // console.log(`ImgLink; ${comic_img}`);
            const details = await page2.$eval(
              COMIC_PAGE.details_selector,
              (el) => el.innerText
            );
            // console.log(`Details: ${details}`);

            // console.log(`Follow: ${follow}`);
            const kind = await page2.$eval(
              COMIC_PAGE.kind_selector,
              (el) => el.innerText
            );
            // console.log(`Kind: ${kind.split("\n")}`);
            const status = await page2.$eval(
              COMIC_PAGE.status_selector,
              (el) => el.innerText
            );
            // console.log(`Status: ${status}`);
            const view = await page2.$eval(
              COMIC_PAGE.view_selector,
              (el) => el.innerText
            );
            // console.log(`View: ${view}`);
            const listchapters = await page2.$$eval(
              COMIC_PAGE.listchapter_selector,
              (els) =>
                els.map((el) => {
                  return {
                    chapterName: el.innerText,
                    // ERROR: Domain is not defined ???
                    chapterLink:
                      "https://saytruyen.net/" + el.getAttribute("href"),
                  };
                })
            );
            // console.log(`Num of chapters: ${listchapters.length}`);
            // console.log(listchapters);

            const requestBody = {
              name: title,
              author: author,
              genres: kind,
              comicImg: "https://saytruyen.net/" + comic_img,
              status: status.split("\n"),
              description: details,
            };
            console.log(requestBody);
            const comic_res = await axios.post(
              "https://hahunavth-express-api.herokuapp.com/api/v1/comics",
              requestBody
            );
            // console.log(comic_res.data.comic._id);

            if (typeof callback === "function") {
              for (chapter_data of listchapters) {
                await callback(chapter_data, comic_res.data.comic._id);
              }
            }
          } catch (e) {
            console.log(`Error: ${e}`);
          }
        }
        await get_comic();

        await page2.close();
      }
    } catch (e) {
      console.log(`Error: ${e}`);
    }
  }

  //   console.log(`All: ${chapter_urls.length}`);
  await comic_loop(browser, chapter_urls, chapter_callback);

  // CHAPTER PAGE
  async function chapter_callback(data, comicId) {
    // console.log("url:" + url);
    try {
      console.log(`Get: ${data.chapterName}`);
      const page3 = await browser.newPage();
      await page3.setUserAgent(agent[request_count++ % 3]);
      await page3.goto(data.chapterLink, {
        waitUntil: "networkidle2",
      });

      const chapter_name = await page3.$eval(
        CHAPTER_PAGE.chapter_name_selector,
        (el) => el.innerText
      );
      //   console.log(`chapter_name: ${chapter_name}`);
      const comic_title = await page3.$eval(
        CHAPTER_PAGE.chapter_title_selector,
        (el) => el.innerText
      );
      //   console.log(`comic_title: ${comic_title}`);
      const chapter_imgs = await page3.$$eval(
        CHAPTER_PAGE.image_selector,
        (els) => {
          return els.map(
            (el) => "https://saytruyen.net/" + el.getAttribute("src")
          );
        }
      );

      const regex = chapter_name.match(/^[^\d]*(\d+)/);
      const chapter_num = Number.parseFloat(regex[1]);
      //   console.log(chapter_num)

      //   console.log(chapter_imgs);
      console.log(
        `https://hahunavth-express-api.herokuapp.com/api/v1/chapters${comicId}`
      );
      const requestBody = {
        comicId: comicId,
        chapterNumber: chapter_num,
        chapterName: chapter_name || "",
        chapterImgs: chapter_imgs || [],
        views: 0,
      };
      axios.post(
        `https://hahunavth-express-api.herokuapp.com/api/v1/chapters${comicId}`
      );

      await page3.close();
    } catch (e) {
      console.log(e);
    }
  }

  await browser.close();
};
start();
