![](img/header.png)

# Tugas 3 IF3110 Pengembangan Aplikasi Berbasis Web

Melakukan upgrade Website ojek online sederhana pada Tugas 2 dengan mengaplikasikan ***cloud service* (Firebase Cloud Messaging) dan *framework* MEAN stack**.

## Tujuan Pembuatan Tugas

Mengerti :
* MEAN stack (Mongo, Express, Angular, dan Node)
* *Cloud service* Firebase Cloud Messaging (FCM) dan kegunaannya.
* Web security terkait access token dan HTTP Headers.

## Anggota Tim
1. Jordhy Fernando (13515004)
2. Sylvia Juliana (13515070)
3. Stevanno Hero Leadervand (13515082)


### Tampilan Program
Halaman Login

![](screenshots/login.PNG)

Halaman Sign Up

![](screenshots/signup.PNG)

Halaman Order pada Pengguna

![](screenshots/order.PNG)

Halaman Select Driver

![](screenshots/select-driver.PNG)

Halaman Order pada Driver

![](screenshots/driver-order.PNG)

Halaman Order pada Driver Ketika Melakukan Finding Order

![](screenshots/driver-finding-order.PNG)

Halaman Order pada Driver Ketika Mendapat Order

![](screenshots/driver-got-order.PNG)

Halaman Chat Driver pada Pengguna

![](screenshots/chat-driver.PNG)

Halaman Complete Order

![](screenshots/complete-order.PNG)

Halaman Previous Order

![](screenshots/my-previous-order.PNG)

Halaman Driver History

![](screenshots/driver-history.PNG)

Halaman Profile

![](screenshots/profile.PNG)

Halaman Edit Profile

![](screenshots/edit-profile.PNG)

Halaman Edit Preferred Location

![](screenshots/edit-preferred-location.PNG)

### Pembagian Tugas

Chat App Front-end:
1. Halaman order customer - 13515070
2. Halaman chat driver & order driver - 13515004, 13515070
3. Halaman chat customer - 13515004, 13515070
4. Penerimaan pesan dari FCM - 13515082

Chat REST Service:  
1. Penyimpanan history chat - 13515004, 13515070
2. Pengiriman pesan ke FCM - 13515004
3. Pengambilan history chat - 13515004, 13515070
4. Penyimpanan online user - 13515004, 13515082
5. Penghapusan online user - 13515004, 13515082
6. Pengambilan daftar driver - 13515004, 13515082

Fitur security (IP, User-agent):
1. Pembuatan token - 13515004
2. Validasi token - 13515070, 13515082

