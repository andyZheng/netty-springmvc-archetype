//浮点数加法运算

function FloatAdd(arg1, arg2) {

  var r1, r2, m;

  try {

      r1 = arg1.toString().split(".")[1].length;

  } catch(e) {

      r1 = 0;

  }

  try {

      r2 = arg2.toString().split(".")[1].length;

  } catch(e) {

      r2 = 0;

  }

  m = Math.pow(10, Math.max(r1, r2)) ;
  return (arg1 * m + arg2 * m) / m;

}




//浮点数减法运算

function FloatSub(arg1, arg2) {

  var r1, r2, m, n;

  try {

      r1 = arg1.toString().split(".")[1].length;

  } catch(e) {

      r1 = 0;

  }

  try {

      r2 = arg2.toString().split(".")[1].length;

  } catch(e) {

      r2 = 0;

  }

  m = Math.pow(10, Math.max(r1, r2));

  //动态控制精度长度

  n = (r1 >= r2) ? r1: r2;

  return ((arg1 * m - arg2 * m) / m).toFixed(n);

}




//浮点数乘法运算

function FloatMul(arg1, arg2) {

  var m = 0,

  s1 = arg1.toString(),

  s2 = arg2.toString();

  try {

      m += s1.split(".")[1].length;

  } catch(e) {}

  try {

      m += s2.split(".")[1].length;

  } catch(e) {}

  return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);

}




//浮点数除法运算

function FloatDiv(arg1, arg2) {

  var t1 = 0,

  t2 = 0,

  r1, r2;

  try {

      t1 = arg1.toString().split(".")[1].length;

  } catch(e) {}

  try {

      t2 = arg2.toString().split(".")[1].length;

  } catch(e) {}

  with(Math) {

      r1 = Number(arg1.toString().replace(".", ""));
      r2 = Number(arg2.toString().replace(".", ""));
      return (r1 / r2) * pow(10, t2 - t1);

  }

}
