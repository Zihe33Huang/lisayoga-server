# **1 总览**
## **1.1. 目的**
本文主要明确Lisayoga健身约课系统功能, 实现会员卡办卡、充值、退款，会员约课，财务流水等功能，及基本数据（课程、教练、会员、约课记录）的管理维护功能。 其中基本数据的需求暂时按照老系统进行开发，本文档中不再赘述。 


## **1.2 名词解释** 
## **2 需求说明**
![](Aspose.Words.dd035f22-8b35-402a-baa2-f10e896fe896.001.png)

## **2.1 会员卡模块**
![](Aspose.Words.dd035f22-8b35-402a-baa2-f10e896fe896.002.png)

按照上表规则，会员卡可细分为

**常规团课卡**： 年卡工作日2次卡， 年卡工作日3次卡，年卡周末2次卡，半年卡工作日2次卡，半年卡工作日3次卡，半年卡工作日周末2次卡等，季卡工作日2次卡， 季卡工作日3次卡，季卡周末2次卡，和 次卡， 以及年卡半年卡季卡的无限次卡种

**vip团课卡**：次卡

**常规私教卡**： 次卡

**普拉提器械私教卡**： 次卡

**普拉提器械团课卡**：次卡   

**充值卡**：根据额度不同，折扣不同

**大礼包充值卡**：无折扣

卡种共:

` `9(常规团课卡) + 1 + 1 + 1 + 1 + x (**需要健身房给出具体的额度和折扣规则**) + 1 = 14 + x 


其中， 一名会员一个时期只能拥有一张时长卡，但可拥有多张次卡和充值卡。 
### **2.1.1 (卡种)常规团课时长卡**
**卡期限**： 根据时长类别进行设定。 如 年卡有效期为 【开卡日期,  开卡日期 + 1年】， 半年卡有效期为 【开卡日期， 开卡日期 + 半年】 

**上课次数限制**： 举例说明： ① 工作日2次卡只能在工作日进行使用， 且使用上限为2次。当约课次数超过2次时，C端提示约课次数已达上限。 

② 周末2次卡只能在周末使用，其他逻辑和①一致。

**上课类型：** 只能上团课课程。  

### **2.1.2 (卡种)次卡** 
包括VIP团课卡、常规私教卡、普拉提器械私教卡，普拉提团课私教卡。

每次签到后，将会员卡对应的次数-1。

### **2.1.3 (卡种)充值卡**
充值卡根据课程价格付费，且支持充值功能。 

会员卡充值逻辑：

只支持卡种为 1、充值卡  2、大礼包充值卡 的充值 


### **2.1.4 (功能)开卡**

**前提条件**：会员信息已录入系统

**流程：** 选择要开卡的会员 -> 选择开卡日期 -> 选择卡类别。① 时长卡选择年卡、半年卡、季卡， 并选择工作日2次卡、工作日3次卡、周末2次卡等类型。② 次卡选择vip团课卡、常规私教卡等， 并输入次数。③ 充值卡选择 普通充值卡 和 大礼包充值卡， 输入充值金额。   

![](Aspose.Words.dd035f22-8b35-402a-baa2-f10e896fe896.003.png)

### **2.1.5 (功能)退款** 
退款逻辑分为两种：

① 时长卡。  退款金额= 总金额 - (已过天数/全部天数 \* 总金额) 。 举例： 年卡价格为1000元，距开卡日期已过180天， 则退款为  1000 - (180/365) \* 1000 ≈ 507 

② 次卡。   退款金额 = 总金额 - (已用次数/全部次数 \* 总金额)  

③ 充值卡。  









## **2.2 约课模块**
### <a name="_toc104942520_wpsoffice_level3"></a>**2.2.1 课程大表**
和老系统功能保持一致。 课程中3个数字从左到右是，确认签到人数，实际约课人数，这节课最多上课人数。

![](Aspose.Words.dd035f22-8b35-402a-baa2-f10e896fe896.004.png)

### **2.2.2 会员约课**
约课状态分为三种： ① 已约 ② 候补 ③ 取消 

**对于C端会员来说**，当课程实际约课人数已满（即 课程实际约课人数 = 这节课最多上课人数）时， 提示课程约课人数已满， 是否进入候补。 若会员同意进入候补，则进入候补队列。  

![](Aspose.Words.dd035f22-8b35-402a-baa2-f10e896fe896.005.png)

当一名已约会员取消预约时， 一名候补会员按照顺序自动进入已约状态。 

**对于后台管理人员来说,** 可以无视课程最大容量的规则，手动添加约课状态为已约的会员。 并且可以手动修改候补队列的会员顺序。 

PS：健身房实际的上课人数可能会大于系统中设定的课程最大容量，为了方便将会员上课情况输入系统， 故需要实现该机制。


### **2.2.3 会员签到** 
会员签到功能为后台管理员手动点击触发， 会员C端没有签到功能。 

会员实际已经参加了课程后，后台管理人员点击签到，从而对会员卡进行结算。 

### **2.2.4 (C端)教练查看课程**
教练可以查看课程的预约情况

## **2.3 财务流水**
### **2.3.1 教练月工资结算** 
分全职教练和兼职教练 

![](Aspose.Words.dd035f22-8b35-402a-baa2-f10e896fe896.006.png)
### **2.3.2 入账统计**
### **2.3.3 每月总收入统计**

