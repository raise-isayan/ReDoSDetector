var someVar = "[0-9a-z]+";

const reg1 = new RegExp("abc");
const reg2 = new RegExp("[a-z]+", "gi");
const reg3 = new RegExp(someVar);         // 対象外
const reg4 = new RegExp("ab+c\\d*", "i");

const r1 = /abc\\d+/gi;
const r2 = /foo.*bar/;
const r3 = new RegExp("def\\s+", "g");
const re3 = /escaped\/slash/;
const notRegex = "this is just a string";

// const comment1 = new RegExp("comment");
