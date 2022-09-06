# cs211-641-project

## รายชื่อสมาชิก

* 6310406272 ชัชวาลย์ สามา (ChatchawanSama)
    * userProfileController, changeDisplayNameController, changePasswordController, lightMode.css, darkMode.css,
      Effect GUI ใน css, Transition scence
    * shopController, ItemController
* 6310401122 ไมเคิล แซ่เติน (MichaelTrizel)
    * ทำหน้า UI, รายละเอียดของ User, รายละเอียดของ Report และระบบ Ban/Unban ของ AdminPage
    * ระบบและ UI ของ Report
* 6310403982 ธงไทย รุจิเวชวงษ์ (timtim5569)
    * เขียนระบบ Login , Register และ Promotion
    * ทำหน้า UI ของ Login , Register , Product , OrderConfirm , ManangeProduct , ManageOrder , ManagePromotion และ
      Review
    * ทำระบบให้ผู้ใช้สามารถ Review สินค้าได้ และ ทำระบบดาวของสินค้า และระบบการบันทึกการจัดส่งสินค้า
* 6310406281 ซาฮิล ชูดวง (NotHolmes)
    * เขียนโปรแกรมส่วน Model Class เป็นส่วนใหญ่ คือ Product, Account, User, Admin, Order, Review, Shop รวมถึง Class
      Collection และ Class DataSource เขียนไฟล์เพื่อเก็บข้อมูลด้วย
    * และเขียน Controller ในส่วนที่เกี่ยวข้องกับ Model Class ด้านบนทั้งหมด
    * Category การแบ่งแยกหมวดหมู่ คุณลักษณะและประเภท

## การใช้งานโปรแกรม

* [รายละเอียดและการใช้งานโปรแกรม](https://docs.google.com/document/d/1NTw2yNPeU8OvVAjQIOBULqTolcEIGCsaBbmn8tLHSuY/edit?usp=sharing)

## วิธีการติดตั้งหรือรันโปรแกรม

โปรเจคที่สำเร็จจะอยู่ที่ directory [Release1.0](Release1.0) โดยที่จะเปิดโปรแกรมจากไฟล์ .jar ได้โดยตรง

หรือ

เปิดโปรแกรมจาก jlink ด้วยไฟล์ launch.sh ที่อยู่ใน directory [jlink](Release1.0/jlink/bin)  
ซึ่งจะใช้ข้อมูลเริ่มต้นในการทดสอบระบบชุดเดียวกัน เพียงแต่ directory ต่างกัน

## การวางโครงสร้างไฟล์

ข้อมูลส่วนร่วมจะถูกเก็บไว้ที่ `data/` เช่น account.csv, shopList.csv, categoryList.csv, report.csv   
แต่ข้อมูลของ user แต่ละคนจะถูกเก็บไว้ที่ directory ของ user คนนั้น   
เช่น username1 จะมีรูปภาพอยู่ที่ `data/username1/username1.png`   
และจะมี productList.csv ของร้านค้าของตนอยู่ที่ `data/username1/shop/ชื่อshop/productList.csv`
รวมถึงรายการสั่งซื้อสินค้าของร้านและรีวิวของสินค้าภายในร้านด้วย

[เพิ่มเติม](https://docs.google.com/document/d/1dXMqHygOrb-iiMqaVsAPyPzfXpV6ZpJ53Io1ig2gnLA/edit?usp=sharing)

## ตัวอย่างข้อมูลผู้ใช้ระบบ

* (Role) (Username)    (Password)
* Admin :  adminadmin password
* Admin :  bigboss imboss123
* User :   userTester01 userTester01
* User :   johnbacon08 ilovebacon123
* User :   ITTech ITTECH123

## สรุปสิ่งที่พัฒนาแต่ละครั้งที่นำเสนอความก้าวหน้าของระบบ

* ครั้งที่ 1 (09/08/2021)
    * Flowchart, UML diagram, Class Account, Class User, Class Admin (พัฒนาโดย NotHolmes)
    * Register FXML, Register Controller , Marketplace FXML , Marketplace Controller (พัฒนาโดย ChatchawanSama)
    * Class Shop, Class Product, Class Review (พัฒนาโดย MichaelTrizel)
    * Login FXML , Login Controller , Credit FXML , Credit Controller (พัฒนาโดย timtim5569)

* ครั้งที่ 2 (08/09/2021)
    * Class Shop, Class Product, Class ProductList, Class ProductListDataSource, Product FXML, Product Controller,
      AddProduct FXML, AddProduct Controller, MyShop FXML, MyShop Controller, SetUpShop FXML, SetUpShop Controller (
      พัฒนาโดย NotHolmes)
    * User Profile FXML, User Profile Controller เชื่อมไปหน้าเปลี่ยนรหัสผ่านและเปลี่ยนชื่อ( Display name ) ได้,
      อัพเดทรูปโปรไฟล์ได้ Change Password FXML, Change Password Controller เปลี่ยนรหัสผ่านได้ Change Display FXML,
      Change Display Controller เปลี่ยนชื่อ Display name ได้ Update Market Place Controller, Update หน้า UI ของ Market
      Place FXML อัพเดทหน้า Market Place ให้ดูดีชึ้น, อัพเดทการเชื่อมปุ่มที่จะไปหน้าต่างๆ  (พัฒนาโดย ChatchawanSama)
    * Fix Login Controller เมื่อ Login เข้ามาในระบบจะดึงข้อมูลผู้ใช้จากไฟล์ csv , Fix Register Controller
      รับข้อมูลจากการสมัครมาบันทึกลงในไฟล์ csv , ระบบ Upload รูปภาพตอน Register (พัฒนาโดย timtim5569)
    * AdminPage FXML, AdminPage Controller, Class Report, Class Review, Class ReportListDataSource, Class
      UserListDataSource, Class UserList, Class ReportList (พัฒนาโดย MichaelTrizel)

* ครั้งที่ 3 (30/09/2021)
    * Interface dataSource, Class Order, Class OrderList, Class OrderListDataSource, OrderConfirmation FXML, Sorting
      Products, Class ShopList, Class ShopListDataSource, Class AccountList, Class AccountListDataSource, Class Review,
      Class ReviewList, Class ReviewListDataSource (พัฒนาโดย NotHolmes)
    * ทำการเปลี่ยน Theme ทั้งโปรแกรมให้สามารถปรับเป็น Dark Mode และ Light Mode ได้, ทำการแก้ปรับ GUI ในหน้า
      userProfile.fxml changeDisplayName.fxml changePassword.fxml, ทำการ set css effect และ transition scene,
      ทำการช่วยเพื่อนแก้บัคในโปรแกรม (พัฒนาโดย ChatchawanSama)
    * ปรับ LoginController และ RegisterController มาใช้กับ DataSource , fix login fxml , fix register fxml , fix product
      fxml , fix ProductController , fix orderConfirmation fxml , fix OrderConfirmationController , fix item fxml , fix
      ItemController , fix marketplace fxml , fix MarketplaceController  (พัฒนาโดย timtim5569)
    * ปรับ Class Report, เพิ่มระบบ Ban/Unban ใน Class AdminPageController, update User Log ใน adminPage.fxml, updata
      Report Log ใน adminPage.fxml (พัฒนาโดย MichaelTrizel)
