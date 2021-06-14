#Endpoints

##/login (POST)
Requires no authentication or authorization. To login, make a POST request to the endpoint with a body of this format:
{"username": "myusername", "password": "mypassword"}. Will return a JWT token in the response header to be used to authenticate further requests.

##/users (POST)
Requires no authentication or authorization. Users must contain the following:
{
	"givenName": string,
	"familyName": string,
	"username": string,
	"password": string,
	"email": string,
	"phone": string,
	"role": string (must be "ROLE_ADMIN" or "ROLE_CUSTOMER"),
	"isActive": boolean
}


##/users/{id} (PUT)
Requires authentication. If editing a user's information that isn't their own, then that user must have role ROLE_ADMIN
{
	same format as POST
}


##/users/{id} (GET)
Requires authentication. If retrieving a user's information that isn't their own, then that user must have role ROLE_ADMIN

##/users/{id} (DELETE)
Deletes a user. Requires authentication and ROLE_ADMIN.

##/users?page=n&size=m (GET)
Requires authentication and ROLE_ADMIN. Returns a Page (org.springframework.data.domain.Page)object.

##/users?term=s&page=n&size=m
Requires authentication and ROLE_ADMIN. Returns a Page (org.springframework.data.domain.Page)object.
"term" refers to the string of characters to search users for. If searching "tom" it will return every user whose givenName, familyName, username, or email contains "tom"
Can also search for a string of numbers. Will return any users whose email or phone number contains that string of numbers.