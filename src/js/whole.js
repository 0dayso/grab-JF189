/**
 * ======================================
 * 说明：在jdk1.6下通过ScriptEngineManager和ScriptEngine类来操作js
 * 把www.sn.189.cn登陆页面里，加密密码的js写到方法function encPwd(pwd)
 * 同时把依赖其他的js文件，都拷贝到这个文件里，注意，jquery的js(带$符号)会报错，这里加密也没有用到，可以不加入改js
 * ======================================
 */

/**
 * 加密密码
 * @param s
 * @return {String}
 */
function encPwd(pwd)
{
    var key =  bodyRSA();
    function bodyRSA()
    {
        setMaxDigits(130);
        return new RSAKeyPair("10001","","a5aeb8c636ef1fda5a7a17a2819e51e1ea6e0cceb24b95574ae026536243524f322807df2531a42139389674545f4c596db162f6e6bbb26498baab074c036777");
    }

    var a = new Array();
    var sl = pwd.length;
    var i = 0;
    while (i < sl) {
        a[i] = pwd.charCodeAt(i);
        i++;
    }

    while (a.length % key.chunkSize != 0) {
        a[i++] = 0;
    }

    var al = a.length;
    var result = "";
    var j, k, block;
    for (i = 0; i < al; i += key.chunkSize) {
        block = new BigInt();
        j = 0;
        for (k = i; k < i + key.chunkSize; ++j) {
            block.digits[j] = a[k++];
            block.digits[j] += a[k++] << 8;
        }
        var crypt = key.barrett.powMod(block, key.e);
        var text = key.radix == 16 ? biToHex(crypt) : biToString(crypt, key.radix);
        result += text + " ";
    }
    return result.substring(0, result.length - 1); // Remove last space.
}
// BarrettMu, a class for performing Barrett modular reduction computations in
// JavaScript.
//
// Requires BigInt.js.
//
// Copyright 2004-2005 David Shapiro.
//
// You may use, re-use, abuse, copy, and modify this code to your liking, but
// please keep this header.
//
// Thanks!
//
// Dave Shapiro
// dave@ohdave.com

function BarrettMu(m)
{
    this.modulus = biCopy(m);
    this.k = biHighIndex(this.modulus) + 1;
    var b2k = new BigInt();
    b2k.digits[2 * this.k] = 1; // b2k = b^(2k)
    this.mu = biDivide(b2k, this.modulus);
    this.bkplus1 = new BigInt();
    this.bkplus1.digits[this.k + 1] = 1; // bkplus1 = b^(k+1)
    this.modulo = BarrettMu_modulo;
    this.multiplyMod = BarrettMu_multiplyMod;
    this.powMod = BarrettMu_powMod;
}

function BarrettMu_modulo(x)
{
    var q1 = biDivideByRadixPower(x, this.k - 1);
    var q2 = biMultiply(q1, this.mu);
    var q3 = biDivideByRadixPower(q2, this.k + 1);
    var r1 = biModuloByRadixPower(x, this.k + 1);
    var r2term = biMultiply(q3, this.modulus);
    var r2 = biModuloByRadixPower(r2term, this.k + 1);
    var r = biSubtract(r1, r2);
    if (r.isNeg) {
        r = biAdd(r, this.bkplus1);
    }
    var rgtem = biCompare(r, this.modulus) >= 0;
    while (rgtem) {
        r = biSubtract(r, this.modulus);
        rgtem = biCompare(r, this.modulus) >= 0;
    }
    return r;
}

function BarrettMu_multiplyMod(x, y)
{
    /*
     x = this.modulo(x);
     y = this.modulo(y);
     */
    var xy = biMultiply(x, y);
    return this.modulo(xy);
}

function BarrettMu_powMod(x, y)
{
    var result = new BigInt();
    result.digits[0] = 1;
    var a = x;
    var k = y;
    while (true) {
        if ((k.digits[0] & 1) != 0) result = this.multiplyMod(result, a);
        k = biShiftRight(k, 1);
        if (k.digits[0] == 0 && biHighIndex(k) == 0) break;
        a = this.multiplyMod(a, a);
    }
    return result;
}

//===================================================================================//
// BigInt, a suite of routines for performing multiple-precision arithmetic in
// JavaScript.
//
// Copyright 1998-2005 David Shapiro.
//
// You may use, re-use, abuse,
// copy, and modify this code to your liking, but please keep this header.
// Thanks!
//
// Dave Shapiro
// dave@ohdave.com

// IMPORTANT THING: Be sure to set maxDigits according to your precision
// needs. Use the setMaxDigits() function to do this. See comments below.
//
// Tweaked by Ian Bunning
// Alterations:
// Fix bug in function biFromHex(s) to allow
// parsing of strings of length != 0 (mod 4)

// Changes made by Dave Shapiro as of 12/30/2004:
//
// The BigInt() constructor doesn't take a string anymore. If you want to
// create a BigInt from a string, use biFromDecimal() for base-10
// representations, biFromHex() for base-16 representations, or
// biFromString() for base-2-to-36 representations.
//
// biFromArray() has been removed. Use biCopy() instead, passing a BigInt
// instead of an array.
//
// The BigInt() constructor now only constructs a zeroed-out array.
// Alternatively, if you pass <true>, it won't construct any array. See the
// biCopy() method for an example of this.
//
// Be sure to set maxDigits depending on your precision needs. The default
// zeroed-out array ZERO_ARRAY is constructed inside the setMaxDigits()
// function. So use this function to set the variable. DON'T JUST SET THE
// VALUE. USE THE FUNCTION.
//
// ZERO_ARRAY exists to hopefully speed up construction of BigInts(). By
// precalculating the zero array, we can just use slice(0) to make copies of
// it. Presumably this calls faster native code, as opposed to setting the
// elements one at a time. I have not done any timing tests to verify this
// claim.

// Max number = 10^16 - 2 = 9999999999999998;
//               2^53     = 9007199254740992;

var biRadixBase = 2;
var biRadixBits = 16;
var bitsPerDigit = biRadixBits;
var biRadix = 1 << 16; // = 2^16 = 65536
var biHalfRadix = biRadix >>> 1;
var biRadixSquared = biRadix * biRadix;
var maxDigitVal = biRadix - 1;
var maxInteger = 9999999999999998;

// maxDigits:
// Change this to accommodate your largest number size. Use setMaxDigits()
// to change it!
//
// In general, if you're working with numbers of size N bits, you'll need 2*N
// bits of storage. Each digit holds 16 bits. So, a 1024-bit key will need
//
// 1024 * 2 / 16 = 128 digits of storage.
//

var maxDigits;
var ZERO_ARRAY;
var bigZero, bigOne;

function setMaxDigits(value)
{
    maxDigits = value;
    ZERO_ARRAY = new Array(maxDigits);
    for (var iza = 0; iza < ZERO_ARRAY.length; iza++) ZERO_ARRAY[iza] = 0;
    bigZero = new BigInt();
    bigOne = new BigInt();
    bigOne.digits[0] = 1;
}

setMaxDigits(129);

// The maximum number of digits in base 10 you can convert to an
// integer without JavaScript throwing up on you.
var dpl10 = 15;
// lr10 = 10 ^ dpl10
var lr10 = biFromNumber(1000000000000000);

function BigInt(flag)
{
    if (typeof flag == "boolean" && flag == true) {
        this.digits = null;
    }
    else {
        this.digits = ZERO_ARRAY.slice(0);
    }
    this.isNeg = false;
}

function biFromDecimal(s)
{
    var isNeg = s.charAt(0) == '-';
    var i = isNeg ? 1 : 0;
    var result;
    // Skip leading zeros.
    while (i < s.length && s.charAt(i) == '0') ++i;
    if (i == s.length) {
        result = new BigInt();
    }
    else {
        var digitCount = s.length - i;
        var fgl = digitCount % dpl10;
        if (fgl == 0) fgl = dpl10;
        result = biFromNumber(Number(s.substr(i, fgl)));
        i += fgl;
        while (i < s.length) {
            result = biAdd(biMultiply(result, lr10),
                biFromNumber(Number(s.substr(i, dpl10))));
            i += dpl10;
        }
        result.isNeg = isNeg;
    }
    return result;
}

function biCopy(bi)
{
    var result = new BigInt(true);
    result.digits = bi.digits.slice(0);
    result.isNeg = bi.isNeg;
    return result;
}

function biFromNumber(i)
{
    var result = new BigInt();
    result.isNeg = i < 0;
    i = Math.abs(i);
    var j = 0;
    while (i > 0) {
        result.digits[j++] = i & maxDigitVal;
        i = Math.floor(i / biRadix);
    }
    return result;
}

function reverseStr(s)
{
    var result = "";
    for (var i = s.length - 1; i > -1; --i) {
        result += s.charAt(i);
    }
    return result;
}

var hexatrigesimalToChar = new Array(
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
    'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
    'u', 'v', 'w', 'x', 'y', 'z'
);

function biToString(x, radix)
    // 2 <= radix <= 36
{
    var b = new BigInt();
    b.digits[0] = radix;
    var qr = biDivideModulo(x, b);
    var result = hexatrigesimalToChar[qr[1].digits[0]];
    while (biCompare(qr[0], bigZero) == 1) {
        qr = biDivideModulo(qr[0], b);
        digit = qr[1].digits[0];
        result += hexatrigesimalToChar[qr[1].digits[0]];
    }
    return (x.isNeg ? "-" : "") + reverseStr(result);
}

function biToDecimal(x)
{
    var b = new BigInt();
    b.digits[0] = 10;
    var qr = biDivideModulo(x, b);
    var result = String(qr[1].digits[0]);
    while (biCompare(qr[0], bigZero) == 1) {
        qr = biDivideModulo(qr[0], b);
        result += String(qr[1].digits[0]);
    }
    return (x.isNeg ? "-" : "") + reverseStr(result);
}

var hexToChar = new Array('0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'a', 'b', 'c', 'd', 'e', 'f');

function digitToHex(n)
{
    var mask = 0xf;
    var result = "";
    for (i = 0; i < 4; ++i) {
        result += hexToChar[n & mask];
        n >>>= 4;
    }
    return reverseStr(result);
}

function biToHex(x)
{
    var result = "";
    var n = biHighIndex(x);
    for (var i = biHighIndex(x); i > -1; --i) {
        result += digitToHex(x.digits[i]);
    }
    return result;
}

function charToHex(c)
{
    var ZERO = 48;
    var NINE = ZERO + 9;
    var littleA = 97;
    var littleZ = littleA + 25;
    var bigA = 65;
    var bigZ = 65 + 25;
    var result;

    if (c >= ZERO && c <= NINE) {
        result = c - ZERO;
    } else if (c >= bigA && c <= bigZ) {
        result = 10 + c - bigA;
    } else if (c >= littleA && c <= littleZ) {
        result = 10 + c - littleA;
    } else {
        result = 0;
    }
    return result;
}

function hexToDigit(s)
{
    var result = 0;
    var sl = Math.min(s.length, 4);
    for (var i = 0; i < sl; ++i) {
        result <<= 4;
        result |= charToHex(s.charCodeAt(i))
    }
    return result;
}

function biFromHex(s)
{
    var result = new BigInt();
    var sl = s.length;
    for (var i = sl, j = 0; i > 0; i -= 4, ++j) {
        result.digits[j] = hexToDigit(s.substr(Math.max(i - 4, 0), Math.min(i, 4)));
    }
    return result;
}

function biFromString(s, radix)
{
    var isNeg = s.charAt(0) == '-';
    var istop = isNeg ? 1 : 0;
    var result = new BigInt();
    var place = new BigInt();
    place.digits[0] = 1; // radix^0
    for (var i = s.length - 1; i >= istop; i--) {
        var c = s.charCodeAt(i);
        var digit = charToHex(c);
        var biDigit = biMultiplyDigit(place, digit);
        result = biAdd(result, biDigit);
        place = biMultiplyDigit(place, radix);
    }
    result.isNeg = isNeg;
    return result;
}

function biDump(b)
{
    return (b.isNeg ? "-" : "") + b.digits.join(" ");
}

function biAdd(x, y)
{
    var result;

    if (x.isNeg != y.isNeg) {
        y.isNeg = !y.isNeg;
        result = biSubtract(x, y);
        y.isNeg = !y.isNeg;
    }
    else {
        result = new BigInt();
        var c = 0;
        var n;
        for (var i = 0; i < x.digits.length; ++i) {
            n = x.digits[i] + y.digits[i] + c;
            result.digits[i] = n % biRadix;
            c = Number(n >= biRadix);
        }
        result.isNeg = x.isNeg;
    }
    return result;
}

function biSubtract(x, y)
{
    var result;
    if (x.isNeg != y.isNeg) {
        y.isNeg = !y.isNeg;
        result = biAdd(x, y);
        y.isNeg = !y.isNeg;
    } else {
        result = new BigInt();
        var n, c;
        c = 0;
        for (var i = 0; i < x.digits.length; ++i) {
            n = x.digits[i] - y.digits[i] + c;
            result.digits[i] = n % biRadix;
            // Stupid non-conforming modulus operation.
            if (result.digits[i] < 0) result.digits[i] += biRadix;
            c = 0 - Number(n < 0);
        }
        // Fix up the negative sign, if any.
        if (c == -1) {
            c = 0;
            for (var i = 0; i < x.digits.length; ++i) {
                n = 0 - result.digits[i] + c;
                result.digits[i] = n % biRadix;
                // Stupid non-conforming modulus operation.
                if (result.digits[i] < 0) result.digits[i] += biRadix;
                c = 0 - Number(n < 0);
            }
            // Result is opposite sign of arguments.
            result.isNeg = !x.isNeg;
        } else {
            // Result is same sign.
            result.isNeg = x.isNeg;
        }
    }
    return result;
}


function biHighIndex(x)
{
    var result = x.digits.length - 1;
    while (result > 0 && x.digits[result] == 0) --result;
    return result;
}

function biNumBits(x)
{
    var n = biHighIndex(x);
    var d = x.digits[n];
    var m = (n + 1) * bitsPerDigit;
    var result;
    for (result = m; result > m - bitsPerDigit; --result) {
        if ((d & 0x8000) != 0) break;
        d <<= 1;
    }
    return result;
}

function biMultiply(x, y)
{
    var result = new BigInt();
    var c;
    var n = biHighIndex(x);
    var t = biHighIndex(y);
    var u, uv, k;

    for (var i = 0; i <= t; ++i) {
        c = 0;
        k = i;
        for (j = 0; j <= n; ++j, ++k) {
            uv = result.digits[k] + x.digits[j] * y.digits[i] + c;
            result.digits[k] = uv & maxDigitVal;
            c = uv >>> biRadixBits;
            //c = Math.floor(uv / biRadix);
        }
        result.digits[i + n + 1] = c;
    }
    // Someone give me a logical xor, please.
    result.isNeg = x.isNeg != y.isNeg;
    return result;
}

function biMultiplyDigit(x, y)
{
    var n, c, uv;

    result = new BigInt();
    n = biHighIndex(x);
    c = 0;
    for (var j = 0; j <= n; ++j) {
        uv = result.digits[j] + x.digits[j] * y + c;
        result.digits[j] = uv & maxDigitVal;
        c = uv >>> biRadixBits;
        //c = Math.floor(uv / biRadix);
    }
    result.digits[1 + n] = c;
    return result;
}

function arrayCopy(src, srcStart, dest, destStart, n)
{
    var m = Math.min(srcStart + n, src.length);
    for (var i = srcStart, j = destStart; i < m; ++i, ++j) {
        dest[j] = src[i];
    }
}

var highBitMasks = new Array(0x0000, 0x8000, 0xC000, 0xE000, 0xF000, 0xF800,
    0xFC00, 0xFE00, 0xFF00, 0xFF80, 0xFFC0, 0xFFE0,
    0xFFF0, 0xFFF8, 0xFFFC, 0xFFFE, 0xFFFF);

function biShiftLeft(x, n)
{
    var digitCount = Math.floor(n / bitsPerDigit);
    var result = new BigInt();
    arrayCopy(x.digits, 0, result.digits, digitCount,
        result.digits.length - digitCount);
    var bits = n % bitsPerDigit;
    var rightBits = bitsPerDigit - bits;
    for (var i = result.digits.length - 1, i1 = i - 1; i > 0; --i, --i1) {
        result.digits[i] = ((result.digits[i] << bits) & maxDigitVal) |
            ((result.digits[i1] & highBitMasks[bits]) >>>
                (rightBits));
    }
    result.digits[0] = ((result.digits[i] << bits) & maxDigitVal);
    result.isNeg = x.isNeg;
    return result;
}

var lowBitMasks = new Array(0x0000, 0x0001, 0x0003, 0x0007, 0x000F, 0x001F,
    0x003F, 0x007F, 0x00FF, 0x01FF, 0x03FF, 0x07FF,
    0x0FFF, 0x1FFF, 0x3FFF, 0x7FFF, 0xFFFF);

function biShiftRight(x, n)
{
    var digitCount = Math.floor(n / bitsPerDigit);
    var result = new BigInt();
    arrayCopy(x.digits, digitCount, result.digits, 0,
        x.digits.length - digitCount);
    var bits = n % bitsPerDigit;
    var leftBits = bitsPerDigit - bits;
    for (var i = 0, i1 = i + 1; i < result.digits.length - 1; ++i, ++i1) {
        result.digits[i] = (result.digits[i] >>> bits) |
            ((result.digits[i1] & lowBitMasks[bits]) << leftBits);
    }
    result.digits[result.digits.length - 1] >>>= bits;
    result.isNeg = x.isNeg;
    return result;
}

function biMultiplyByRadixPower(x, n)
{
    var result = new BigInt();
    arrayCopy(x.digits, 0, result.digits, n, result.digits.length - n);
    return result;
}

function biDivideByRadixPower(x, n)
{
    var result = new BigInt();
    arrayCopy(x.digits, n, result.digits, 0, result.digits.length - n);
    return result;
}

function biModuloByRadixPower(x, n)
{
    var result = new BigInt();
    arrayCopy(x.digits, 0, result.digits, 0, n);
    return result;
}

function biCompare(x, y)
{
    if (x.isNeg != y.isNeg) {
        return 1 - 2 * Number(x.isNeg);
    }
    for (var i = x.digits.length - 1; i >= 0; --i) {
        if (x.digits[i] != y.digits[i]) {
            if (x.isNeg) {
                return 1 - 2 * Number(x.digits[i] > y.digits[i]);
            } else {
                return 1 - 2 * Number(x.digits[i] < y.digits[i]);
            }
        }
    }
    return 0;
}

function biDivideModulo(x, y)
{
    var nb = biNumBits(x);
    var tb = biNumBits(y);
    var origYIsNeg = y.isNeg;
    var q, r;
    if (nb < tb) {
        // |x| < |y|
        if (x.isNeg) {
            q = biCopy(bigOne);
            q.isNeg = !y.isNeg;
            x.isNeg = false;
            y.isNeg = false;
            r = biSubtract(y, x);
            // Restore signs, 'cause they're references.
            x.isNeg = true;
            y.isNeg = origYIsNeg;
        } else {
            q = new BigInt();
            r = biCopy(x);
        }
        return new Array(q, r);
    }

    q = new BigInt();
    r = x;

    // Normalize Y.
    var t = Math.ceil(tb / bitsPerDigit) - 1;
    var lambda = 0;
    while (y.digits[t] < biHalfRadix) {
        y = biShiftLeft(y, 1);
        ++lambda;
        ++tb;
        t = Math.ceil(tb / bitsPerDigit) - 1;
    }
    // Shift r over to keep the quotient constant. We'll shift the
    // remainder back at the end.
    r = biShiftLeft(r, lambda);
    nb += lambda; // Update the bit count for x.
    var n = Math.ceil(nb / bitsPerDigit) - 1;

    var b = biMultiplyByRadixPower(y, n - t);
    while (biCompare(r, b) != -1) {
        ++q.digits[n - t];
        r = biSubtract(r, b);
    }
    for (var i = n; i > t; --i) {
        var ri = (i >= r.digits.length) ? 0 : r.digits[i];
        var ri1 = (i - 1 >= r.digits.length) ? 0 : r.digits[i - 1];
        var ri2 = (i - 2 >= r.digits.length) ? 0 : r.digits[i - 2];
        var yt = (t >= y.digits.length) ? 0 : y.digits[t];
        var yt1 = (t - 1 >= y.digits.length) ? 0 : y.digits[t - 1];
        if (ri == yt) {
            q.digits[i - t - 1] = maxDigitVal;
        } else {
            q.digits[i - t - 1] = Math.floor((ri * biRadix + ri1) / yt);
        }

        var c1 = q.digits[i - t - 1] * ((yt * biRadix) + yt1);
        var c2 = (ri * biRadixSquared) + ((ri1 * biRadix) + ri2);
        while (c1 > c2) {
            --q.digits[i - t - 1];
            c1 = q.digits[i - t - 1] * ((yt * biRadix) | yt1);
            c2 = (ri * biRadix * biRadix) + ((ri1 * biRadix) + ri2);
        }

        b = biMultiplyByRadixPower(y, i - t - 1);
        r = biSubtract(r, biMultiplyDigit(b, q.digits[i - t - 1]));
        if (r.isNeg) {
            r = biAdd(r, b);
            --q.digits[i - t - 1];
        }
    }
    r = biShiftRight(r, lambda);
    // Fiddle with the signs and stuff to make sure that 0 <= r < y.
    q.isNeg = x.isNeg != origYIsNeg;
    if (x.isNeg) {
        if (origYIsNeg) {
            q = biAdd(q, bigOne);
        } else {
            q = biSubtract(q, bigOne);
        }
        y = biShiftRight(y, lambda);
        r = biSubtract(y, r);
    }
    // Check for the unbelievably stupid degenerate case of r == -0.
    if (r.digits[0] == 0 && biHighIndex(r) == 0) r.isNeg = false;

    return new Array(q, r);
}

function biDivide(x, y)
{
    return biDivideModulo(x, y)[0];
}

function biModulo(x, y)
{
    return biDivideModulo(x, y)[1];
}

function biMultiplyMod(x, y, m)
{
    return biModulo(biMultiply(x, y), m);
}

function biPow(x, y)
{
    var result = bigOne;
    var a = x;
    while (true) {
        if ((y & 1) != 0) result = biMultiply(result, a);
        y >>= 1;
        if (y == 0) break;
        a = biMultiply(a, a);
    }
    return result;
}

function biPowMod(x, y, m)
{
    var result = bigOne;
    var a = x;
    var k = y;
    while (true) {
        if ((k.digits[0] & 1) != 0) result = biMultiplyMod(result, a, m);
        k = biShiftRight(k, 1);
        if (k.digits[0] == 0 && biHighIndex(k) == 0) break;
        a = biMultiplyMod(a, a, m);
    }
    return result;
}

//===================================================================================//
function setCookie(name, value, hours, path) {
    var name = escape(name);
    var value = escape(value);
    var expires = new Date();
    expires.setTime(expires.getTime() + hours * 3600000);
    path = path == "" ? "" : ";path=" + path;
    _expires = (typeof hours) == "string" ? "" : ";expires="
        + expires.toUTCString();
    document.cookie = name + "=" + value + _expires + path;
}
function getCookieValue(name) {
    var name = escape(name);
    var allcookies = document.cookie;
    name += "=";
    var pos = allcookies.indexOf(name);
    if (pos != -1) {
        var start = pos + name.length;
        var end = allcookies.indexOf(";", start);
        if (end == -1)
            end = allcookies.length;
        var value = allcookies.substring(start, end);
        return unescape(value);
    } else
        return "";
}

function deleteCookie(name, path) {
    var name = escape(name);
    var expires = new Date(0);
    path = path == "" ? "" : ";path=" + path;
    document.cookie = name + "=" + ";expires=" + expires.toUTCString() + path;
}
//===================================================================================//
var findPwdUrl = "/service/passmanage/init.action";

var uplimitTime = true;//随机码发送是否超过限制的时间，默认是

var checkWayRadio = 1;//手机登录方式的密码类型，默认是手机密码


//图形验证码
function check_img(url){
    if (url.indexOf("random") < 0){
        url += "?random=" + Math.random();
    }
    $("#vImg").attr("src",url);
}

/*
 *根据cookie信息初始化登录form
 *@param change 是否初始化登录方式
 */
function initLoginFormCookie(change){
    if(cookie_loginType != "" && cookie_loginType !=undefined) {
        if(change){
            $("#loginType").val(cookie_loginType);
            $("#loginType").change();
            //存在本地网，绑定本地网变更事件
            if(cookie_loginType != "4" && cookie_loginType != "5"){
                $("#thislatnId").bind("change",function(){
                    if($("#loginType").val() == cookie_loginType && cookie_thislatnId == $(this).val()){
                        initLoginNameAndPwd();
                    }else{
                        $("#loginName").val("");
                        $("#passwd").val("");
                        $("input[name='remPwd']").removeAttr("checked");
                        return false;
                    }
                });
            }

            $("#loginName").bind("blur",function(){
                if($("#loginType").val() == cookie_loginType && cookie_thislatnId == $("#thislatnId").val() && $("#loginName").val()==cookie_loginName){
                    $("#passwd").val(cookie_passwd);
                }else{
                    $("#passwd").val("");
                }
            });
        }else {
            //登录方式改变，同步变更本地网及登录账号， 密码信息
            if(cookie_loginType != "4" && cookie_loginType != "5" && cookie_loginType != "10"){
                $("#thislatnId").val(cookie_thislatnId);
                $("#thislatnId").change();
            }
            initLoginNameAndPwd();
        }

    }
}

function changeLoginName(){
    // $("#passWord").val("");
}

//初始化页面 登录账号， 密码信息
function initLoginNameAndPwd(){
    $("input[id='loginName']").val(cookie_loginName);
    $("#loginName").css("color","#000");
    if($("#errorMsg").html() == ""){
        $("#passWord").val(cookie_passwd);
    }
    $("input[name='remPwd']").attr("checked", "checked");
    $("#remPwdContent").find("span").addClass("zogo-form-checkbox-checked");
}



function getDefaultValue(){
    var loginType=$("#loginType").val();
    if("4" == loginType){
        return "请输入11位手机号码";
    }else if("2" == loginType || "3" == loginType){
        var latnid = $("#thislatnId").val();
        if(latnid =='290' || latnid =='910'){
            return "请输入8位电话号码";
        }else{
            return "请输入7位电话号码";
        }
    }else if("5" == loginType){
        return "请输入拨号账号";
    }else if("15" == loginType){
        return "请输入天翼会员卡号";
    }else if("1"==loginType){
        return "请输入客户标识码";
    }else if("10"==loginType){
        return "请输入注册账号";
    }else{
        return "";
    }

}



//点击密码类型需要切换显示
function seleCheckWay(v){
    checkWayRadio = v;
    var display = "";
    //如果选的是手机密码
    if(v==1){
        //显示为手机密码
        $("#login_passWord").html("手机密码:");
        $("#passWord_reset").html("找回密码");
        $("#passWord_reset").attr("disabled", false);
        $("#passWord_reset").attr("href",findPwdUrl);
        if($("#loginType").val() == getCookieValue("loginType")){
            $("#passwd").val(getCookieValue("passwd"));
        }
    }
    //如果选的是随机码
    else{
        //显示为随机密码
        $("#login_passWord").html("随机密码:");
        $("#passWord_reset").html("点击获取");
        $("#passWord_reset").attr("href","javascript:sedSmsCode();");
        if(!uplimitTime){
            $("#passWord_reset").attr("disabled", true);
            $("#passWord_reset").attr("href","javascript:void(0);");
        }
        display = "none";
        $("#passwd").val("");
    }
    document.getElementById('remPwdContent').style.display=display;
}

//短信发送限制超时后回调方法
function sendTimeOut(){
    uplimitTime = true;
    $("#passWord_reset").attr("disabled", false);
    $("#passWord_reset").attr("href","javascript:sedSmsCode();");
    $("#errorMsg").css("display","none");
    $("#errorMsg").html('');
}

//发送短信随机码
function sedSmsCode()
{
    //获取手机号码
    var mobilNbr = $("#loginName").val();
    var defaultValue = getDefaultValue();
    if(mobilNbr==null||mobilNbr=="" || mobilNbr==defaultValue){
        $("#errorMsg").css("display","block");
        $("#errorMsg").html('请输入手机号码!');
        return;
    }

    //手机号码格式为1，第二位为3,5,8,后接9个数字
    var reg = /^1[3,5,8]\d{9}$/;
    if(!reg.test(mobilNbr)){
        $("#errorMsg").css("display","block");
        $("#errorMsg").html('手机号码格式错误!');
        return;
    }

    var url = ctxPath + "/SendSms?";
    var params = "phone=" + mobilNbr;
    $.ajax({
        url: url,  //后台处理程序
        type: 'post',    //数据发送方式
        dataType: 'json',   //接受数据格式
        data: params,   //要传递的数据
        cache:false,
        success:function(data){
            var limitTime = 60;
            if(data.result == "1"){
                $("#errorMsg").css("display","block");
                $("#errorMsg").html(data.msg);
                $("#passWord_reset").attr("disabled", false);
                $("#passWord_reset").attr("href","javascript:sedSmsCode();");
            }else{
                $("#errorMsg").css("display","block");
                $("#errorMsg").html('随机码已发送'+ limitTime +'秒后可重发!');
                setTimeout("sendTimeOut()",limitTime * 1000);
                countDown(60);
            }
        },
        error: function(){
            $("#errorMsg").css("display","block");
            $("#errorMsg").html('随机码发送失败，请稍候再试!');
            setTimeout("sendTimeOut()",limitTime * 1000);
        }
    });

    $("#passWord_reset").attr("disabled", true);
    $("#passWord_reset").attr("href", "javascript:void(0);");
    uplimitTime = false;
}

function trim(str) {
    return str.replace( /(^[\s|　]*)|([\s|　]*$)/g , '' );
}

//登录提交验证
function checkSubmit(obj)
{
    //登录名
    var loginName = trim($('#loginName').val());
    loginName = loginName.replace(/(^\s*)|(\s*$)/g, "");
    var pwd = $.trim($('#passWord').val());
    document.getElementById("loginName").value = loginName;
    //验证号码是否为空
    if("" == loginName||loginName==getDefaultValue())
    {
        $("#loginMsgDiv").css("display","block");
        $("#loginMsg").html('请输入登录号码!');
        return;
    }
    //手机登录
    if($("#loginType").val() == "4"){
        var reg = /^1[3,5,8]\d{9}$/;

        //验证手机号码输入是否正确
        if(!reg.test(loginName)){
            $("#loginMsgDiv").css("display","block");
            $("#loginMsg").html('手机号码格式错误!');
            return;
        }
    }else if($("#loginType").val() == "2"){
        var latnid = $("#thislatnId").val();
        var reg =  /^[0-9]{7}$/;
        if(latnid =='290' || latnid =='910'){
            var reg =  /^[0-9]{8}$/;
        }
        if(!reg.test($("#loginName").val())){
            $("#loginMsgDiv").css("display","block");
            $("#loginMsg").html('电话号码格式错误!');
            return;
        }
    }
    //验证密码是否为空
    if(pwd == null || pwd =="")
    {
        $("#loginMsgDiv").css("display","block");
        $("#loginMsg").html('请输入密码!');
        return;
    }
    //判断验证码是否为空
    if($("#valicodeshow").css("display") == "block"){
        var validCode = document.getElementById("validCode").value;
        if(validCode == null || validCode ==""){
            $("#loginMsgDiv").css("display","block");
            $("#loginMsg").html('请输入验证码!');
            return;
        }
    }
    //对密码进行加密
    if(getCookieValue("passwd") != pwd){
        document.getElementById("passWord").value = encryptedString(key, pwd);
    }else if(getCookieValue("passwd") == pwd){
        $("#remPwd").val("13");
    }

    var remPwd = $("input[name='remPwd']").attr("checked");
    deleteCookie("loginType", "/");
    deleteCookie("loginName", "/");
    deleteCookie("passwd", "/");
    deleteCookie("thislatnId", "/");
    if(remPwd && document.getElementById('remPwdContent').style.display !="none"){
        //手机号码
        setCookie("loginType", $("#loginType").val(), 2400, "/");
        setCookie("loginName", $("#loginName").val(), 2400, "/");
        setCookie("passwd", $("#passWord").val(), 2400, "/");
        //电话号码
        setCookie("thislatnId", $("#thislatnId").val(), 2400, "/");
    }
    var form1 = document.getElementById("LOGIN_F");
    // Feely
    form1.action=ctxPath + "/GetLoginServlet";
    $("#loginButton").attr("disabled","disabled");
    form1.submit();
}

//倒计时
function countDown(cnt){
    var maxNum = 60;
    var num = parseInt(cnt);
    if(num == maxNum){
        $("#errorMsg").css("display","block");
        $("#errorMsg").html('随机码已发送'+ num +'秒后可重发!');
    }
    num=num-1;
    if(num <= 1){
        $("#errorMsg").css("display","none");
    }else{
        $("#errorMsg").html('随机码已发送'+ num +'秒后可重发!');
        setTimeout("countDown(" + num +")",1000);
    }
}

//根据所选地市提示语变动
function changeLatn(){
    var loginType = $("#loginType").val();
    var latnid = $("#thislatnId").val();
    if(loginType=='2' || loginType=='3'){//固话、小灵通
        if(latnid =='290' || latnid =='910'){
            $("#loginName").val("请输入8位电话号码");
        }else{
            $("#loginName").val("请输入7位电话号码");
        }
    }
}

//改变登录方式时需要对页面文字修改
function changeLoginType(loginType)
{
    //显示记住密码
    document.getElementById('remPwdContent').style.display="";
    $("#loginType").val(loginType);

    //如果是手机类型
    if("4" == loginType)
    {
        //隐藏本地网选择
        $("#selectLatn").hide();
        $("#loginAccount").text("手机号码:");
        $("#seleCheck").show();
        $("#login_passWord").html("手机密码:");
        $("#loginName").val("请输入11位手机号码");
        $("#loginName").css("color","#999");
        $("#passWord_reset").html("找回密码");
        $("#passWord_reset").attr("href",findPwdUrl);
        $("#passWord_reset").attr("disabled", false);
        //如果选择的是随机密码并且随机密码的时间限制还没超过
        if(!uplimitTime && checkWayRadio != 1){
            $("#passWord_reset").html("点击获取");
            $("#passWord_reset").attr("disabled", true);
            $("#passWord_reset").attr("href","javascript:void(0);");
        }
        //当随机密码选中的时候不显示记住密码
        if($("input[name='selCheckWay']:checked").val() == "18"){
            document.getElementById('remPwdContent').style.display="none";
        }
    }
    //如果是小灵通或固话
    else if(loginType == "2" || loginType == "3")
    {	$("#s11").attr("checked",'checked');
        //显示本地网选择
        $("#selectLatn").show();
        $("#loginAccount").text("电话号码:");
        $("#seleCheck").hide();
        $("#login_passWord").html("登录密码:");
        changeLatn();//loginName根据地市显示提示语
        $("#passWord_reset").html("找回密码");
        $("#loginName").val("请输入8位电话号码");
        $("#loginName").css("color","#999");
        $("#passWord_reset").attr("href",findPwdUrl);
        $("#passWord_reset").attr("disabled", false);
    }
    //如果是客户标识码
    else if(loginType == "1")
    {	$("#s11").attr("checked",'checked');
        //显示本地网选择
        $("#selectLatn").show();
        $("#loginAccount").html("标&nbsp;识&nbsp;码:");
        $("#seleCheck").hide();
        $("#login_passWord").html("登录密码:");
        $("#loginName").val("请输入客户标识码");
        $("#loginName").css("color","#999");
        $("#passWord_reset").html("找回密码");
        $("#passWord_reset").attr("href",findPwdUrl);
        $("#passWord_reset").attr("disabled", false);
    }
    //如果是宽带帐号
    else if(loginType == "5")
    {
        $("#s11").attr("checked",'checked');
        //隐藏本地网选择
        //$("#selectLatn").hide();
        $("#selectLatn").show();
        $("#loginAccount").text("宽带帐号:");
        $("#seleCheck").hide();
        $("#loginName").val("请输入拨号账号");
        $("#login_passWord").html("登录密码:");
        $("#loginName").css("color","#999");
        $("#passWord_reset").html("找回密码");
        $("#passWord_reset").attr("href",findPwdUrl);
        $("#passWord_reset").attr("disabled", false);
    }
    //注册用户登录
    else if(loginType == "10")
    {	$("#s11").attr("checked",'checked');
        //隐藏本地网选择
        $("#selectLatn").hide();
        $("#loginAccount").text("注册帐号:");
        $("#seleCheck").hide();
        $("#login_passWord").html("登录密码:");
        $("#loginName").val("请输入注册账号");
        $("#loginName").css("color","#999");
        $("#passWord_reset").html("找回密码");
        $("#passWord_reset").attr("href",findPwdUrl);
        $("#passWord_reset").attr("disabled", false);
    }
    //天翼会员卡号
    else if(loginType == "15")
    {	$("#s11").attr("checked",'checked');
        //显示本地网选择
        $("#selectLatn").show();

        $("#loginAccount").text("会员卡号:");

        $("#seleCheck").hide();
        $("#loginName").val("请输入天翼会员卡号");
        $("#loginName").css("color","#999");
        $("#login_passWord").html("会员密码:");

        $("#passWord_reset").html("");

    }
    $("input[name='remPwd']").removeAttr("checked");
    $("#passwd").val("");
    if(loginType == cookie_loginType){
        initLoginFormCookie(false);
    }


}



// 一点通
var ydt_flg = true;

function  autoYDTLogin(ch){
    //alert(ch.checked);
    if(ch.checked){
        setCookie("_onePointAutoLogin","1",24*30,"/");// 延长cookie保存时间30天;
        submitYDT();
    }else {
        setCookie("_onePointAutoLogin","2",24*30,"/");
        submitYDT();
    }
}

function getUserAccount(ip){
    var url = "/freeAccountLogin";
    var params = "ip="+ip;
    $.ajax({
        url: url,  //后台处理程序
        type: 'post',    //数据发送方式
        dataType: 'json',   //接受数据格式
        data: params,   //要传递的数据
        cache:false,
        success:function(data){
            if(data.account != null && data.latnId != null)
            {
                $("#YDTdiv").css("display","block");// 显示一点通登录模块
                $("#loginSwitch").css("display","block");
                $("#logindiv").css("display","none");// 隐藏登录模块
                $("#account").html(data.account);// 帐号
                $("#ydtLoginName").val(data.account);
                $("#ydtLatnId").val(data.latnId);
            }
        },
        error: function(){
        }
    });
}
function submitYDT()
{
    var form1 = document.getElementById("LOGIN_F");
    form1.action=ctxPath + "/LoginServlet";
    $("#loginButton").attr("disabled","disabled");
    form1.submit();
}
function switchDiv(){
    if($("#YDTdiv").css("display")=="none" ){
        $("#YDTdiv").css("display","block");
        $("#logindiv").css("display","none");
    }else{
        $("#YDTdiv").css("display","none");
        $("#logindiv").css("display","block");
    }
}

function YDTlogin(){

    var form1 = document.getElementById("YDTform");
    form1.action=ctxPath + "/LoginServlet";
    form1.submit();

}

//===================================================================================//
// RSA, a suite of routines for performing RSA public-key computations in
// JavaScript.
//
// Requires BigInt.js and Barrett.js.
//
// Copyright 1998-2005 David Shapiro.
//
// You may use, re-use, abuse, copy, and modify this code to your liking, but
// please keep this header.
//
// Thanks!
//
// Dave Shapiro
// dave@ohdave.com

function RSAKeyPair(encryptionExponent, decryptionExponent, modulus)
{
    this.e = biFromHex(encryptionExponent);
    this.d = biFromHex(decryptionExponent);
    this.m = biFromHex(modulus);
    // We can do two bytes per digit, so
    // chunkSize = 2 * (number of digits in modulus - 1).
    // Since biHighIndex returns the high index, not the number of digits, 1 has
    // already been subtracted.
    this.chunkSize = 2 * biHighIndex(this.m);
    this.radix = 16;
    this.barrett = new BarrettMu(this.m);
}

function twoDigit(n)
{
    return (n < 10 ? "0" : "") + String(n);
}

function encryptedString(key, s)
    // Altered by Rob Saunders (rob@robsaunders.net). New routine pads the
    // string after it has been converted to an array. This fixes an
    // incompatibility with Flash MX's ActionScript.
{
//    var a = new Array();
//    var sl = s.length;
//    var i = 0;
//    while (i < sl) {
//        a[i] = s.charCodeAt(i);
//        i++;
//    }
//
//    while (a.length % key.chunkSize != 0) {
//        a[i++] = 0;
//    }
//
//    var al = a.length;
//    var result = "";
//    var j, k, block;
//    for (i = 0; i < al; i += key.chunkSize) {
//        block = new BigInt();
//        j = 0;
//        for (k = i; k < i + key.chunkSize; ++j) {
//            block.digits[j] = a[k++];
//            block.digits[j] += a[k++] << 8;
//        }
//        var crypt = key.barrett.powMod(block, key.e);
//        var text = key.radix == 16 ? biToHex(crypt) : biToString(crypt, key.radix);
//        result += text + " ";
//    }
//    return result.substring(0, result.length - 1); // Remove last space.
    return "xxxxx";
}

function decryptedString(key, s)
{
    var blocks = s.split(" ");
    var result = "";
    var i, j, block;
    for (i = 0; i < blocks.length; ++i) {
        var bi;
        if (key.radix == 16) {
            bi = biFromHex(blocks[i]);
        }
        else {
            bi = biFromString(blocks[i], key.radix);
        }
        block = key.barrett.powMod(bi, key.d);
        for (j = 0; j <= biHighIndex(block); ++j) {
            result += String.fromCharCode(block.digits[j] & 255,
                block.digits[j] >> 8);
        }
    }
    // Remove trailing null, if any.
    if (result.charCodeAt(result.length - 1) == 0) {
        result = result.substring(0, result.length - 1);
    }
    return result;
}
