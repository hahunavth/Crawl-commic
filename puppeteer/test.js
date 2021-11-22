// let str = "Chapter 12341: Hello 2346 weret fegsk 134 2v";
// let num = str.match(/^[^\d]*(\d+)/);
// console.log(Number.parseInt(num[1]));
const fs = require("fs");

const axios = require("axios");

(async () => {
  const data = await axios.get(
    "http://www.nettruyenpro.com/truyen-tranh/bach-chi-y-tien-29529"
  );
  fs.writeFileSync("fs", data.data);
  console.log(data);
})();

// const cherio = require("cherio")
