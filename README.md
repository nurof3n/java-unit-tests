# Getting Started

## What is this?

This is the repository for my homework on java unit testing from the Java Spring Advanced course. I've implemented JWT
authentication on the two extra endpoints from the `MarketController`:
`/carts/sorted_by_total_quantity` and `/users/sorted_by_number_of_orders`.

I've used OpenApi with Swagger UI, because SpringFox 3.0.0 does not work for me.

## Homework requirements

### Part 1

* Create a Spring Boot project and design (code) an API for a Market (users have a cart, and add and remove products,
  each user has an id, and also a Wishlist, no need to implement a login system; create all CRUD operations for Users,
  Cart and Wishlist, use H2 (in-memory) as a database)
* Create unit tests for it
* Please make sure to implement all those things, and meet the deadline
* Comments would be a nice addition

Please make sure to have the extra endpoints:

* An endpoint where I can get all the carts from all the users, sorted by the total of the products (a product must have
  a name, price and a quantity)
* An endpoint where I can get all the users sorted by their number of orders (users will also have order history, make
  sure to implement it)

### Part 2

* Implement JWT auth for two endpoints from the previous homework
* Write what endpoints have JWT enabled in `README.md`
* Allow swagger to be reached without auth (SecurityConfiguration)

