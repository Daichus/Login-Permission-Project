# Login-Permission-Project
Login Permission Project

專案名稱：銀行權限管理系統-後端 



## 專案介紹

此專案為提供於銀行管理內部人員權限為主要目的。人員在本系統，會根據賦予的權限決定可以執行哪一些個操作；比如查看自己的個人資訊與登錄紀錄，而如後台管理員擁有最高權限，可以執行所有新增、刪除或修改的操作。

本專案核心為管理權限。系統提供了「角色」的機制，是一種群組的設計，每個角色持有的權限不一樣。當需要給予員工權限時，並不是直接將權限賦予到員工身上，而是給予員工角色，如此員工便可以擁有所有角色內包含的權限。一個員工可以同時擁有多個角色，另外角色可以根據需求調整包含的權限，如此增加管理上的彈性。

## 身分驗證機制

只有持有允許修改角色權限的人員可以進行角色編輯的操作。如果人員發送修改請求，必須要有一種驗證身分的機制。本系統設計為每登錄都會發送一組 JWT Token 給予登入用戶，Token 內部含有員工自己的 id。每當進行請求操作，便需要將持有的 Token 傳送至後端，後端收到 Token 除了驗證簽章的有效性以外，會解析除內部的員工 id，隨後後台系統便會根據 id 去查找該名員工持有的權限，再決定該員工是否有訪問的權力。

 ## 技術架構
 - 後端：Spring Boot, JPA
 -  前端：Vue 3
 - 資料庫：MySQL
 - 安全驗證機制：Spring Security, JWT
 - 其他：Junit, Jacoco, Mockito

 ## 核心功能
1. 登入驗證
   - JWT Token 認證
   - Spring Security驗證權限
2. 角色管理
   - 角色新增/刪除
   - 角色設定與調整
3. 員工管理
   - 角色配置
   - 員工資料修改
   - 重設密碼
4. 科別管理
5. 部門管理
6. 登錄記錄管理
   - 登入歷史紀錄
   - 登入異常紀錄
7. 操作紀錄追蹤
   - 系統操作日誌

 ## 系統展示
影片網址-基礎功能展示：https://www.youtube.com/watch?v=dxQSKz93Pw4

影片網址-操作紀錄功能展示：https://youtu.be/GkuseNdygjo

## 系統特色
1. 彈性的權限管理
   - 採用角色群組機制，降低權限管理複雜度
   - 支援多重角色配置，提升管理靈活性
   - 權限異動即時生效
2. 安全機制
   - JWT Token 確保請求安全性
   - Spring Security 進行權限驗證
   - 操作與登入紀錄追蹤
  
## 開發心得
1. 我們透過實作 Spring Security 與 JWT，理解了認證與授權機制的重要性
2. 學會使用 JPA 進行資料庫操作，提升了開發效率
3. 學會使用 Vue 進行前端開發
4. 透過撰寫單元測試，確保程式碼的品質
5. 學習到了如何管理專案進度與版本控制
6. 學習到多關聯表在查找時對於效能的影響

## 負責項目 - D1204476 許恩嘉
-後端：
-	使用Spring Boot框架設計RESTful API架構, 接收與回應前端傳遞的資料
-	運用JPA進行資料庫資料存取層設計, 本專案為使用MySQL
-	整合JWT與Spring Security，建立多層級的身分驗證機制
-	人員資料、權限設定、科別管理、登錄紀錄設置的程式流程設計與實作
-	JWT類別的單元測試與整合測試，確保程式碼品質與穩定性
  
-前端：
-	人員權限設定、職位管理與科別管理等功能實作
-	使用Fetch API實作HTTP請求，處理使用者登入、資料查詢等功能
-	表單驗證機制，確保向後端發送的資料符合格式要求
-	實作JWT身分驗證的存取控制，管理使用者登入狀態

