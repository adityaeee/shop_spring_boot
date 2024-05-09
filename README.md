

# Shop API with Java Spring Boot


### properties
1. create database postgresql with name __aditya_shop__ or you can use the other name what you want,
but you must update **database_name** in the _application.properties_
2. setup all information database in application properties like **database username, password or else**
3. you can manage all properties this app in _application.properties_ like __time zone__


### branch 
use latest branch to see what's new in repository, branch **master** just create first repo.


### API
use postman or any application to hit endpoint, if you use postman application
you can import file __shop_postman_collection.json__ to your postman and **boom....**

### Tables
1. m_customer
2. m_product
3. m_transaction
4. m_trans_detail

#### table relations
- __m_customer__ --> __m_transaction__ _[ One to many ]_
- __m_transaction__ --> __m_trans_detail__ _[ One to many ]_
- __m_trans_detail__ --> __m_product__ _[Many to one]_


