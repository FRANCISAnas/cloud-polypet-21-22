import os, json, requests

partner_host = os.getenv('PARTNER_1_HOST', "http://localhost:5000")
#partner_host = os.getenv('PARTNER_1_HOST', "https://archicloud-j-partner-ultima.herokuapp.com") + ':' + os.getenv('PARTNER_1_PORT', '')
product_path = partner_host + '/j/product'

product_to_create = {"name":"Medium Maxi Adult","category":"Croquette","price":350}
#product_to_create = json.loads(os.getenv('PRODUCT','{"name":"Medium Maxi Adult","category":"Croquette","price":350}'))
DEFAULT_STORED_QUANTITY=1

def create_product():
    print("POST Url: ",product_path)
    product_from_partner_res = requests.post(product_path,json=product_to_create)
    assert product_from_partner_res.status_code==200
    print(product_from_partner_res.status_code)
    print(product_from_partner_res.json())
    product_create=product_from_partner_res.json()
    print("Create content:", product_create)
    print(f"Storing 1 quantity of {product_create['name']}")
    product_stored_res = requests.get(f"{partner_host}/j/product/{product_create['reference']}/store/{product_create['store_id']}/{DEFAULT_STORED_QUANTITY}")
    assert product_stored_res.status_code==200
    print("Stored status code:", product_stored_res.status_code)