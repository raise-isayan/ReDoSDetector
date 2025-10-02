var someVar = "test";

const reg1 = new RegExp("abc");
const reg2 = RegExp("[a-z]+", "gi");
const reg3 = RegExp(someVar);         // 対象外
const reg4 = RegExp("ab+c\\d*", "i");

const r1 = /abc\\d+/gi;
const r2 = /foo.*bar/;
const r3 = new RegExp("def\\s+", "g");
const re3 = /escaped\/slash/;
const notRegex = "this is just a string";
