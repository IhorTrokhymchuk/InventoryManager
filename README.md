admin@i.com
rootROOT1234@

user@i.com
rootROOT1234@


1. Get app code

Go to url and copy app code:
````angular2html
https://www.dropbox.com/oauth2/authorize?client_id=<APP_KEY>&response_type=code&token_access_type=offline
````

2. Get refresh token
````angular2html
curl https://api.dropbox.com/oauth2/token \
   -d code=<APP_CODE> \
   -d grant_type=authorization_code \
   -u <APP_KEY>:<APP_SECRET>
````