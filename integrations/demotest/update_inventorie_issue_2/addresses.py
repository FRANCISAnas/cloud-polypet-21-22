import os
from dotenv import load_dotenv

load_dotenv()
partner_name = os.getenv("PARTNER_NAME","Ultima")
partner_address = os.getenv("PARTNER_ADDRESS","https://archicloud-j-partner-ultima.herokuapp.com")
partner_host = os.getenv('PARTNER_1_HOST', "https://archicloud-j-partner-ultima.herokuapp.com")
backend_host = os.getenv('BACKEND_HOST', 'http://34.117.40.226/catalog')
customer_care = os.getenv('CUSTOMER_CARE_HOST', 'http://34.117.40.226')