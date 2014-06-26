/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package org.dragon.rmm.payment;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	//合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088411812546712";

	//收款支付宝账号
	public static final String DEFAULT_SELLER = "cde_home@126.com";

	//商户私钥，自助生成
	public static final String PRIVATE = ""
			+ "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALAUoJ/"
			+ "SnYjFJ5eaMbbKDktvVq+Kcuh5bVHWv81K89PhTmGuJwpk+16XP9CclWscjE+/"
			+ "66bt18cX+9IWJ4U0PtMAYOQ4Xz4MLvRGi+VimV2+"
			+ "GDNMT5ZF7Kot47oxEZcxx4AnGqJj0eud04D3V8v3a3VCGAwrZBWXZFa0NcrspHQBAgMBAAEC"
			+ "gYB2cSmq2E3HlncPb1VJk5kR/"
			+ "Q7efc7VufhjOIVEXsqtF71Vz4PLZioActUmSm3n4wgvDPZDD3I6JlPp+"
			+ "aUT4RTwVBJyNaiXb97jFLL2ugR10iCDLMTSQneB9XbSq1OvqIQ2BeWJqqdv4Cb8ackAvLFI0"
			+ "toL0om9OUICARZfCnEVwQJBAODc62/wYt1wx7ahMmnDmbn4Xs+rnzlKrE/"
			+ "iqpZRbBVBZX2ZPBEH8r3hldqpmfsV9+"
			+ "PvV8fQmbWhL8QeGM2VWYUCQQDIdnCMQVhtZkixs8RvHmDop6OJiEJcIh3woPbgg9ml94ltEV"
			+ "k4snNKbjXPXkBydjWvKbYO2mbL3Mb8llYQAZtNAkEAhurGsMFQ8ICss52AfAkKgZTOflgBDw"
			+ "UI4l2j8NElwpMcbAG3EpjsQpXJrgs5Angp5i9DQfadY2c1G7zbMO69aQJASNOGhd3Dg1MSe8"
			+ "SES54stLkaV3+yJAic+etwZRZhfsMUIRSZZlimO9cYI3LpVOwPCsjLhE/"
			+ "h4pQdkmwHbh83TQJAQlqHyFekJ5AFExTwmZ1ydYiEX8l4nujcl71MElEC+"
			+ "H7zbqwtVnjnaEvZSiOD2LhWtcAuZA80WTTZKY2bSF086A==";

	public static final String PUBLIC = ""
			+ "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/"
			+ "y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/"
			+ "PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/"
			+ "B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

}
