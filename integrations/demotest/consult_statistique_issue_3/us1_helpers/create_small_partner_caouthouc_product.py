import os, json, requests

backend_host = os.getenv('BACKEND_HOST', 'http://35.241.189.130:31000')
product_path = backend_host + '/product/partner/'
product_to_create = json.loads('{"name":"Caoutchouc","category":"Caniche","price":300,"origin":"JolyPet_ZooPlus","reference":"ZooPlus_base_name_Caoutchouc"}')
DEFAULT_STORED_QUANTITY=1
def create_product(store_id):
    print('POST Url',product_path)
    print('Object to create',product_to_create)
    product_create_res = requests.post(product_path,
                                      json=product_to_create)
    assert product_create_res.status_code==200
    print("Create status code:", product_create_res.status_code)
    product_create = product_create_res.json()
    print("Create content:", product_create)
    print(f"Storing 1 quantity of {product_create['name']}")
    product_stored_res = requests.get(f"{backend_host}/product/{product_create['reference']}/internal/store/{store_id}/{DEFAULT_STORED_QUANTITY}")
    assert product_stored_res.status_code==200
    print("Stored status code:", product_stored_res.status_code)